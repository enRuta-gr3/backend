package com.uy.enRutaBackend.datatypes;

import java.util.List;

import com.uy.enRutaBackend.entities.Venta_Compra;


public class DtVendedor extends DtUsuario{

    private List<Venta_Compra> ventas;

	public DtVendedor() {}
	
	public List<Venta_Compra> getVentas() {
		return ventas;
	}

	public void setVentas(List<Venta_Compra> ventas) {
		this.ventas = ventas;
	}
    

}

