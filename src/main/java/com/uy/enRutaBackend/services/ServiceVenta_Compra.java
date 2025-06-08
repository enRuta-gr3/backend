package com.uy.enRutaBackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtAsiento;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Descuento;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.entities.EstadoTransaccion;
import com.uy.enRutaBackend.entities.EstadoVenta;
import com.uy.enRutaBackend.entities.Pago;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;
import com.uy.enRutaBackend.icontrollers.IServicePago;
import com.uy.enRutaBackend.icontrollers.IServicePasaje;
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
    private final IServiceAsiento serviceAsiento;
    private final IServicePasaje servicePasaje;

    @Autowired
    public ServiceVenta_Compra(ClienteRepository repositoryCliente, DescuentoRepository repositoryDescuento, 
    		ViajeRepository viajeRepository, VentaCompraRepository repositoryVentaCompra, IServicePago servicePago, 
    		VendedorRepository repositoryVendedor, IServiceAsiento serviceAsiento, IServicePasaje servicePasaje) {
        this.repositoryCliente = repositoryCliente;
        this.repositoryDescuento = repositoryDescuento;
        this.viajeRepository = viajeRepository;
        this.repositoryVentaCompra = repositoryVentaCompra;
        this.servicePago = servicePago;
        this.repositoryVendedor = repositoryVendedor;
        this.serviceAsiento = serviceAsiento;
        this.servicePasaje = servicePasaje;
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

	@Override
	public ResultadoOperacion<?> finalizarVenta(DtVenta_Compra compra) {
		List<DtPasaje> pasajes = new ArrayList<DtPasaje>();
		try {
			Venta_Compra venta = repositoryVentaCompra.findById(compra.getId_venta()).get();
			if (compra.getPago().getEstado_trx().equals(EstadoTransaccion.APROBADA)) {
				List<DisAsiento_Viaje> asientos = cambiarEstadoAsientos(venta.getCliente().getUuidAuth(), EstadoAsiento.OCUPADO);
				Pago pago = actualizarPago(venta, EstadoTransaccion.APROBADA);
				actualizarVenta(venta, EstadoVenta.COMPLETADA, pago);
				pasajes = crearPasajes(asientos, venta);
				return new ResultadoOperacion(true, "Pasajes creados correctamente", pasajes);
			} else {
				List<DisAsiento_Viaje> asientos = cambiarEstadoAsientos(venta.getCliente().getUuidAuth(), EstadoAsiento.LIBRE);
				Pago pago = actualizarPago(venta, compra.getPago().getEstado_trx());
				actualizarVenta(venta, EstadoVenta.CANCELADA, pago);
				DtVenta_Compra dtVenta = new DtVenta_Compra();
				dtVenta.setId_venta(venta.getId_venta());
				dtVenta.setEstado(venta.getEstado());
				return new ResultadoOperacion(true, "Estado de venta y pago correctamente cambiado", dtVenta);
			}
		} catch (Exception e) {
			return new ResultadoOperacion(false, e.getMessage(), ErrorCode.ERROR_LISTADO);
		}
	}
	

	private Pago actualizarPago(Venta_Compra venta, EstadoTransaccion estado) {
		Pago pago = venta.getPago();
		pago.setEstado_trx(estado);
		servicePago.actualizarPago(pago);
		return pago;
	}

	private List<DisAsiento_Viaje> cambiarEstadoAsientos(UUID uuidAuth, EstadoAsiento estado) throws Exception {
		try {
			return serviceAsiento.cambiarEstadoPorVenta(uuidAuth, estado);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void actualizarVenta(Venta_Compra venta, EstadoVenta estadoVenta, Pago pago) {
		venta.setEstado(estadoVenta);
		venta.setPago(pago);
		repositoryVentaCompra.save(venta);
	}
	
	private List<DtPasaje> crearPasajes(List<DisAsiento_Viaje> asientos, Venta_Compra venta) {
		List<DtPasaje> pasajes = new ArrayList<DtPasaje>();
		List<DtAsiento> asientosOmnibus1 = new ArrayList<DtAsiento>();
		List<DtAsiento> asientosOmnibus2 = new ArrayList<DtAsiento>();
		List<DtViaje> viajes1 = new ArrayList<DtViaje>();
		List<DtViaje> viajes2 = new ArrayList<DtViaje>();
		List<DtOmnibus> omnibus = new ArrayList<DtOmnibus>();
		for(DisAsiento_Viaje asiento : asientos) {
			if(viajes1.isEmpty() ) {
				DtViaje viaje = new DtViaje();
				viaje.setId_viaje(asiento.getViaje().getId_viaje());
				viajes1.add(viaje);
			} else if(viajes1.get(0).getId_viaje() != asiento.getViaje().getId_viaje() 
					&& viajes2.isEmpty()){
				DtViaje viaje = new DtViaje();
				viaje.setId_viaje(asiento.getViaje().getId_viaje());
				viajes2.add(viaje);
			}
		}
		
		for(DisAsiento_Viaje asiento : asientos) {
			for(DtViaje viaje1 : viajes1) {
				if(viaje1.getId_viaje() == asiento.getViaje().getId_viaje()) {
					DtAsiento asientoDt = new DtAsiento();
					asientoDt.setId_asiento(asiento.getAsiento().getId_asiento());
					asientosOmnibus1.add(asientoDt);
				}
			}
			
			if(asientos.size() > 1 && !viajes2.isEmpty()) {
				for (DtViaje viaje2 : viajes2) {
					if (viaje2.getId_viaje() == asiento.getViaje().getId_viaje()) {
						DtAsiento asientoDt = new DtAsiento();
						asientoDt.setId_asiento(asiento.getAsiento().getId_asiento());
						asientosOmnibus2.add(asientoDt);
					}
				}
			}
		}
		
		DtOmnibus omnibusDt1 = new DtOmnibus();
		omnibusDt1.setViajes(viajes1);
		omnibusDt1.setAsientos(asientosOmnibus1);
		
		omnibus.add(omnibusDt1);
		if(!viajes2.isEmpty()) {
		DtOmnibus omnibusDt2 = new DtOmnibus();
		omnibusDt2.setViajes(viajes2);
		omnibusDt2.setAsientos(asientosOmnibus2);
		
		omnibus.add(omnibusDt2);
		}
		List<Pasaje> pasajesCreados = servicePasaje.CrearPasajes(omnibus, venta);
		
		for(Pasaje pasaje : pasajesCreados) {
			DtPasaje pasajeDt = servicePasaje.entityToDt(pasaje);
			pasajes.add(pasajeDt);
		}
		
		return pasajes;
	}


}