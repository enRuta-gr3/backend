package com.uy.enRutaBackend.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtAsiento;
import com.uy.enRutaBackend.datatypes.DtDisAsiento;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.DisAsientoViajeRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;

@Service
public class ServiceAsiento implements IServiceAsiento {

	private static final String OK_MESSAGE = "Operación realizada con éxito";
	
    private final AsientoRepository asientoRepository;
    private final ViajeRepository viajeRepository;
    private final DisAsientoViajeRepository asientoViajeRepository;
    private final ModelMapper mapper;
    @Value("${minutos.control.desbloqueo}")
    private int controlDesbloqueo;

    @Autowired
    public ServiceAsiento(AsientoRepository asientoRepository, ViajeRepository viajeRepository, DisAsientoViajeRepository asientoViajeRepository, ModelMapper mapper) {
		this.asientoRepository = asientoRepository;
		this.viajeRepository = viajeRepository;
		this.asientoViajeRepository = asientoViajeRepository;
		this.mapper = mapper;
	}
    
    @Override
	public ResultadoOperacion<?> listarAsientosDeOmnibus(DtViaje viaje) {
		try {
			Optional<Viaje> obtenido = viajeRepository.findById(viaje.getId_viaje());
			Omnibus bus = obtenido.get().getOmnibus();
			List<Asiento> asientos = asientoRepository.findByOmnibus(bus);
			List<DtDisAsiento> asientosDisponibles = new ArrayList<DtDisAsiento>();
			List<EstadoAsiento> estados = new ArrayList<EstadoAsiento>();
			estados.add(EstadoAsiento.OCUPADO);
			estados.add(EstadoAsiento.BLOQUEADO);
			estados.add(EstadoAsiento.LIBRE);
			for (Asiento asiento : asientos) {
				DisAsiento_Viaje disponibilidad = (DisAsiento_Viaje) asientoViajeRepository
						.findByAsientoAndViajeAndEstadoIn(asiento, obtenido.get(), estados);
				if (disponibilidad != null) {
					DtDisAsiento disponibilidadDt = toDt(disponibilidad);
					asientosDisponibles.add(disponibilidadDt);
				} else {
					return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
				}
			}

			if (asientosDisponibles.size() > 0) {
				return new ResultadoOperacion(true, OK_MESSAGE, asientosDisponibles);
			} else {
				return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
			}
		} catch (Exception e) {
			return new ResultadoOperacion(false, e.getLocalizedMessage(), ErrorCode.REQUEST_INVALIDO);
		}
	}
    
    @Override
    public ResultadoOperacion<?> cambiarEstadoDisponibilidad(List<DtDisAsiento> paraCambiar) {
    	try {
    		List<DtDisAsiento> asientosOcupados = asientosOcupados(paraCambiar);
    		List<DtDisAsiento> aMostrar = new ArrayList<DtDisAsiento>();
    		if(asientosOcupados == null || asientosOcupados.size() == 0) {
    			validarDesbloquearBloqueado(paraCambiar);
    			
    			cambiarEstadoAsientos(paraCambiar, EstadoAsiento.BLOQUEADO);
    			
    			aMostrar = setearNuevoEstado(paraCambiar, EstadoAsiento.BLOQUEADO);
    			
    			return new ResultadoOperacion(true, "Asientos bloqueados correctamente", aMostrar);    			
    		} else {
    			procesarAsientosLibres(paraCambiar, asientosOcupados, aMostrar);
    			
    			return new ResultadoOperacion(false, "Algunos asientos están ocupados.", aMostrar);
    		}
    	} catch (Exception e) {
			return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), e.getLocalizedMessage());
		} 
    }


	private void procesarAsientosLibres(List<DtDisAsiento> paraCambiar, List<DtDisAsiento> asientosOcupados, List<DtDisAsiento> aMostrar) {
		List<DtDisAsiento> aProcesar = paraCambiar.stream().filter(e -> !asientosOcupados.contains(e)).collect(Collectors.toList());
		cambiarEstadoAsientos(aProcesar, EstadoAsiento.BLOQUEADO);
		
		aMostrar.addAll(aProcesar);
		aMostrar.addAll(asientosOcupados);
		
	}

	private List<DtDisAsiento> asientosOcupados(List<DtDisAsiento> paraCambiar) {
		List<DtDisAsiento> ocupadosDt = new ArrayList<DtDisAsiento>();
		Viaje viaje = (viajeRepository.findById(paraCambiar.get(0).getViaje().getId_viaje())).get();
		List<EstadoAsiento> estados = Arrays.asList(EstadoAsiento.OCUPADO, EstadoAsiento.BLOQUEADO);
		List<DisAsiento_Viaje> ocupados = asientoViajeRepository.findByViajeAndEstadoIn(viaje, estados);	
		if(ocupados != null && !ocupados.isEmpty()) {
			for(DisAsiento_Viaje ocupado : ocupados) {
				for(DtDisAsiento solicitado : paraCambiar) {
					if(ocupado.getId_disAsiento() == solicitado.getId_disAsiento() 
							&& ocupado.getIdBloqueo() != solicitado.getIdBloqueo()) {
						solicitado.setEstado(ocupado.getEstado());
						solicitado.setIdBloqueo(null);
						ocupadosDt.add(solicitado);
					}
				}
			}
		}
		return ocupadosDt;
	}

	private List<DtDisAsiento> setearNuevoEstado(List<DtDisAsiento> paraCambiar, EstadoAsiento estado) {
		List<DtDisAsiento> aMostrar = new ArrayList<DtDisAsiento>();
		for(DtDisAsiento asiento : paraCambiar) {
			asiento.setEstado(estado);
			aMostrar.add(asiento);
		}
		return aMostrar;		
	}

	private void validarDesbloquearBloqueado(List<DtDisAsiento> paraCambiar) {
		Viaje viaje = (viajeRepository.findById(paraCambiar.get(0).getViaje().getId_viaje())).get();
		List<DisAsiento_Viaje> bloqueadosParaElCliente = asientoViajeRepository.findByViajeAndIdBloqueoAndEstado(viaje,
				paraCambiar.get(0).getIdBloqueo(), EstadoAsiento.BLOQUEADO);
		if (bloqueadosParaElCliente != null && !bloqueadosParaElCliente.isEmpty()) {
			List<DtDisAsiento> bloqueadosDt = new ArrayList<DtDisAsiento>();
			for (DisAsiento_Viaje dispEnBaseLista : bloqueadosParaElCliente) {
				DtDisAsiento bloqueado = new DtDisAsiento();
				bloqueado = toDt(dispEnBaseLista);
				bloqueadosDt.add(bloqueado);
			}

			List<DtDisAsiento> aDesbloquear = bloqueadosDt.stream().filter(e -> !paraCambiar.contains(e)).collect(Collectors.toList());

			System.out.println(aDesbloquear.toString());

			cambiarEstadoAsientos(aDesbloquear, EstadoAsiento.LIBRE);
		}
	}

	private void cambiarEstadoAsientos(List<DtDisAsiento> asientos, EstadoAsiento estado) {
		for(DtDisAsiento asiento : asientos) {
			DisAsiento_Viaje aCambiar = (asientoViajeRepository.findById(asiento.getId_disAsiento())).get();
			aCambiar.setEstado(estado);
			if(estado.equals(EstadoAsiento.LIBRE) || estado.equals(EstadoAsiento.REASIGNADO)) {
				aCambiar.setIdBloqueo(null);
				aCambiar.setFechaBloqueo(null);
			} else if (estado.equals(EstadoAsiento.BLOQUEADO)){
				aCambiar.setIdBloqueo(asiento.getIdBloqueo());
				aCambiar.setFechaBloqueo(new Date());
			}
			aCambiar.setFechaActualizacion(new Date());
			asientoViajeRepository.save(aCambiar);
		}		
	}

	private DtDisAsiento toDt(DisAsiento_Viaje disponibilidad) {
		DtDisAsiento disponibilidadDt = new DtDisAsiento();
		disponibilidadDt.setId_disAsiento(disponibilidad.getId_disAsiento());
		disponibilidadDt.setAsiento(formatAsiento(disponibilidad.getAsiento()));
		disponibilidadDt.setEstado(disponibilidad.getEstado());
		disponibilidadDt.setViaje(fotmatViaje(disponibilidad.getViaje()));
		return disponibilidadDt;
	}

	private DtViaje fotmatViaje(Viaje viaje) {
		DtViaje viajeDt = new DtViaje();
		viajeDt.setId_viaje(viaje.getId_viaje());
		return viajeDt;
	}

	private DtAsiento formatAsiento(Asiento asiento) {
		DtAsiento asientoDt = new DtAsiento();
		asientoDt.setId_asiento(asiento.getId_asiento());
		asientoDt.setNumero_asiento(asiento.getNumeroAsiento());
		asientoDt.setId_omnibus(asiento.getOmnibus().getId_omnibus());
		return asientoDt;
	}

	@Override
	public void desbloquearPorTiempo() {
		List<DisAsiento_Viaje> asientosBloqueados = asientoViajeRepository.findByEstado(EstadoAsiento.BLOQUEADO);
		for (DisAsiento_Viaje asientoBloqueado : asientosBloqueados) {
			if (pasaronCincoMinutos(asientoBloqueado.getFechaBloqueo())) {
				asientoBloqueado.setEstado(EstadoAsiento.LIBRE);
				asientoBloqueado.setFechaBloqueo(null);
				asientoBloqueado.setIdBloqueo(null);
				asientoBloqueado.setFechaActualizacion(new Date());
				asientoViajeRepository.save(asientoBloqueado);
			}
			System.out.println("Se desbloqueo el asiento con idAsientoViaje: " + asientoBloqueado.getId_disAsiento());
		}
	}

	private boolean pasaronCincoMinutos(Date fechaBloqueo) {
		boolean pasaronCincoMinutos = false;
		LocalDateTime fechaBloqueoDateTime = fechaBloqueo.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		LocalDateTime fechaActual = LocalDateTime.now();
		
		long diferencia = Duration.between(fechaBloqueoDateTime, fechaActual).toMinutes();
		if(diferencia >= controlDesbloqueo) {
			pasaronCincoMinutos = true;
		}
		
		return pasaronCincoMinutos;
	}

	@Override
	public List<DisAsiento_Viaje> cambiarEstadoPorVenta(UUID uuidAuth, EstadoAsiento estado) throws Exception {
		List<DisAsiento_Viaje> asientosBloqueados = asientoViajeRepository
				.findByEstadoAndIdBloqueo(EstadoAsiento.BLOQUEADO, uuidAuth.toString());
		if (!asientosBloqueados.isEmpty()) {
			List<DisAsiento_Viaje> ocupados = new ArrayList<DisAsiento_Viaje>();
			for (DisAsiento_Viaje bloqueado : asientosBloqueados) {
				cambiarEstadoPorVenta(bloqueado, estado);
				ocupados.add(bloqueado);
			}
			return ocupados;
		} else {
			throw new Exception("Los asientos no están disponibles");
		}
	}

	private void cambiarEstadoPorVenta(DisAsiento_Viaje bloqueado, EstadoAsiento estado) {
		if(estado.equals(EstadoAsiento.OCUPADO)) {
			bloqueado.setEstado(estado);
		} else {
			bloqueado.setEstado(estado);
			bloqueado.setFechaBloqueo(null);
			bloqueado.setIdBloqueo(null);
		}
		bloqueado.setFechaActualizacion(new Date());
		asientoViajeRepository.save(bloqueado);
	}

	@Override
	public void marcarReasignado(DisAsiento_Viaje asiento) {
		if(asiento.getEstado().equals(EstadoAsiento.BLOQUEADO) && asiento.getIdBloqueo() != null) {
			asiento.setIdBloqueo(null);
		}
		asiento.setEstado(EstadoAsiento.REASIGNADO);
		asiento.setFechaActualizacion(new Date());
		
		asientoViajeRepository.save(asiento);
	}	
	
}
