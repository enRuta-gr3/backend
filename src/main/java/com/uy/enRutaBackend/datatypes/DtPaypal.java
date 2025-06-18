package com.uy.enRutaBackend.datatypes;

public class DtPaypal {

	private int id_venta;
	private String urlPago;
	private String id_orden;
	
	public DtPaypal() {
	}
	
	public DtPaypal(int id_venta, String urlPago, String id_orden) {
		this.id_venta = id_venta;
		this.urlPago = urlPago;
		this.id_orden = id_orden;
	}
	

	public int getId_venta() {
		return id_venta;
	}
	public void setId_venta(int id_venta) {
		this.id_venta = id_venta;
	}

	public String getUrlPago() {
		return urlPago;
	}

	public void setUrlPago(String urlPago) {
		this.urlPago = urlPago;
	}

	public String getId_orden() {
		return id_orden;
	}

	public void setId_orden(String id_orden) {
		this.id_orden = id_orden;
	}

}
