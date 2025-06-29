package com.uy.enRutaBackend.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.entities.Buzon_notificacion;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.entities.EstadoPasaje;
import com.uy.enRutaBackend.entities.EstadoTransaccion;
import com.uy.enRutaBackend.entities.Medio_de_Pago;
import com.uy.enRutaBackend.entities.Pago;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceVendedor;
import com.uy.enRutaBackend.persistence.DisAsientoViajeRepository;
import com.uy.enRutaBackend.persistence.MedioDePagoRepository;
import com.uy.enRutaBackend.persistence.PagoRepository;
import com.uy.enRutaBackend.persistence.PasajeRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.utils.AsuntoEmail;
import com.uy.enRutaBackend.utils.NotificacionesHelper;
import com.uy.enRutaBackend.utils.UtilsClass;



@Service
public class ServiceVendedor implements IServiceVendedor {
	
	private final PagoRepository pagoRepository;
	private final DisAsientoViajeRepository asientoViajeRepository;
	private final PasajeRepository pasajeRepository;
	private final MedioDePagoRepository mpRepository;
	@Value("${tiempo.maximo.devolucion}")
	private int tiempoMaximoDevolucion;
	
	private final UtilsClass utils;
	private final NotificacionesHelper notificacionesHelper;
	
	@Autowired
	public ServiceVendedor(ViajeRepository viajeRepository, PagoRepository pagoRepository,
			DisAsientoViajeRepository asientoViajeRepository, PasajeRepository pasajeRepository, 
			MedioDePagoRepository mpRepository, UtilsClass utils, NotificacionesHelper notificacionesHelper) {
		this.pagoRepository = pagoRepository;
		this.asientoViajeRepository = asientoViajeRepository;
		this.pasajeRepository = pasajeRepository;
		this.mpRepository = mpRepository;
		this.utils = utils;
		this.notificacionesHelper = notificacionesHelper;
	}

	@Override
	public ResultadoOperacion<?> devolverPasajes(List<DtPasaje> pasajesDt) {
		List<Pasaje> pasajes = new ArrayList<Pasaje>();
		for(DtPasaje pasajeDt : pasajesDt) {
			Pasaje p = pasajeRepository.findById(pasajeDt.getId_pasaje()).get();
			pasajes.add(p);
		}
		//verifico tiempo mayor a lo estipulado
		boolean enTiempoParaDevolver = restriccionPorHoraPartidaViaje(pasajes.get(0).getViaje());
		if(enTiempoParaDevolver) {
			double montoDevolver = 0;
			List<String> idPasajesDevueltos = new ArrayList<String>();
			for(Pasaje pasaje : pasajes) {
				montoDevolver = montoDevolver + pasaje.getPrecio();
				
				idPasajesDevueltos.add(String.valueOf(pasaje.getId_pasaje()));
				
				//libero asientos bloqueados
				liberarAsiento(pasaje);

				//marco pasajes como devueltos
				marcarPasajeDevuelto(pasaje);			
			}
			//creo el pago negativo por el monto de los pasajes a devolver
			crearPagoNegativo(montoDevolver);
			
			generarNotificaciones(idPasajesDevueltos, montoDevolver, pasajes.get(0).getVenta_compra(), pasajes.get(0).getViaje());
			
			return new ResultadoOperacion(true, "Devolución completada con éxito", null);
		} else {
			return new ResultadoOperacion(false, ErrorCode.ERROR_DEVOLUCION.getMsg(), ErrorCode.ERROR_DEVOLUCION);
		}
	}


	private boolean restriccionPorHoraPartidaViaje(Viaje viaje) {
		boolean enTiempo = true;
		LocalDate fechaPartida = (viaje.getFecha_partida()).toLocalDate();
		LocalTime horaPartida = (viaje.getHora_partida()).toLocalTime();
		LocalDateTime fechaPartidaViaje = LocalDateTime.of(fechaPartida, horaPartida);
		LocalDateTime fechaActual = LocalDateTime.now();
		
		long diferenciaTiempo = Math.abs(Duration.between(fechaPartidaViaje, fechaActual).toMinutes());
		if(diferenciaTiempo <= tiempoMaximoDevolucion) {
			enTiempo = false;
		}
		
		return enTiempo;
	}

	private void liberarAsiento(Pasaje pasaje) {
		List<EstadoAsiento> estados = new ArrayList<EstadoAsiento>();
		estados.add(EstadoAsiento.OCUPADO);
		DisAsiento_Viaje disAsiento = (DisAsiento_Viaje) asientoViajeRepository.findByAsientoAndViajeAndEstadoIn(pasaje.getAsiento(), pasaje.getViaje(), estados);
		disAsiento.setEstado(EstadoAsiento.LIBRE);
		disAsiento.setFechaBloqueo(null);
		disAsiento.setIdBloqueo(null);
		asientoViajeRepository.save(disAsiento);
	}
	
	private void marcarPasajeDevuelto(Pasaje pasaje) {
		pasaje.setEstadoPasaje(EstadoPasaje.DEVUELTO);
		pasaje.setFechaDevolucion(java.sql.Date.valueOf(LocalDate.now(ZoneId.of("America/Montevideo"))));
		pasajeRepository.save(pasaje);
	}
	
	private void crearPagoNegativo(double monto) {
		Pago pago = new Pago();
		pago.setEstado_trx(EstadoTransaccion.APROBADA);
		pago.setMonto(-monto);
		Medio_de_Pago mp = mpRepository.findByNombre("Efectivo");
		pago.setMedio_de_pago(mp);
		pagoRepository.save(pago);		
	}
	

	private void generarNotificaciones(List<String> idPasajesDevueltos, double montoDevolver,
			Venta_Compra venta_compra, Viaje viaje) {
		String mensaje = new String();
		
		if(idPasajesDevueltos.size() > 1) {
			String listaPasajes = String.join(", Nro ", idPasajesDevueltos.subList(0, idPasajesDevueltos.size() - 1))
	        + " y Nro " + idPasajesDevueltos.get(idPasajesDevueltos.size() - 1);

	
			mensaje = String.format("Se devolvieron los pasajes Nro %s para el viaje hacia %s - %s, del día %s, hora %s. Por un monto total de $%s.",
					listaPasajes,
					viaje.getLocalidadDestino().getNombre(),
					viaje.getLocalidadDestino().getDepartamento().getNombre(),
					utils.dateToString(viaje.getFecha_partida()),
					utils.timeToString(viaje.getHora_partida()),
					montoDevolver);
		} else {
			mensaje = String.format("Se devolvió el pasaje Nro %s, para el viaje hacia %s - %s, del día %s, hora %s. Por un monto total de $%s.",
					idPasajesDevueltos.get(0),
					viaje.getLocalidadDestino().getNombre(),
					viaje.getLocalidadDestino().getDepartamento().getNombre(),
					utils.dateToString(viaje.getFecha_partida()),
					utils.timeToString(viaje.getHora_partida()),
					montoDevolver);
		}
		Buzon_notificacion buzon = notificacionesHelper.obtenerBuzon(venta_compra.getCliente());
		
		notificacionesHelper.generarNotificacion(buzon, mensaje, "CLIENTE");
		
		notificacionesHelper.enviarNotificacionEmail(venta_compra.getCliente(), AsuntoEmail.DEVOLUCION.getAsunto(), mensaje);
		
		notificacionesHelper.enviarNotificacionPush(venta_compra.getCliente(), AsuntoEmail.DEVOLUCION.getAsunto(), mensaje);
	}

}
