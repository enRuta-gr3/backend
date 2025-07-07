package com.uy.enRutaBackend.services;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.uy.enRutaBackend.datatypes.ClaveAgrupacionLocalidad;
import com.uy.enRutaBackend.datatypes.DtDepartamento;
import com.uy.enRutaBackend.datatypes.DtEstadisticaViajesMes;
import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.entities.EstadoViaje;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.NoExistenViajesException;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.DisAsientoViajeRepository;
import com.uy.enRutaBackend.persistence.OmnibusRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.utils.UtilsClass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class ServiceViaje implements IServiceViaje {

	private static final String OK_MESSAGE = "Operación realizada con éxito";
    private final ViajeRepository vRepository;
    private final DisAsientoViajeRepository disAsientosRepository;
    private final AsientoRepository asientoRepository;
    private final OmnibusRepository omnibusRepository;
    private final UtilsClass utils;
    private final IServiceAsiento serviceAsiento;
  
    @Autowired
    public ServiceViaje(ViajeRepository vRepository, DisAsientoViajeRepository disAsientosRepository, AsientoRepository asientoRepository, 
    		OmnibusRepository omnibusRepository, UtilsClass utils, IServiceAsiento serviceAsiento) {
		this.vRepository = vRepository;
		this.disAsientosRepository = disAsientosRepository;
		this.asientoRepository = asientoRepository;
		this.omnibusRepository = omnibusRepository;
		this.utils = utils;
		this.serviceAsiento = serviceAsiento;
	}

	@Override
	public ResultadoOperacion<?> RegistrarViaje(DtViaje viajeDt) {
		try {
			validarOrigenDestino(viajeDt);
			validarInicioFin(viajeDt);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResultadoOperacion(false, e.getMessage(), ErrorCode.REQUEST_INVALIDO);
		}
		Viaje aCrear = dtToEntity(viajeDt);
		if (!vRepository.mismoViaje(aCrear.getLocalidadOrigen().getId_localidad(), aCrear.getLocalidadDestino().getId_localidad(), aCrear.getFecha_partida(),
				aCrear.getHora_partida(), aCrear.getFecha_llegada(), aCrear.getHora_llegada(), aCrear.getOmnibus().getId_omnibus(), EstadoViaje.ABIERTO).isPresent()) {
			try {
				Viaje creado = vRepository.save(aCrear);
				if (creado != null) {
					DtViaje creadoDt = entityToDt(creado);
					cargarTablaControlDisponibilidad(creado, EstadoAsiento.LIBRE);
					System.out.println("Viaje registrado y persistido correctamente.");
					return new ResultadoOperacion(true, OK_MESSAGE, creadoDt);
				} else {
					System.out.println("Error al registrar viaje.");
					return new ResultadoOperacion(false, ErrorCode.ERROR_DE_CREACION.getMsg(), ErrorCode.ERROR_DE_CREACION);
				}
			} catch (Exception e) {
				System.out.println("Error al registrar viaje.");
				return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), ErrorCode.REQUEST_INVALIDO);
			}
		} else {
			return new ResultadoOperacion(false, "Ya existe el viaje", ErrorCode.YA_EXISTE);
		}
	}
	
	private void validarInicioFin(DtViaje viajeDt) throws Exception {
		if(viajeDt.getLocalidadDestino().getId_localidad() == viajeDt.getLocalidadOrigen().getId_localidad())
			throw new Exception("Debe seleccionar localidades distintas");
	}

	private void validarOrigenDestino(DtViaje viajeDt) throws Exception {
		Date fechaLlegada = Date.valueOf(viajeDt.getFecha_llegada());
		Date fechaSalida = Date.valueOf(viajeDt.getFecha_partida());
		Time horaLlegada = Time.valueOf(viajeDt.getHora_llegada());
		Time horaPartida = Time.valueOf(viajeDt.getHora_partida());
		
		if(fechaSalida.before(Date.valueOf(LocalDate.now())) || fechaLlegada.before(Date.valueOf(LocalDate.now()))) {
			throw new Exception("Las fechas no pueden ser menores a la actual.");
		} else if(viajeDt.getFecha_llegada().equals(viajeDt.getFecha_partida())  
				&& viajeDt.getHora_llegada().equals(viajeDt.getHora_partida())) {
			throw new Exception("La fecha de llegada no puede ser igual a la de salida.");
		} else if(fechaSalida.after(fechaLlegada)) {
			throw new Exception("La fecha de salida no puede ser posterior a la de llegada.");
		} else if(fechaSalida.equals(fechaLlegada) && horaPartida.after(horaLlegada)) {
			throw new Exception("La hora de salida no puede ser posterior a la de llegada.");
		}
		
	}

	private List<DisAsiento_Viaje> cargarTablaControlDisponibilidad(Viaje viaje, EstadoAsiento estado) {
		List<DisAsiento_Viaje> asientosDisponibles = new ArrayList<DisAsiento_Viaje>();
		Omnibus omnibus = (omnibusRepository.findById(viaje.getOmnibus().getId_omnibus())).get();
		for(Asiento asiento : asientoRepository.findByOmnibus(omnibus)) {
			DisAsiento_Viaje disponibilidad = new DisAsiento_Viaje();
			disponibilidad.setAsiento(asiento);
			disponibilidad.setViaje(viaje);
			disponibilidad.setEstado(estado); //estado que reciba
			disponibilidad.setFechaActualizacion(new java.util.Date());
			asientosDisponibles.add(disponibilidad);
			System.out.println(disponibilidad.toString());
			disAsientosRepository.save(disponibilidad);
		}
		viaje.setDisponibilidad(asientosDisponibles);
		vRepository.save(viaje);
		return asientosDisponibles;
	}



	@Override
    public ResultadoOperacion<?> listarViajes() throws NoExistenViajesException {
		List<DtViaje> listViajesDt = new ArrayList<DtViaje>();
		List<Viaje> viajes = (List<Viaje>) vRepository.findAll();
		for(Viaje viaje : viajes) {
			DtViaje viajeDt = entityToDt(viaje);
			listViajesDt.add(viajeDt);
		}
		if(listViajesDt.size() > 0) {
			return new ResultadoOperacion(true, OK_MESSAGE, listViajesDt);			
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}
	
	private DtViaje entityToDt(Viaje viaje) {
		DtViaje viajeDt = new DtViaje();
		viajeDt.setId_viaje(viaje.getId_viaje());
		viajeDt.setFecha_partida(utils.dateToString(viaje.getFecha_partida()));
		viajeDt.setFecha_llegada(utils.dateToString(viaje.getFecha_llegada()));
		viajeDt.setHora_partida(utils.timeToString(viaje.getHora_partida()));
		viajeDt.setHora_llegada(utils.timeToString(viaje.getHora_llegada()));
		viajeDt.setPrecio_viaje(viaje.getPrecio_viaje());
		viajeDt.setLocalidadOrigen(getDtLocalidad(viaje.getLocalidadOrigen()));
		viajeDt.setLocalidadDestino(getDtLocalidad(viaje.getLocalidadDestino()));
		viajeDt.setEstado(viaje.getEstado().toString());
		viajeDt.setOmnibus(getDtOmnibus(viaje.getOmnibus()));
		viajeDt.setAsientosDisponibles(calcularAsientos(viaje));
		return viajeDt;
	}
	
	private int calcularAsientos(Viaje viaje) {
		return disAsientosRepository.countByViajeAndEstado(viaje, EstadoAsiento.LIBRE);
	}

	private DtOmnibus getDtOmnibus(Omnibus omnibus) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(Omnibus.class, DtOmnibus.class)
		.addMappings(mapper -> { 
			mapper.skip(DtOmnibus::setHistorico_estado);
			mapper.skip(DtOmnibus::setActivo);
			mapper.skip(DtOmnibus::setFecha_fin);
			mapper.skip(DtOmnibus::setViajes);
			mapper.skip(DtOmnibus::setId_omnibus);
		});
		return modelMapper.map(omnibus, DtOmnibus.class);
	}

	private DtLocalidad getDtLocalidad(Localidad localidad) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(localidad, DtLocalidad.class);
	}

	private Viaje dtToEntity(DtViaje viajeDt) {
		ModelMapper modelMapper = new ModelMapper();
		Viaje aCrear = new Viaje();
		aCrear.setFecha_partida(Date.valueOf(viajeDt.getFecha_partida()));
		aCrear.setFecha_llegada(Date.valueOf(viajeDt.getFecha_llegada()));
		aCrear.setHora_partida(Time.valueOf(viajeDt.getHora_partida()));
		aCrear.setHora_llegada(Time.valueOf(viajeDt.getHora_llegada()));
		aCrear.setPrecio_viaje(viajeDt.getPrecio_viaje());
		aCrear.setLocalidadOrigen(modelMapper.map(viajeDt.getLocalidadOrigen(), Localidad.class));
		aCrear.setLocalidadDestino(modelMapper.map(viajeDt.getLocalidadDestino(), Localidad.class));
		aCrear.setEstado(mapEstado(viajeDt.getEstado()));
		aCrear.setOmnibus(modelMapper.map(viajeDt.getOmnibus(), Omnibus.class));
		return aCrear;
	}

	private EstadoViaje mapEstado(String estado) {
		switch(estado) {
		case "ABIERTO":
			return EstadoViaje.ABIERTO;
		case "CERRADO":
			return EstadoViaje.CERRADO;
		}			
		return null;
	}

	@Override
	public ResultadoOperacion<?> calcularCantidadViajesLocalidad(int anio, int mes) {
		List<DtViaje> estadistica = new ArrayList<DtViaje>();
		List<Object[]> viajesLocalidad = vRepository.contarViajes();		
		if(viajesLocalidad.size() > 0) {
			
			
			Map<ClaveAgrupacionLocalidad, Long> agrupadosPorLocalidad = viajesLocalidad.stream()
				.filter(obj -> {
		            Date fecha = (Date) obj[2];
		            LocalDate fechaLocal = fecha.toLocalDate();
		            return fechaLocal.getMonthValue() == mes && fechaLocal.getYear() == anio;
		        })
				.collect(Collectors.groupingBy(
			            obj -> new ClaveAgrupacionLocalidad((String) obj[0], (String) obj[1]),
			            Collectors.summingLong(obj -> (Long) obj[3])
			        ));
			    estadistica = agrupadosPorLocalidad.entrySet().stream()
			        .map(e -> crearDtViaje(e.getKey().nombre(), e.getKey().localidad(), e.getValue()))
			        .collect(Collectors.toList());
	
			return new ResultadoOperacion(true, OK_MESSAGE, estadistica);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}
	
	private DtViaje crearDtViaje(String nombreLocalidad, String nombreDepartamento, long cantidad) {
		DtViaje dtViaje = new DtViaje();
		dtViaje.setLocalidadOrigen(crearDtLocalidad(nombreLocalidad, nombreDepartamento));
		dtViaje.setCantidad((int) cantidad);
		return dtViaje;
	}

	private DtLocalidad crearDtLocalidad(String nombreLocalidad, String nombreDepartamento) {
		DtLocalidad localidad = new DtLocalidad();
		localidad.setNombreLocalidad(nombreLocalidad);
		DtDepartamento depto = new DtDepartamento();
		depto.setNombreDepartamento(nombreDepartamento);
		localidad.setDepartamento(depto);
		return localidad;
	}
	
	@Transactional
	@Override
	public ResultadoOperacion<?> reasignarOmnibus(int idViaje, int idOmnibus) {
		try {			
			Viaje aReasignar = vRepository.findById(idViaje).get();
			Omnibus anterior = aReasignar.getOmnibus();
			List<Asiento> asientosOmnibusAnterior = asientoRepository.findByOmnibus(anterior);
			//Reasigno nuevo omnibus
			Viaje reasignado = reasignar(aReasignar, idOmnibus);
			//Creo los asientos para el viaje
			List<DisAsiento_Viaje> asientosDisponibles = cargarTablaControlDisponibilidad(reasignado, EstadoAsiento.PENDIENTE);
			
			List<Asiento> asientosOmnibusNuevo = asientoRepository.findByOmnibus(reasignado.getOmnibus());
			
			//busco asientos libres
			List<DisAsiento_Viaje> libres = asientosLibres(reasignado, asientosOmnibusAnterior, asientosDisponibles);
			if(!libres.isEmpty()) {
				reasignarAsientosLibres(libres, reasignado, asientosOmnibusNuevo);
			}
			
			//busco asientos bloqueados
			List<DisAsiento_Viaje> bloqueados = tieneAsientosBloqueados(reasignado, asientosOmnibusAnterior);
			if(!bloqueados.isEmpty()) {
				reasignarAsientosBloqueados(bloqueados, reasignado, asientosOmnibusNuevo);
			}
			
			//busco los asientos ocupados y asigno los nuevos
			List<DisAsiento_Viaje> vendidos = tienePasajesVendidos(reasignado, asientosOmnibusAnterior);
			if(!vendidos.isEmpty()) {
				reasignarAsientosVendidos(vendidos, reasignado, asientosOmnibusNuevo);
			}
			
			return new ResultadoOperacion(true, OK_MESSAGE, "Reasignacion completada correctamente.");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        return new ResultadoOperacion<>(false, "Ocurrió un error durante la reasignacion", e.getMessage());

		}
	}


	/**
	 * @param idViaje
	 * @param idOmnibus
	 * @return
	 */
	private Viaje reasignar(Viaje viaje, int idOmnibus) {		
		Omnibus nuevoBus = omnibusRepository.findById(idOmnibus).get();
		viaje.setOmnibus(nuevoBus);
		Viaje reasignado = vRepository.save(viaje);
		return reasignado;
	}

	private List<DisAsiento_Viaje> asientosLibres(Viaje reasignado, List<Asiento> asientosOmnibusAnterior, List<DisAsiento_Viaje> nuevosDisponibles) {
		List<DisAsiento_Viaje> asientosLibres = new ArrayList<DisAsiento_Viaje>();
		
		List<Asiento> asientosNuevos = nuevosDisponibles.stream()
			    .map(DisAsiento_Viaje::getAsiento)
			    .collect(Collectors.toList());

		//Me fijo si hay numeros de asiento que no esten en el omnibus anterior (omnibus nuevo mayor capacidad)
		Set<Integer> numerosAsientosAnterior = asientosOmnibusAnterior.stream()
			    .map(Asiento::getNumeroAsiento)
			    .collect(Collectors.toSet());

			List<Asiento> nuevosDisponiblesNoEnAnterior = asientosNuevos.stream()
			    .filter(asiento -> !numerosAsientosAnterior.contains(asiento.getNumeroAsiento()))
			    .collect(Collectors.toList());
		//Si hay mas asientos, los agrego a la lista de asientos libres.
		if(nuevosDisponiblesNoEnAnterior.size() > 0) {
			asientosLibres = nuevosDisponibles.stream()
					.filter(disponible -> nuevosDisponiblesNoEnAnterior.contains(disponible.getAsiento()))
					.collect(Collectors.toList());
		}
		
		//recorro los asientos del omnibus anterior en estado libre y los guardo en la lista.
		for(Asiento asiento : asientosOmnibusAnterior) {
			DisAsiento_Viaje libre = (DisAsiento_Viaje) disAsientosRepository.findByAsientoAndViajeAndEstado(asiento, reasignado, EstadoAsiento.LIBRE);
			if(libre != null){
				asientosLibres.add(libre);
			}
		}
		return asientosLibres;
	}

	private void reasignarAsientosLibres(List<DisAsiento_Viaje> libres, Viaje reasignado,
			List<Asiento> asientosOmnibusNuevo) {
		try {
			for (DisAsiento_Viaje asientoLibre : libres) {
				int nroAsientoLibre = asientoLibre.getAsiento().getNumeroAsiento();
				serviceAsiento.marcarReasignado(asientoLibre); // reasigno el asiento anterior

				for (Asiento nuevoAsiento : asientosOmnibusNuevo) {
					if (nuevoAsiento.getNumeroAsiento() == nroAsientoLibre) {
						DisAsiento_Viaje asientoReasignar = (DisAsiento_Viaje) disAsientosRepository
								.findByAsientoAndViajeAndEstado(nuevoAsiento, reasignado, EstadoAsiento.PENDIENTE); // fecha
																													// reciente
						asientoReasignar.setEstado(EstadoAsiento.LIBRE);
						asientoReasignar.setFechaActualizacion(new java.util.Date());
						disAsientosRepository.save(asientoReasignar);
					}
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		}
	}
	
	private List<DisAsiento_Viaje> tieneAsientosBloqueados(Viaje reasignado, List<Asiento> asientosOmnibusAnterior) {
		List<DisAsiento_Viaje> asientosBloqueados = new ArrayList<DisAsiento_Viaje>();		
		for(Asiento asiento : asientosOmnibusAnterior) {
			DisAsiento_Viaje bloqueado = (DisAsiento_Viaje) disAsientosRepository.findByAsientoAndViajeAndEstado(asiento, reasignado, EstadoAsiento.BLOQUEADO);
			if(bloqueado != null){
				asientosBloqueados.add(bloqueado);
			}
		}
		return asientosBloqueados;	
	}
	
	private void reasignarAsientosBloqueados(List<DisAsiento_Viaje> bloqueados, Viaje reasignado, List<Asiento> asientosOmnibusNuevo) {
		try {	
			
			for(DisAsiento_Viaje asientoBloqueado : bloqueados) {
				int nroAsientoBloqueado = asientoBloqueado.getAsiento().getNumeroAsiento();
				String idBloqueo = asientoBloqueado.getIdBloqueo();
				java.util.Date fechaBloqueo = asientoBloqueado.getFechaBloqueo();
				
				serviceAsiento.marcarReasignado(asientoBloqueado); //reasigno el asiento anterior
				
				for(Asiento nuevoAsiento : asientosOmnibusNuevo) {
					if(nuevoAsiento.getNumeroAsiento() == nroAsientoBloqueado) {				
						DisAsiento_Viaje asientoReasignar = (DisAsiento_Viaje) disAsientosRepository.findByAsientoAndViajeAndEstado(nuevoAsiento, reasignado, EstadoAsiento.PENDIENTE); //fecha reciente
						asientoReasignar.setIdBloqueo(idBloqueo);
						asientoReasignar.setFechaBloqueo(fechaBloqueo);
						asientoReasignar.setEstado(EstadoAsiento.BLOQUEADO);
						asientoReasignar.setFechaActualizacion(new java.util.Date());
						disAsientosRepository.save(asientoReasignar);
					}
				}
			}			
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		}
	}
	
	private List<DisAsiento_Viaje> tienePasajesVendidos(Viaje reasignado, List<Asiento> asientosOmnibusAnterior) {
		List<DisAsiento_Viaje> asientosVendidos = new ArrayList<DisAsiento_Viaje>();		
		for(Asiento asiento : asientosOmnibusAnterior) {
			DisAsiento_Viaje ocupado = (DisAsiento_Viaje) disAsientosRepository.findByAsientoAndViajeAndEstado(asiento, reasignado, EstadoAsiento.OCUPADO);
			if(ocupado != null){
				asientosVendidos.add(ocupado);
			}
		}
		return asientosVendidos;
	}
	
	private void reasignarAsientosVendidos(List<DisAsiento_Viaje> vendidos, Viaje reasignado, List<Asiento> asientosOmnibusNuevo) {
		try {
			
			for(DisAsiento_Viaje asientoVendido : vendidos) {
				int nroAsientoVendido = asientoVendido.getAsiento().getNumeroAsiento();
				String idBloqueo = asientoVendido.getIdBloqueo();
				
				serviceAsiento.marcarReasignado(asientoVendido); //reasigno el asiento anterior
				
				for(Asiento nuevoAsiento : asientosOmnibusNuevo) {
					if(nuevoAsiento.getNumeroAsiento() == nroAsientoVendido) {				
						DisAsiento_Viaje asientoReasignar = (DisAsiento_Viaje) disAsientosRepository.findByAsientoAndViajeAndEstado(nuevoAsiento, reasignado, EstadoAsiento.PENDIENTE);
						asientoReasignar.setIdBloqueo(idBloqueo);
						asientoReasignar.setEstado(EstadoAsiento.OCUPADO);
						asientoReasignar.setFechaActualizacion(new java.util.Date());
						disAsientosRepository.save(asientoReasignar);
					}
				}
			}			
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		}
	}
	
	
	@Override
	public ResultadoOperacion<?> calcularCantidadViajesPorMesAlAnio(int anio) {
		List<Viaje> viajesEnAnio = vRepository.obtenerViajesPorAnio(anio);
		if(viajesEnAnio.size() > 0) {
			Map<YearMonth, Long> viajesPorMes = viajesEnAnio.stream()
					.collect(Collectors.groupingBy(
							viaje -> YearMonth.from(viaje.getFecha_partida().toLocalDate()), 
							Collectors.counting()
							));
			List<DtEstadisticaViajesMes> estadisticaPorMes = viajesPorMes.entrySet().stream()
					.map(est -> crearDtViajesPorMes(est.getKey(), est.getValue()))
					.collect(Collectors.toList());
			
			return new ResultadoOperacion(true, OK_MESSAGE, estadisticaPorMes);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}

	private DtEstadisticaViajesMes crearDtViajesPorMes(YearMonth mes, Long cantidad) {
		DtEstadisticaViajesMes viajesMes = new DtEstadisticaViajesMes();
		String[] separaAnioMes = mes.toString().split("-");
		viajesMes.setMes(separaAnioMes[1]);
		viajesMes.setAnio(separaAnioMes[0]);
		viajesMes.setCantidad(cantidad.intValue());
		return viajesMes;
	}

	@Override
	public ResultadoOperacion<?> listarViajesPorOmnibus(int idOmnibus) {
		List<DtViaje> viajesDt = new ArrayList<DtViaje>();
		Omnibus omnibus = omnibusRepository.findById(idOmnibus).get();
		List<Viaje> viajes = vRepository.findByOmnibus(omnibus);
		if(viajes.size() > 0) {
			viajesDt = viajes.stream()
				    .map(this::entityToDt)
				    .toList();
		}
		if(viajesDt.size() > 0) {
			return new ResultadoOperacion(true, OK_MESSAGE, viajesDt);			
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}

}
