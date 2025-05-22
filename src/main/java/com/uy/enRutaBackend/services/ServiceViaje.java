package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;
import com.uy.enRutaBackend.persistence.persistence;

import org.springframework.stereotype.Service;

@Service
public class ServiceViaje implements IServiceViaje {

    private final persistence persistence;

    // Inyección de la dependencia Persistence
    public ServiceViaje(persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void RegistrarViaje(Viaje viaje) {
            boolean isSaved = persistence.saveViaje(viaje);
            if (isSaved) {
                System.out.println("✅ Viaje registrado y persistido correctamente.");
            } else {
                System.out.println("❌ Error al registrar viaje.");
            }
    }
}
