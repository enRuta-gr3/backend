package com.uy.enRutaBackend.datatypes;

public class DtPorcentajeOmnibusAsignados {
	
	private int totalOmnibus;
	private int asignados;
	private int noAsignados;
	private int porcentajeAsignados;
	private int porcentajeNoAsignados;
	
	public int getTotalOmnibus() {
		return totalOmnibus;
	}
	public void setTotalOmnibus(int totalOmnibus) {
		this.totalOmnibus = totalOmnibus;
	}
	public int getAsignados() {
		return asignados;
	}
	public void setAsignados(int asignados) {
		this.asignados = asignados;
	}
	public int getNoAsignados() {
		return noAsignados;
	}
	public void setNoAsignados(int noAsignados) {
		this.noAsignados = noAsignados;
	}
	public int getPorcentajeAsignados() {
		return porcentajeAsignados;
	}
	public void setPorcentajeAsignados(int porcentajeAsignados) {
		this.porcentajeAsignados = porcentajeAsignados;
	}
	public int getPorcentajeNoAsignados() {
		return porcentajeNoAsignados;
	}
	public void setPorcentajeNoAsignados(int porcentajeNoAsignados) {
		this.porcentajeNoAsignados = porcentajeNoAsignados;
	}
}
