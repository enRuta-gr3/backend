package com.uy.enRutaBackend.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.controllers.LocalidadController;
import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.NoExistenViajesException;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.DisAsientoViajeRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.utils.UtilsClass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class ServiceViaje implements IServiceViaje {

	private static final String OK_MESSAGE = "Operación realizada con éxito";
	private final LocalidadController localidadController;
    private final ViajeRepository vRepository;
    private final DisAsientoViajeRepository disAsientosRepository;
    private final AsientoRepository asientoRepository;
    private final UtilsClass utils;
  
    @Autowired
    public ServiceViaje(LocalidadController localidadController, ViajeRepository vRepository, DisAsientoViajeRepository disAsientosRepository, AsientoRepository asientoRepository, UtilsClass utils) {
		this.localidadController = localidadController; 
		this.vRepository = vRepository;
		this.disAsientosRepository = disAsientosRepository;
		this.asientoRepository = asientoRepository;
		this.utils = utils;
	}

	@Override
    public ResultadoOperacion<?> RegistrarViaje(DtViaje viajeDt) {
		validarOrigenDestino();
		validarInicioFin();
		Viaje aCrear = dtToEntity(viajeDt);
		try {			
			Viaje creado = vRepository.save(aCrear);		
			if (creado != null) {
				DtViaje creadoDt = entityToDt(creado);
				System.out.println("Viaje registrado y persistido correctamente.");
				return new ResultadoOperacion(true, OK_MESSAGE, creadoDt.toString());
			} else {
				System.out.println("Error al registrar viaje.");
				return new ResultadoOperacion(false, ErrorCode.ERROR_DE_CREACION.getMsg(), ErrorCode.ERROR_DE_CREACION);
			}
		} catch (Exception e) {
			System.out.println("Error al registrar viaje.");
			return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), ErrorCode.REQUEST_INVALIDO + e.getMessage());
		}
    }
	
	private ResultadoOperacion<?> validarInicioFin() {
		// TODO Auto-generated method stub
		return null;
	}

	private ResultadoOperacion<?> validarOrigenDestino() {
		// TODO Auto-generated method stub
		return null;
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
		ModelMapper modelMapper = new ModelMapper();
		
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
		int ocupados = 0;
		int capacidad = viaje.getOmnibus().getCapacidad();
		List<Asiento> asientos = asientoRepository.findByOmnibus(viaje.getOmnibus());
		for(Asiento asiento : asientos) {
			if(disAsientosRepository.findByAsientoAndViaje(asiento, viaje) != null) {
				ocupados = ocupados + 1;				
			}
		}
		return capacidad - ocupados;
	}

	private DtOmnibus getDtOmnibus(Omnibus omnibus) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(Omnibus.class, DtOmnibus.class)
		.addMappings(mapper -> { 
//			mapper.skip(DtOmnibus::setLocalidad_actual);
			mapper.skip(DtOmnibus::setHistorico_estado);
			mapper.skip(DtOmnibus::setActivo);
			mapper.skip(DtOmnibus::setFecha_fin);
			mapper.skip(DtOmnibus::setAsientos);
			mapper.skip(DtOmnibus::setViajes);
			mapper.skip(DtOmnibus::setId_omnibus);
		});
		return modelMapper.map(omnibus, DtOmnibus.class);
	}

	private DtLocalidad getDtLocalidad(Localidad localidad) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(Localidad.class, DtLocalidad.class)
		.addMappings(mapper -> mapper.skip(DtLocalidad::setId_localidad));
		return modelMapper.map(localidad, DtLocalidad.class);
	}

	private Viaje dtToEntity(DtViaje viajeDt) {
		//TODO implementar mapeo de dt a entidad
		Viaje aCrear = new Viaje();
//		aCrear.set
		return aCrear;
	}
	
	
}
