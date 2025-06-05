package com.uy.enRutaBackend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Descuento;
import com.uy.enRutaBackend.entities.EstadoVenta;
import com.uy.enRutaBackend.entities.Medio_de_Pago;
import com.uy.enRutaBackend.entities.Pago;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.icontrollers.IServicePago;
import com.uy.enRutaBackend.icontrollers.IServiceVenta_Compra;
import com.uy.enRutaBackend.persistence.ClienteRepository;
import com.uy.enRutaBackend.persistence.DescuentoRepository;
import com.uy.enRutaBackend.persistence.VendedorRepository;
import com.uy.enRutaBackend.persistence.VentaCompraRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;

@Service
public class ServiceVenta_Compra implements IServiceVenta_Compra {

    private final ClienteRepository repositoryCliente;
    private final DescuentoRepository repositoryDescuento;
    private final ViajeRepository viajeRepository;
    private final VentaCompraRepository repositoryVentaCompra;
    private final IServicePago servicePago;
    private final VendedorRepository repositoryVendedor;

    @Autowired
    public ServiceVenta_Compra(ClienteRepository repositoryCliente, DescuentoRepository repositoryDescuento, ViajeRepository viajeRepository, VentaCompraRepository repositoryVentaCompra, IServicePago servicePago, VendedorRepository repositoryVendedor) {
        this.repositoryCliente = repositoryCliente;
        this.repositoryDescuento = repositoryDescuento;
        this.viajeRepository = viajeRepository;
        this.repositoryVentaCompra = repositoryVentaCompra;
        this.servicePago = servicePago;
        this.repositoryVendedor = repositoryVendedor;
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
    
    
    public Venta_Compra armarVenta(DtVenta_Compra compra) {
		try {
			DtVentaCompraResponse venta = calcularVenta(compra.getPasajes());
			Pago pago = servicePago.crearPago((venta.getMontoTotal()-venta.getMontoDescuento()), compra.getPago().getMedio_de_pago().getId_medio_de_pago());
			Cliente cliente = repositoryCliente.findByUuidAuth(compra.getPasajes().get(0).getUuidAuth());
			if(compra.getVendedor() != null) {
				Vendedor vendedor = repositoryVendedor.findById(compra.getVendedor().getUuidAuth()).get();
				return crearVenta(vendedor, cliente, pago, venta.getTipoDescuento());
			}
			return crearVenta(null, cliente, pago, venta.getTipoDescuento());
		}catch(Exception e) {
			throw e;
		}
	}
    

    public Venta_Compra crearVenta(Vendedor vendedor, Cliente cliente, Pago pago, String tipoDescuento) {
    	
    	Venta_Compra compra = new Venta_Compra();
    	
    	Descuento desc = repositoryDescuento.findByTipo(tipoDescuento); 
    	 	
    	compra.setVendedor(vendedor);
    	compra.setCliente(cliente);
    	compra.setDescuento(desc);
    	compra.setPago(pago);
    	compra.setEstado(EstadoVenta.EN_PROCESO);
    	
    	return repositoryVentaCompra.save(compra);
    }
    
    public void agregarVendedor(Venta_Compra compra, Vendedor vendedor) {
    	compra.setVendedor(vendedor);    	
    	repositoryVentaCompra.save(compra);
    }
}