package com.uy.enRutaBackend.datatypes;

public class DtOmnibusPorEstadoPorMes {

	private String mes;
	private String anio;
	private String cantidadActivos;
	private String cantidadInactivos;
	
	public DtOmnibusPorEstadoPorMes() {
	}

	public DtOmnibusPorEstadoPorMes(String mes, Long cantidadActivos, Long cantidadInactivos) {
		this.mes = mes;
		this.anio = String.valueOf(java.time.Year.now().getValue());
		this.cantidadActivos = String.valueOf(cantidadActivos);
		this.cantidadInactivos = String.valueOf(cantidadInactivos);
	}
	
	
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getCantidadActivos() {
		return cantidadActivos;
	}
	public void setCantidadActivos(String cantidadActivos) {
		this.cantidadActivos = cantidadActivos;
	}
	public String getCantidadInactivos() {
		return cantidadInactivos;
	}
	public void setCantidadInactivos(String cantidadInactivos) {
		this.cantidadInactivos = cantidadInactivos;
	}

}
