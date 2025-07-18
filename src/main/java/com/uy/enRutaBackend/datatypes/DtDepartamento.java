package com.uy.enRutaBackend.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtDepartamento {
	private int id_departamento;
	private String nombreDepartamento;
	
	public DtDepartamento() {}
	
	public DtDepartamento(int id_departamento, String nombreDepartamento) {
		this.id_departamento = id_departamento;
		this.nombreDepartamento = nombreDepartamento;
	}


	@Override
	public String toString() {
		return "DtDepartamento [id_departamento=" + id_departamento + ", nombreDepartamento=" + nombreDepartamento
				+ "]";
	}
	
	public int getId_departamento() {
		return id_departamento;
	}

	public void setId_departamento(int id_departamento) {
		this.id_departamento = id_departamento;
	}

	public String getNombreDepartamento() {
		return nombreDepartamento;
	}

	public void setNombreDepartamento(String nombreDepartamento) {
		this.nombreDepartamento = nombreDepartamento;
	}


	
	
}
