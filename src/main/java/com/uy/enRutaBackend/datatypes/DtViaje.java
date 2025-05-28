package com.uy.enRutaBackend.datatypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtViaje {
	private int id_viaje;
	private String fecha_partida;
	private String hora_partida;
	private String fecha_llegada;
	private String hora_llegada;
	private double precio_viaje;
	private String estado;
	private DtLocalidad localidadOrigen;
	private DtLocalidad localidadDestino;
	private DtOmnibus omnibus;
	private int asientosDisponibles;
}
