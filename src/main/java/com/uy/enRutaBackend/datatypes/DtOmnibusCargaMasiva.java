package com.uy.enRutaBackend.datatypes;

public class DtOmnibusCargaMasiva {
	
	private int capacidad;
	private int nroCoche;
	private boolean activo;
	private String fechaFin;
	private String nombreLocalidad;
	private String nombreDepartamento;
	
	
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public int getNroCoche() {
		return nroCoche;
	}
	public void setNroCoche(int nroCoche) {
		this.nroCoche = nroCoche;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getNombreLocalidad() {
		return nombreLocalidad;
	}
	public void setNombreLocalidad(String nombreLocalidad) {
		this.nombreLocalidad = nombreLocalidad;
	}
	public String getNombreDepartamento() {
		return nombreDepartamento;
	}
	public void setNombreDepartamento(String nombreDepartamento) {
		this.nombreDepartamento = nombreDepartamento;
	}
}
