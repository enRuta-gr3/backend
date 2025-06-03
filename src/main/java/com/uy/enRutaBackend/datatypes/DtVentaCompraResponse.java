package com.uy.enRutaBackend.datatypes;

public class DtVentaCompraResponse {
    private double montoTotal;
    private double montoDescuento;
    private String tipoDescuento;
    
    public DtVentaCompraResponse(double montoTotal, double montoDescuento, String tipoDescuento) {
        this.montoTotal = montoTotal;
        this.montoDescuento = montoDescuento;
        this.tipoDescuento = tipoDescuento;
    }
    
	public double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}
	public double getMontoDescuento() {
		return montoDescuento;
	}
	public void setMontoDescuento(double montoDescuento) {
		this.montoDescuento = montoDescuento;
	}
	public String getTipoDescuento() {
		return tipoDescuento;
	}
	public void setTipoDescuento(String tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}

}
