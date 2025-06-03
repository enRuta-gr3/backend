package com.uy.enRutaBackend.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtLocalidad {
	private int id_localidad;
	private String nombreLocalidad;
	private DtDepartamento departamento;
	
	public DtLocalidad() {}
	
	public DtLocalidad(int id_localidad, String nombreLocalidad, DtDepartamento departamento) {
		super();
		this.id_localidad = id_localidad;
		this.nombreLocalidad = nombreLocalidad;
		this.departamento = departamento;
	}

	public DtLocalidad(String nombreLocalidad, DtDepartamento departamento) {
		this.nombreLocalidad = nombreLocalidad;
		this.departamento = departamento;
	}
	
	@Override
	public String toString() {
		return "DtLocalidad [id_localidad=" + id_localidad + ", nombreLocalidad=" + nombreLocalidad + ", departamento="
				+ departamento + "]";
	}

	public int getId_localidad() {
		return id_localidad;
	}

	public void setId_localidad(int id_localidad) {
		this.id_localidad = id_localidad;
	}

	public String getNombreLocalidad() {
		return nombreLocalidad;
	}

	public void setNombreLocalidad(String nombreLocalidad) {
		this.nombreLocalidad = nombreLocalidad;
	}

	public DtDepartamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DtDepartamento departamento) {
		this.departamento = departamento;
	}	
	
	
}
