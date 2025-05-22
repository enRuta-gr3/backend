package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.icontrollers.IServicePasaje;
import com.uy.enRutaBackend.persistence.persistence;

import org.springframework.stereotype.Service;

@Service
public class ServicePasaje implements IServicePasaje {

    private final persistence persistence;

    // Inyección de la dependencia Persistence
    public ServicePasaje(persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void RegistrarPasaje(Pasaje pasaje) {
        // Aquí puedes validar el pasaje o hacer otras comprobaciones antes de persistirlo
            boolean isSaved = persistence.savePasaje(pasaje);
            if (isSaved) {
                System.out.println("✅ Pasaje registrado y persistido correctamente.");
            } else {
                System.out.println("❌ Error al registrar pasaje.");
            }
    }
}
