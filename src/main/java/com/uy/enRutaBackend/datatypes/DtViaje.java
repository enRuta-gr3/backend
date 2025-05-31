package com.uy.enRutaBackend.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
	
	public DtViaje() {}
	
	public DtViaje(int id_viaje, String fecha_partida, String hora_partida, String fecha_llegada, String hora_llegada,
			double precio_viaje, String estado, DtLocalidad localidadOrigen, DtLocalidad localidadDestino,
			DtOmnibus omnibus, int asientosDisponibles) {
		this.id_viaje = id_viaje;
		this.fecha_partida = fecha_partida;
		this.hora_partida = hora_partida;
		this.fecha_llegada = fecha_llegada;
		this.hora_llegada = hora_llegada;
		this.precio_viaje = precio_viaje;
		this.estado = estado;
		this.localidadOrigen = localidadOrigen;
		this.localidadDestino = localidadDestino;
		this.omnibus = omnibus;
		this.asientosDisponibles = asientosDisponibles;
	}
	
	public DtViaje(String fecha_partida, String hora_partida, String fecha_llegada, String hora_llegada,
			double precio_viaje, String estado, DtLocalidad localidadOrigen, DtLocalidad localidadDestino,
			DtOmnibus omnibus) {
		this.fecha_partida = fecha_partida;
		this.hora_partida = hora_partida;
		this.fecha_llegada = fecha_llegada;
		this.hora_llegada = hora_llegada;
		this.precio_viaje = precio_viaje;
		this.estado = estado;
		this.localidadOrigen = localidadOrigen;
		this.localidadDestino = localidadDestino;
		this.omnibus = omnibus;
		this.asientosDisponibles = omnibus.getCapacidad();
	}



	@Override
	public String toString() {
		return "DtViaje [id_viaje=" + id_viaje + ", fecha_partida=" + fecha_partida + ", hora_partida=" + hora_partida
				+ ", fecha_llegada=" + fecha_llegada + ", hora_llegada=" + hora_llegada + ", precio_viaje="
				+ precio_viaje + ", estado=" + estado + ", localidadOrigen=" + localidadOrigen.toString() + ", localidadDestino="
				+ localidadDestino.toString() + ", omnibus=" + omnibus.toString() + ", asientosDisponibles=" + asientosDisponibles + "]";
	}
}
