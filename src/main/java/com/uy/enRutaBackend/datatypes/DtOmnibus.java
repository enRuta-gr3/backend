package com.uy.enRutaBackend.datatypes;

import java.sql.Date;

import com.uy.enRutaBackend.entities.Localidad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtOmnibus {
	private int id_omnibus;
	private int capacidad;
	private int nro_coche;
	private Localidad localidad_actual;
	private boolean activo;
	private Date fecha_fin;
}
