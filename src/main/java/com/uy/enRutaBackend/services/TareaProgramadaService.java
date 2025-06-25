package com.uy.enRutaBackend.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.entities.Buzon_notificacion;
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

    @Scheduled(fixedRate = 50000) // Cada 50 segundos
    public void verificarTareasPendientes() {
        Timestamp ahora = new Timestamp(new Date().getTime());
        List<TareaProgramada> tareas = tareaRepo.findByFechaEjecucionBefore(ahora);

        for (TareaProgramada tarea : tareas) {
            Omnibus omnibus = tarea.getOmnibus();
            boolean nuevoEstado = tarea.isNuevoEstado();

            // Evitar duplicados (tolerancia de milisegundos)
            boolean yaRegistrado = historicoEstadoRepo
                .findByOmnibus(omnibus)
                .stream()
                .anyMatch(h -> h.isActivo() == nuevoEstado &&
                               h.getFechaInicio() != null &&
                               Math.abs(h.getFechaInicio().getTime() - tarea.getFechaEjecucion().getTime()) < 1000);

            if (yaRegistrado) {
                logger.info("Ya existe historial para ómnibus ID {}, estado {}, fecha {}", 
                    omnibus.getId_omnibus(), nuevoEstado, tarea.getFechaEjecucion());
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
                Optional<TareaProgramada> nextReactivationOpt =
                    tareaRepo.findTopByOmnibusAndNuevoEstadoAndFechaEjecucionAfterOrderByFechaEjecucionAsc(
                        omnibus, true, tarea.getFechaEjecucion());

                if (nextReactivationOpt.isPresent()) {
                	Timestamp fechaReactivacion = new Timestamp(nextReactivationOpt.get().getFechaEjecucion().getTime());
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

                historicoEstadoRepo.findTopByOmnibusAndActivoOrderByFechaInicioDesc(omnibus, false)
                    .ifPresent(prev -> {
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

        List<Pasaje> pasajes = pasajeRepository.findPasajesDeViajesCercanos(
        	    EstadoViaje.ABIERTO.name(),
        	    Timestamp.valueOf(ahora),
        	    Timestamp.valueOf(enMediaHora)
        	);
        
        System.out.println("Ahora: " + ahora.toString());
        System.out.println("Media hora: " + enMediaHora.toString());
        

        // Agrupar por viaje
        Map<Viaje, List<Pasaje>> agrupadosPorViaje = pasajes.stream()
            .collect(Collectors.groupingBy(Pasaje::getViaje));
        
        System.out.println("Viajes a cerrar: " + agrupadosPorViaje.size());

        System.out.println("Viajes a cerrar: " + agrupadosPorViaje.size());
        for (Map.Entry<Viaje, List<Pasaje>> entry : agrupadosPorViaje.entrySet()) {
            Viaje viaje = entry.getKey();
            List<Pasaje> pasajesDelViaje = entry.getValue();

            viaje.setEstado(EstadoViaje.CERRADO);
            viajeRepository.save(viaje);
            
            System.out.println("Se cierra el viaje a: " + viaje.getLocalidadDestino().getNombre());

            Set<UUID> usuariosNotificados = new HashSet<>();

            for (Pasaje pasaje : pasajesDelViaje) {
                Venta_Compra venta = pasaje.getVenta_compra();
                Usuario usuario = venta.getCliente();

                if (usuario == null || usuariosNotificados.contains(usuario.getUuidAuth()))
                    continue;

                usuariosNotificados.add(usuario.getUuidAuth());
                
                String mensaje = "🚍 Se cerraron las ventas para su viaje del " +
                        viaje.getFecha_partida().toString() + " a las " +
                        viaje.getHora_partida().toString() + ". Recuerde que el viaje parte en 30 minutos.";

                Buzon_notificacion buzon = notificacionesHelper.obtenerBuzon(usuario);
                if (buzon == null) {
                    continue;
                }
                
                notificacionesHelper.generarNotificacion(buzon, mensaje, usuario.getTipoUsuario());
                
                notificacionesHelper.enviarNotificacionEmail(usuario, AsuntoEmail.CIERRE_VENTA.getAsunto(), mensaje);

            }
        }
    }


}