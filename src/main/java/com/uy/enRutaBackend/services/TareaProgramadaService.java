package com.uy.enRutaBackend.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.entities.Buzon_notificacion;
import com.uy.enRutaBackend.entities.EstadoPasaje;
import com.uy.enRutaBackend.entities.EstadoVenta;
import com.uy.enRutaBackend.entities.EstadoViaje;
import com.uy.enRutaBackend.entities.Historico_estado;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.TareaProgramada;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.icontrollers.ITareaProgramadaService;
import com.uy.enRutaBackend.persistence.BuzonNotificacionRepository;
import com.uy.enRutaBackend.persistence.HistoricoEstadoRepository;
import com.uy.enRutaBackend.persistence.NotificacionRepository;
import com.uy.enRutaBackend.persistence.OmnibusRepository;
import com.uy.enRutaBackend.persistence.PasajeRepository;
import com.uy.enRutaBackend.persistence.TareaProgramadaRepository;
import com.uy.enRutaBackend.persistence.UsuarioRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.utils.AsuntoEmail;
import com.uy.enRutaBackend.utils.NotificacionesHelper;

import jakarta.transaction.Transactional;

@Service
public class TareaProgramadaService implements ITareaProgramadaService {

	private static final Logger logger = LoggerFactory.getLogger(TareaProgramadaService.class);

	@Autowired
	private TareaProgramadaRepository tareaRepo;

	@Autowired
	private OmnibusRepository omnibusRepo;

	@Autowired
	private ViajeRepository viajeRepository;

	@Autowired
	private BuzonNotificacionRepository buzonNotis;

	@Autowired
	private HistoricoEstadoRepository historicoEstadoRepo;

	@Autowired
	private NotificacionRepository notificacionRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasajeRepository pasajeRepository;

	@Autowired
	private NotificacionesHelper notificacionesHelper;

	@Autowired
	private TaskScheduler taskScheduler;

	@Scheduled(fixedRate = 50000) // Cada 50 segundos
	public void verificarTareasPendientes() {
		Timestamp ahora = new Timestamp(new Date().getTime());
		List<TareaProgramada> tareas = tareaRepo.findByFechaEjecucionBefore(ahora);

		for (TareaProgramada tarea : tareas) {
			Omnibus omnibus = tarea.getOmnibus();
			boolean nuevoEstado = tarea.isNuevoEstado();

			// Evitar duplicados (tolerancia de milisegundos)
			boolean yaRegistrado = historicoEstadoRepo.findByOmnibus(omnibus).stream()
					.anyMatch(h -> h.isActivo() == nuevoEstado && h.getFechaInicio() != null
							&& Math.abs(h.getFechaInicio().getTime() - tarea.getFechaEjecucion().getTime()) < 1000);

			if (yaRegistrado) {
				logger.info("Ya existe historial para ómnibus ID {}, estado {}, fecha {}", omnibus.getId_omnibus(),
						nuevoEstado, tarea.getFechaEjecucion());
				tareaRepo.delete(tarea);
				continue;
			}

			// Aplicar nuevo estado al ómnibus
			omnibus.setActivo(nuevoEstado);

			Historico_estado hist = new Historico_estado();
			hist.setOmnibus(omnibus);
			hist.setVendedor(tarea.getVendedor());
			hist.setActivo(nuevoEstado);
			hist.setFechaInicio(tarea.getFechaEjecucion());

			if (!nuevoEstado) {
				// 🟡 DESACTIVANDO: buscar próxima reactivación
				Optional<TareaProgramada> nextReactivationOpt = tareaRepo
						.findTopByOmnibusAndNuevoEstadoAndFechaEjecucionAfterOrderByFechaEjecucionAsc(omnibus, true,
								tarea.getFechaEjecucion());

				if (nextReactivationOpt.isPresent()) {
					Timestamp fechaReactivacion = new Timestamp(
							nextReactivationOpt.get().getFechaEjecucion().getTime());
					hist.setFechaFin(fechaReactivacion);
					omnibus.setFecha_fin(fechaReactivacion); // ✅ SETEAR fecha_fin en Omnibus
				} else {
					hist.setFechaFin(null);
					omnibus.setFecha_fin(null);
					logger.warn("No se encontró reactivación para ómnibus ID {}", omnibus.getId_omnibus());
				}

				// 🆕 ACTUALIZAR HISTORIAL ANTERIOR DE ACTIVACIÓN
				historicoEstadoRepo.findTopByOmnibusAndActivoOrderByFechaInicioDesc(omnibus, true)
						.ifPresent(prevTrue -> {
							prevTrue.setFechaFin(tarea.getFechaEjecucion());
							historicoEstadoRepo.save(prevTrue);
							logger.info("Se cerró el historial TRUE anterior (ID {}) con fecha_fin = {}",
									prevTrue.getId_his_estado(), tarea.getFechaEjecucion());
						});

			} else {
				// 🟢 ACTIVANDO: limpiar fecha_fin y cerrar historial anterior
				omnibus.setFecha_fin(null); // ✅ RESET fecha_fin

				historicoEstadoRepo.findTopByOmnibusAndActivoOrderByFechaInicioDesc(omnibus, false).ifPresent(prev -> {
					prev.setFechaFin(tarea.getFechaEjecucion());
					historicoEstadoRepo.save(prev);
				});

				hist.setFechaFin(null);
			}

			omnibusRepo.save(omnibus);
			historicoEstadoRepo.save(hist);
			tareaRepo.delete(tarea);

			logger.info("Tarea ejecutada para ómnibus ID {} - Nuevo estado: {}", omnibus.getId_omnibus(), nuevoEstado);
		}
	}

	@Scheduled(fixedRate = 60000) // Cada minuto
	@Transactional
	public void cerrarVentasDeViajesProximos() {
		LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Montevideo"));
		LocalDateTime enMediaHora = ahora.plusMinutes(30);

		System.out.println("⏰ Hora actual: " + ahora);
		System.out.println("⏰ Hora en 30 minutos: " + enMediaHora);

		List<Viaje> viajes = viajeRepository.findViajesProximosPorEstado(EstadoViaje.ABIERTO.name(),
				Timestamp.valueOf(ahora), Timestamp.valueOf(enMediaHora));

		System.out.println("🔍 Viajes a revisar: " + viajes.size());

		for (Viaje viaje : viajes) {
			System.out.println("🔎 Revisando viaje ID: " + viaje.getId_viaje());
			System.out.println("    Fecha partida: " + viaje.getFecha_partida());
			System.out.println("    Hora partida: " + viaje.getHora_partida());
			System.out.println("    Hora llegada: " + viaje.getHora_llegada());
			System.out.println("    Estado actual: " + viaje.getEstado());

			List<Pasaje> pasajes = pasajeRepository
					.findByViaje(viaje).stream().filter(p -> p.getEstadoPasaje().equals(EstadoPasaje.VIGENTE)
							&& p.getVenta_compra() != null && p.getVenta_compra().getEstado().equals(EstadoVenta.COMPLETADA))
					.toList();

			System.out.println("    Pasajes vigentes y completados encontrados: " + pasajes.size());

			if (pasajes.isEmpty()) {
				System.out.println("    No hay pasajes, se procede a cancelar el viaje.");
				viajeRepository.actualizarEstadoViaje(viaje.getId_viaje(), EstadoViaje.CANCELADO);
				System.out.println("❌ Viaje cancelado por falta de pasajes: " + viaje.getId_viaje());
			} else {
				System.out.println("    Hay pasajes, se procede a cerrar el viaje y notificar usuarios.");
				viajeRepository.actualizarEstadoViaje(viaje.getId_viaje(), EstadoViaje.CERRADO);
				System.out.println("✅ Viaje cerrado: " + viaje.getId_viaje());

				Set<UUID> usuariosNotificados = new HashSet<>();
				for (Pasaje pasaje : pasajes) {
					Venta_Compra venta = pasaje.getVenta_compra();
					Usuario usuario = venta.getCliente();

					if (usuario == null) {
						System.out.println("    Pasaje sin usuario asociado, se ignora.");
						continue;
					}
					if (usuariosNotificados.contains(usuario.getUuidAuth())) {
						System.out.println("    Usuario " + usuario.getUuidAuth() + " ya fue notificado, se salta.");
						continue;
					}
					usuariosNotificados.add(usuario.getUuidAuth());

					String mensaje = "🚍 Se cerraron las ventas para su viaje del "
							+ viaje.getFecha_partida().toString() + " a las " + viaje.getHora_partida().toString()
							+ ". Recuerde que el viaje parte en 30 minutos.";

					System.out.println("Notificando usuario: " + usuario.getUuidAuth() + " - Mensaje: " + mensaje);

					Buzon_notificacion buzon = notificacionesHelper.obtenerBuzon(usuario);
					if (buzon == null) {
						System.out.println("No se encontró buzón para usuario: " + usuario.getUuidAuth());
						continue;
					}

					notificacionesHelper.generarNotificacion(buzon, mensaje, usuario.getTipoUsuario());
					notificacionesHelper.enviarNotificacionEmail(usuario, AsuntoEmail.CIERRE_VENTA.getAsunto(),
							mensaje);
					notificacionesHelper.enviarNotificacionPush(usuario, AsuntoEmail.CIERRE_VENTA.getAsunto(), mensaje);
					System.out.println("Notificación enviada por email a: " + usuario.getUuidAuth());
				}
			}

			// 🕓 Programar cambio de localidad al final del viaje
			Omnibus omnibus = viaje.getOmnibus();
			if (omnibus != null) {
			    LocalDateTime fechaEjecucion = viaje.getFecha_llegada()
			        .toLocalDate()
			        .atTime(viaje.getHora_llegada().toLocalTime());

			    Instant startInstant = fechaEjecucion
			        .atZone(ZoneId.of("America/Montevideo"))
			        .toInstant();

			    System.out.println("    Programando tarea para actualizar localidad de ómnibus.");
			    System.out.println("    Fecha ejecución: " + fechaEjecucion + " (instant: " + startInstant + ")");

			    taskScheduler.schedule(() -> {
			        Omnibus ombi = omnibusRepo.findById(omnibus.getId_omnibus()).orElse(null);
			        Viaje vj = viajeRepository.findById(viaje.getId_viaje()).orElse(null);

			        if (ombi != null && vj != null) {
			            ombi.setLocalidad_actual(vj.getLocalidadDestino());
			            omnibusRepo.save(ombi);
			            System.out.println("🚍 Localidad del ómnibus actualizada al destino del viaje finalizado.");
			        } else {
			            System.out.println("    Omnibus o viaje no encontrados al ejecutar tarea programada.");
			        }
			    }, startInstant);
			} else {
			    System.out.println("    Viaje sin ómnibus asignado, no se programa actualización de localidad.");
			}
		}
	}

}