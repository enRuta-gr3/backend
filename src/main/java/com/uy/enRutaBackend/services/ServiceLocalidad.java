package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;
import com.uy.enRutaBackend.persistence.persistence;

import org.springframework.stereotype.Service;

@Service
public class ServiceLocalidad implements IServiceLocalidad {

    private final persistence persistence;

    // Inyección de la dependencia Persistence
    public ServiceLocalidad(persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void RegistrarLocalidad(Localidad localidad) {
        if (localidad != null && localidad.getNombre() != null && !localidad.getNombre().isEmpty()) {
            boolean isSaved = persistence.saveLocalidad(localidad);
            if (isSaved) {
                System.out.println("✅ Localidad registrada y persistida correctamente.");
            } else {
                System.out.println("❌ Error al registrar localidad.");
            }
        } else {
            System.out.println("❌ Datos de localidad inválidos.");
        }
    }
}
