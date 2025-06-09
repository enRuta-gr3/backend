package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.datatypes.DtAsiento;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.icontrollers.IServicePasaje;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.persistence.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ServicePasaje implements IServicePasaje {

    private final persistence persistence;
    private final AsientoRepository asientoRepository;
    private final ViajeRepository viajeRepository;

    public ServicePasaje(persistence persistence,
                         AsientoRepository asientoRepository,
                         ViajeRepository viajeRepository) {
        this.persistence = persistence;
        this.asientoRepository = asientoRepository;
        this.viajeRepository = viajeRepository;
    }
    
    
    public void CrearPasajes(List<DtOmnibus> omnibusDTOs, Venta_Compra venta_compra) {
        List<Pasaje> listaPasajes = new ArrayList<>();

        for (DtOmnibus dtoOmnibus : omnibusDTOs) {
            int idViaje = dtoOmnibus.getViajes().get(0).getId_viaje();
            Viaje viaje = viajeRepository.findById(idViaje).orElseThrow();
            double monto = viaje.getPrecio_viaje();

            for (DtAsiento asientoDTO : dtoOmnibus.getAsientos()) {
                int idAsiento = asientoDTO.getId_asiento();
                Asiento asiento = asientoRepository.findById(idAsiento).orElseThrow();

                if (asiento.getOmnibus().getId_omnibus() != viaje.getOmnibus().getId_omnibus()) {
                    throw new IllegalArgumentException("El asiento no pertenece al ómnibus del viaje.");
                }

                Pasaje pasaje = new Pasaje(monto, viaje, asiento, venta_compra);
                listaPasajes.add(pasaje);
            }
        }

        RegistrarPasajes(listaPasajes);
    }


    @Override
    public void RegistrarPasajes(List<Pasaje> pasajes) {
        for (Pasaje pasaje : pasajes) {
            boolean isSaved = persistence.savePasaje(pasaje);
            if (isSaved) {
                System.out.println("✅ Pasaje registrado: " + pasaje);
            } else {
                System.out.println("❌ Error al registrar pasaje: " + pasaje);
            }
        }
    }
}
