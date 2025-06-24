package com.uy.enRutaBackend.datatypes;

public class DtOmnibusPorEstadoPorMes {

	private String idOmnibus;
	private String mes;
	private String anio;
	private String cantidadActivos;
	private String cantidadInactivos;
	
	
	public String getIdOmnibus() {
		return idOmnibus;
	}
	public void setIdOmnibus(String idOmnibus) {
		this.idOmnibus = idOmnibus;
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
