package com.uy.enRutaBackend.datatypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtLocalidad {
	private int id_localidad;
	private String nombreLocalidad;
	private DtDepartamento departamento;
}
