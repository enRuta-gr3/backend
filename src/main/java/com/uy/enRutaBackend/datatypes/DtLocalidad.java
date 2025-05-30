package com.uy.enRutaBackend.datatypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	
	
}
