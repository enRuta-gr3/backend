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
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.NoExistenViajesException;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.utils.UtilsClass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class ServiceViaje implements IServiceViaje {

	private static String PROCESADO_OK = "Procesado Correctamente";
	private final LocalidadController localidadController;
    private final ViajeRepository vRepository;
    private final UtilsClass utils;
  
    @Autowired
    public ServiceViaje(LocalidadController localidadController, ViajeRepository vRepository, UtilsClass utils) {
		this.localidadController = localidadController; 
		this.vRepository = vRepository;
		this.utils = utils;
	}

	@Override
    public ResultadoOperacion<?> RegistrarViaje(DtViaje viajeDt) {
		Viaje aCrear = dtToEntity(viajeDt);
		try {
			Viaje creado = vRepository.save(aCrear);		
			if (creado != null) {
				DtViaje creadoDt = entityToDt(creado);
				System.out.println("Viaje registrado y persistido correctamente.");
				return new ResultadoOperacion(true, "Viaje creado correctamente", null, creadoDt.toString());
			} else {
				System.out.println("Error al registrar viaje.");
				return new ResultadoOperacion(false, "No se pudo registrar el viaje", "No se pudo registrar el viaje", null);
			}
		} catch (Exception e) {
			System.out.println("Error al registrar viaje.");
			return new ResultadoOperacion(false, "No se pudo registrar el viaje", e.getMessage(), null);
		}
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
			return new ResultadoOperacion(true, PROCESADO_OK, null, listViajesDt);			
		} else {
			throw new NoExistenViajesException("No hay viajes para mostrar.");
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
		viajeDt.setLocalidadOrigen(modelMapper.map(viaje.getLocalidadOrigen(), DtLocalidad.class));
		viajeDt.setLocalidadDestino(modelMapper.map(viaje.getLocalidadDestino(), DtLocalidad.class));
		viajeDt.setEstado(viaje.getEstado().toString());
		viajeDt.setOmnibus(modelMapper.map(viaje.getOmnibus(), DtOmnibus.class));
		viajeDt.setAsientosDisponibles(0);
		return viajeDt;
	}

	private Viaje dtToEntity(DtViaje viajeDt) {
		//TODO implementar mapeo de dt a entidad
		Viaje aCrear = new Viaje();
//		aCrear.set
		return aCrear;
	}
	
	
}
