package com.uy.enRutaBackend.services;

import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;
import com.uy.enRutaBackend.persistence.persistence;

@Service
public class ServiceAsiento implements IServiceAsiento {

    private final persistence persistence;

    public ServiceAsiento(persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public ResultadoOperacion<Asiento> RegistrarAsiento(Asiento asiento) {
//        if (asiento == null || asiento.getNumeroAsiento() <= 0) {
//            return new ResultadoOperacion<>(false, "Datos de asiento inválidos", "DATOS_INVALIDOS");
//        }
//
//        boolean isSaved = persistence.saveAsiento(asiento);
//        if (isSaved) {
//            return new ResultadoOperacion<>(true, "Asiento registrado correctamente", asiento);
//        } else {
//            return new ResultadoOperacion<>(false, "Error al registrar asiento", "ERROR_PERSISTENCIA");
//        }
        return null;
    }
}
