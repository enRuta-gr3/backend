package com.uy.enRutaBackend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Descuento;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.icontrollers.IServiceVenta_Compra;
import com.uy.enRutaBackend.persistence.ClienteRepository;
import com.uy.enRutaBackend.persistence.DescuentoRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;

@Service
public class ServiceVenta_Compra implements IServiceVenta_Compra {

    private final ClienteRepository repositoryCliente;
    private final DescuentoRepository repositoryDescuento;
    private final ViajeRepository viajeRepository;

    public ServiceVenta_Compra(ClienteRepository repositoryCliente, DescuentoRepository repositoryDescuento, ViajeRepository viajeRepository) {
        this.repositoryCliente = repositoryCliente;
        this.repositoryDescuento = repositoryDescuento;
        this.viajeRepository = viajeRepository;
    }

    @Override
    public DtVentaCompraResponse calcularVenta(List<DtPasaje> pasajes) {

        if (pasajes == null || pasajes.isEmpty()) {
            return new DtVentaCompraResponse(0, 0, "");
        }

        UUID uuid = pasajes.get(0).getUuidAuth();
        Cliente cliente = repositoryCliente.findByUuidAuth(uuid);

        String tipoDescuento = "";
        double porcentajeDescuento = 0.0;

        if (cliente != null && cliente.isEstado_descuento()) {
            if (cliente.isEsJubilado()) {
                Descuento descuento = repositoryDescuento.findById(1).orElse(null);
                tipoDescuento = "Jubilado";
                porcentajeDescuento = descuento.getPorcentaje_descuento();
            } else if (cliente.isEsEstudiante()) {
                Descuento descuento = repositoryDescuento.findById(2).orElse(null);
                tipoDescuento = "Estudiante";
                porcentajeDescuento = descuento.getPorcentaje_descuento();
            }
        }

        double precioTotal = 0;
        double montoDescuentoTotal = 0;

        for (DtPasaje pasaje : pasajes) {
            int cantidad = pasaje.getViaje().getCantidad();
            int idViaje = pasaje.getViaje().getId_viaje();
            Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado: " + idViaje));

            double precioUnitario = viaje.getPrecio_viaje();


            for (int i = 0; i < cantidad; i++) {
                precioTotal += precioUnitario;
                montoDescuentoTotal += precioUnitario * porcentajeDescuento;
            }
        }

        return new DtVentaCompraResponse(precioTotal, montoDescuentoTotal, tipoDescuento);
    }
}

