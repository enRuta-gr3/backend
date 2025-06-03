package com.uy.enRutaBackend.datatypes;

import java.util.List;

import com.uy.enRutaBackend.entities.EstadoVenta;


public class DtVenta_Compra {


    private int id_venta;

    private DtVendedor vendedor;
    
    private DtCliente cliente;
    
    private EstadoVenta estado;
    
    private DtDescuento descuento;
    
    private DtPago pago;
    
    private List<DtPasaje> pasajes;

	public int getId_venta() {
		return id_venta;
	}

	public void setId_venta(int id_venta) {
		this.id_venta = id_venta;
	}

	public DtVendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(DtVendedor vendedor) {
		this.vendedor = vendedor;
	}

	public DtCliente getCliente() {
		return cliente;
	}

	public void setCliente(DtCliente cliente) {
		this.cliente = cliente;
	}

	public EstadoVenta getEstado() {
		return estado;
	}

	public void setEstado(EstadoVenta estado) {
		this.estado = estado;
	}

	public DtDescuento getDescuento() {
		return descuento;
	}

	public void setDescuento(DtDescuento descuento) {
		this.descuento = descuento;
	}

	public DtPago getPago() {
		return pago;
	}

	public void setPago(DtPago pago) {
		this.pago = pago;
	}

	public List<DtPasaje> getPasajes() {
		return pasajes;
	}

	public void setPasajes(List<DtPasaje> pasajes) {
		this.pasajes = pasajes;
	}
    
    
}