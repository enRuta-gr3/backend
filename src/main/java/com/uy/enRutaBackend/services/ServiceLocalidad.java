package com.uy.enRutaBackend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;
import com.uy.enRutaBackend.persistence.LocalidadRepository;

@Service
public class ServiceLocalidad implements IServiceLocalidad {

    private final LocalidadRepository repository;

    @Autowired
    public ServiceLocalidad(LocalidadRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResultadoOperacion<?> RegistrarLocalidad(DtLocalidad localidadDt) {
        if (localidadDt != null && localidadDt.getNombreLocalidad() != null && !localidadDt.getNombreLocalidad().isEmpty()) {
        	
//            boolean isSaved = persistence.saveLocalidad(localidad);
//            if (isSaved) {
//                System.out.println("Localidad registrada y persistida correctamente.");
//            } else {
//                System.out.println("Error al registrar localidad.");
//            }
        } else {
            System.out.println("Datos de localidad inválidos.");
        }
		return null;
    }

    private Localidad doToEntity(DtLocalidad localidadDt) {
    	ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(localidadDt, Localidad.class);
    }

	private DtLocalidad entityToDt(Localidad localidad) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(localidad, DtLocalidad.class);
	}
    
}
