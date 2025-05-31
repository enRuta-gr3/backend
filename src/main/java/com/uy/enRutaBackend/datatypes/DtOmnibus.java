package com.uy.enRutaBackend.datatypes;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtOmnibus {
	private int id_omnibus;
	private int capacidad;
	private int nro_coche;
	private DtLocalidad localidad_actual;
	private boolean activo;
	private Date fecha_fin;
	
	private List<DtAsiento> asientos;
    private List<DtViaje> viajes;
    private List<DtHistoricoEstado> historico_estado;
    
    public DtOmnibus() {}

	public DtOmnibus(int id_omnibus, int capacidad, int nro_coche, DtLocalidad localidad_actual, boolean activo,
			Date fecha_fin, List<DtAsiento> asientos, List<DtViaje> viajes, List<DtHistoricoEstado> historico_estado) {
		super();
		this.id_omnibus = id_omnibus;
		this.capacidad = capacidad;
		this.nro_coche = nro_coche;
		this.localidad_actual = localidad_actual;
		this.activo = activo;
		this.fecha_fin = fecha_fin;
		this.asientos = asientos;
		this.viajes = viajes;
		this.historico_estado = historico_estado;
	}

	@Override
	public String toString() {
		return "DtOmnibus [id_omnibus=" + id_omnibus + ", capacidad=" + capacidad + ", nro_coche=" + nro_coche
				+ ", localidad_actual=" + localidad_actual.toString() + ", activo=" + activo + ", fecha_fin=" + fecha_fin
				+ ", asientos=" + asientos + ", viajes=" + viajes + ", historico_estado=" + historico_estado + "]";
	}
    
    
}


