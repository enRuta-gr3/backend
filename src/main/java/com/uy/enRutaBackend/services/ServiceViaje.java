package com.uy.enRutaBackend.services;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  
    @Autowired
    public ServiceViaje(ViajeRepository vRepository, DisAsientoViajeRepository disAsientosRepository, AsientoRepository asientoRepository, OmnibusRepository omnibusRepository, UtilsClass utils) {
		this.vRepository = vRepository;
		this.disAsientosRepository = disAsientosRepository;
		this.asientoRepository = asientoRepository;
		this.omnibusRepository = omnibusRepository;
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
				cargarTablaControlDisponibilidad(creado);
				System.out.println("Viaje registrado y persistido correctamente.");
				return new ResultadoOperacion(true, OK_MESSAGE, creadoDt);
			} else {
				System.out.println("Error al registrar viaje.");
				return new ResultadoOperacion(false, ErrorCode.ERROR_DE_CREACION.getMsg(), ErrorCode.ERROR_DE_CREACION);
			}
		} catch (Exception e) {
			System.out.println("Error al registrar viaje.");
			return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), ErrorCode.REQUEST_INVALIDO + e.getMessage());
		}
    }
	
	private void cargarTablaControlDisponibilidad(Viaje viaje) {
		Omnibus omnibus = (omnibusRepository.findById(viaje.getOmnibus().getId_omnibus())).get();
		for(Asiento asiento : asientoRepository.findByOmnibus(omnibus)) {
			DisAsiento_Viaje disponibilidad = new DisAsiento_Viaje();
			disponibilidad.setAsiento(asiento);
			disponibilidad.setViaje(viaje);
			disponibilidad.setEstado(EstadoAsiento.LIBRE);
			disAsientosRepository.save(disponibilidad);
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
	
	
}
