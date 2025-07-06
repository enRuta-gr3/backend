package com.uy.enRutaBackend.datatypes;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DtOmnibus {

	private int id_omnibus;
    private int capacidad;
    private int nro_coche;
    private boolean activo;
    private Date fecha_fin;
    private int id_localidad_actual;

    private List<DtAsiento> asientos;
    private List<DtViaje> viajes;
    private List<DtHistoricoEstado> historico_estado;

    public DtOmnibus() {}

    public DtOmnibus(int id_omnibus, int capacidad, int nro_coche, boolean activo, Date fecha_fin,
			int id_localidad_actual, List<DtAsiento> asientos, List<DtViaje> viajes,
			List<DtHistoricoEstado> historico_estado) {
		super();
		this.id_omnibus = id_omnibus;
		this.capacidad = capacidad;
		this.nro_coche = nro_coche;
		this.activo = activo;
		this.fecha_fin = fecha_fin;
		this.id_localidad_actual = id_localidad_actual;
		this.asientos = asientos;
		this.viajes = viajes;
		this.historico_estado = historico_estado;
	}

	public DtOmnibus(int capacidad, int nro_coche, boolean activo, Date fecha_fin, int id_localidad_actual,
                     List<DtAsiento> asientos, List<DtViaje> viajes, List<DtHistoricoEstado> historico_estado) {
        this.capacidad = capacidad;
        this.nro_coche = nro_coche;
        this.activo = activo;
        this.fecha_fin = fecha_fin;
        this.id_localidad_actual = id_localidad_actual;
        this.asientos = asientos;
        this.viajes = viajes;
        this.historico_estado = historico_estado;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getNro_coche() {
        return nro_coche;
    }

    public void setNro_coche(int nro_coche) {
        this.nro_coche = nro_coche;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getId_localidad_actual() {
        return id_localidad_actual;
    }

    public void setId_localidad_actual(int id_localidad_actual) {
        this.id_localidad_actual = id_localidad_actual;
    }

    public List<DtAsiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<DtAsiento> asientos) {
        this.asientos = asientos;
    }

    public List<DtViaje> getViajes() {
        return viajes;
    }

    public void setViajes(List<DtViaje> viajes) {
        this.viajes = viajes;
    }

    public List<DtHistoricoEstado> getHistorico_estado() {
        return historico_estado;
    }

    public void setHistorico_estado(List<DtHistoricoEstado> historico_estado) {
        this.historico_estado = historico_estado;
    }

	public int getId_omnibus() {
		return id_omnibus;
	}

	public void setId_omnibus(int id_omnibus) {
		this.id_omnibus = id_omnibus;
	}
}