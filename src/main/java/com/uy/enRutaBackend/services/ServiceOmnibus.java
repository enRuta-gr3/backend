package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;
import com.uy.enRutaBackend.persistence.persistence;

import org.springframework.stereotype.Service;

@Service
public class ServiceOmnibus implements IServiceOmnibus {

    private final persistence persistence;

    // Inyección de la dependencia Persistence
    public ServiceOmnibus(persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void RegistrarOmnibus(Omnibus omnibus) {
        if (omnibus != null && omnibus.getCapacidad() > 0) {
            boolean isSaved = persistence.saveOmnibus(omnibus);
            if (isSaved) {
                System.out.println("✅ Omnibus registrado y persistido correctamente.");
            } else {
                System.out.println("❌ Error al registrar omnibus.");
            }
        } else {
            System.out.println("❌ Datos de omnibus inválidos.");
        }
    }
}
