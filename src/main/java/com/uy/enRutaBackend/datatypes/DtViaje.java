package com.uy.enRutaBackend.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtViaje {

	private int id_viaje;
	private String fecha_partida;
	private String hora_partida;
	private String fecha_llegada;
	private String hora_llegada;
	private int cantidad; /*Usado para el calculo de costos indica la cantidad de asientos vendidos en una venta para un viaje */
	private double precio_viaje;
	private String estado;
	private DtLocalidad localidadOrigen;
	private DtLocalidad localidadDestino;
	private DtOmnibus omnibus;
	private int asientosDisponibles;
	
	public DtViaje() {}
		
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

	public DtViaje(int id_viaje, String fecha_partida, String hora_partida, String fecha_llegada, String hora_llegada,
			double precio_viaje, String estado, DtLocalidad localidadOrigen, DtLocalidad localidadDestino,
			DtOmnibus omnibus) {
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
		this.asientosDisponibles = omnibus.getCapacidad();
	}


	@Override
	public String toString() {
		return "DtViaje [id_viaje=" + id_viaje + ", fecha_partida=" + fecha_partida + ", hora_partida=" + hora_partida
				+ ", fecha_llegada=" + fecha_llegada + ", hora_llegada=" + hora_llegada + ", precio_viaje="
				+ precio_viaje + ", estado=" + estado + ", localidadOrigen=" + localidadOrigen.toString() + ", localidadDestino="
				+ localidadDestino.toString() + ", omnibus=" + omnibus.toString() + ", asientosDisponibles=" + asientosDisponibles + "]";
	}

	public int getId_viaje() {
		return id_viaje;
	}

	public void setId_viaje(int id_viaje) {
		this.id_viaje = id_viaje;
	}

	public String getFecha_partida() {
		return fecha_partida;
	}

	public void setFecha_partida(String fecha_partida) {
		this.fecha_partida = fecha_partida;
	}

	public String getHora_partida() {
		return hora_partida;
	}

	public void setHora_partida(String hora_partida) {
		this.hora_partida = hora_partida;
	}

	public String getFecha_llegada() {
		return fecha_llegada;
	}

	public void setFecha_llegada(String fecha_llegada) {
		this.fecha_llegada = fecha_llegada;
	}

	public String getHora_llegada() {
		return hora_llegada;
	}

	public void setHora_llegada(String hora_llegada) {
		this.hora_llegada = hora_llegada;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio_viaje() {
		return precio_viaje;
	}

	public void setPrecio_viaje(double precio_viaje) {
		this.precio_viaje = precio_viaje;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DtLocalidad getLocalidadOrigen() {
		return localidadOrigen;
	}

	public void setLocalidadOrigen(DtLocalidad localidadOrigen) {
		this.localidadOrigen = localidadOrigen;
	}

	public DtLocalidad getLocalidadDestino() {
		return localidadDestino;
	}

	public void setLocalidadDestino(DtLocalidad localidadDestino) {
		this.localidadDestino = localidadDestino;
	}

	public DtOmnibus getOmnibus() {
		return omnibus;
	}

	public void setOmnibus(DtOmnibus omnibus) {
		this.omnibus = omnibus;
	}

	public int getAsientosDisponibles() {
		return asientosDisponibles;
	}

	public void setAsientosDisponibles(int asientosDisponibles) {
		this.asientosDisponibles = asientosDisponibles;
	}
}


