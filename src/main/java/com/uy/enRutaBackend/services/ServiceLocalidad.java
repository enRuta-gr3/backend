package com.uy.enRutaBackend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;
import com.uy.enRutaBackend.persistence.LocalidadRepository;
import com.uy.enRutaBackend.persistence.persistence;

@Service
public class ServiceLocalidad implements IServiceLocalidad {

    private final persistence persistence;
    private final LocalidadRepository repository;

    @Autowired
    public ServiceLocalidad(persistence persistence, LocalidadRepository repository) {
        this.persistence = persistence;
        this.repository = repository;
    }

    @Override
    public void RegistrarLocalidad(Localidad localidad) {
        if (localidad != null && localidad.getNombre() != null && !localidad.getNombre().isEmpty()) {
            boolean isSaved = persistence.saveLocalidad(localidad);
            if (isSaved) {
                System.out.println("Localidad registrada y persistida correctamente.");
            } else {
                System.out.println("Error al registrar localidad.");
            }
        } else {
            System.out.println("Datos de localidad inválidos.");
        }
    }

	@Override
	public DtLocalidad convertirLocalidadADt(Localidad localidad) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(localidad, DtLocalidad.class);
	}
    
}
