package com.uy.enRutaBackend.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uy.enRutaBackend.entities.EstadoAsiento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtDisAsiento {	
	private int id_disAsiento;
	private DtAsiento asiento;
	private DtViaje viaje;
	private EstadoAsiento estado;
	private String idBloqueo;
	
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
	
	
}
