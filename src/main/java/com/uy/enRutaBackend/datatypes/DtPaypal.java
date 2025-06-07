package com.uy.enRutaBackend.datatypes;

public class DtPaypal {
	private String client_id;
	private String currency;
	private String intent;
	private double monto;
	private double cotizacion;
	private int id_venta;
	
	public DtPaypal() {
	}
	
	public DtPaypal(String client_id, String currency, String intent, double monto,double cotizacion, int id_venta) {
		this.client_id = client_id;
		this.currency = currency;
		this.intent = intent;
		this.monto = monto;
		this.cotizacion = cotizacion;
		this.id_venta = id_venta;
	}
	
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public int getId_venta() {
		return id_venta;
	}
	public void setId_venta(int id_venta) {
		this.id_venta = id_venta;
	}
	public double getCotizacion() {
		return cotizacion;
	}
	public void setCotizacion(double cotizacion) {
		this.cotizacion = cotizacion;
	}
}
