package com.uy.enRutaBackend.datatypes;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uy.enRutaBackend.entities.EstadoAsiento;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtDisAsiento {	
	private int id_disAsiento;
	private DtAsiento asiento;
	private DtViaje viaje;
	private EstadoAsiento estado;
	private String idBloqueo;
	private Date fechaBloqueo;
	
	public DtDisAsiento() {}
	
	public DtDisAsiento(DtAsiento asiento, DtViaje viaje, EstadoAsiento estado, String idBloqueo) {
		this.asiento = asiento;
		this.viaje = viaje;
		this.estado = estado;
		this.idBloqueo = idBloqueo;
	}

	public DtDisAsiento(int id_disAsiento, DtAsiento asiento, DtViaje viaje, EstadoAsiento estado, String idBloqueo) {
		this.id_disAsiento = id_disAsiento;
		this.asiento = asiento;
		this.viaje = viaje;
		this.estado = estado;
		this.idBloqueo = idBloqueo;
	}

	public int getId_disAsiento() {
		return id_disAsiento;
	}

	public void setId_disAsiento(int id_disAsiento) {
		this.id_disAsiento = id_disAsiento;
	}

	public DtAsiento getAsiento() {
		return asiento;
	}

	public void setAsiento(DtAsiento asiento) {
		this.asiento = asiento;
	}

	public DtViaje getViaje() {
		return viaje;
	}

	public void setViaje(DtViaje viaje) {
		this.viaje = viaje;
	}

	public EstadoAsiento getEstado() {
		return estado;
	}

	public void setEstado(EstadoAsiento estado) {
		this.estado = estado;
	}

	public String getIdBloqueo() {
		return idBloqueo;
	}

	public void setIdBloqueo(String idBloqueo) {
		this.idBloqueo = idBloqueo;
	}

	public Date getFechaBloqueo() {
		return fechaBloqueo;
	}

	public void setFechaBloqueo(Date fechaBloqueo) {
		this.fechaBloqueo = fechaBloqueo;
	}
	
}
