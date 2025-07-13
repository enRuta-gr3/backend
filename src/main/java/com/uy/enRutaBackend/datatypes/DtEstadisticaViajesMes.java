package com.uy.enRutaBackend.datatypes;

public class DtEstadisticaViajesMes {
	
	private String mes;
	private Integer cantidad;
	private String anio;
	
	public DtEstadisticaViajesMes() {
	}

	public DtEstadisticaViajesMes(Integer mes, Long cantidad, Integer anio) {
	    this.anio = String.valueOf(anio);
	    this.mes = (mes < 10 ? "0" : "") + mes;
	    this.cantidad = cantidad.intValue();
	}

	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
}
