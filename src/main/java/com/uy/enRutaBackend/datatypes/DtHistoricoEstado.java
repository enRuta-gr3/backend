package com.uy.enRutaBackend.datatypes;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtHistoricoEstado {

    private int id_his_estado;
    private Date fecha_inicio;
    private Date fecha_fin;
    private boolean activo;
    private int id_omnibus;
    private UUID vendedor;

    public DtHistoricoEstado() {}

    public DtHistoricoEstado(int id_his_estado, Date fecha_inicio, Date fecha_fin, boolean activo, int id_omnibus, UUID vendedor) {
        this.id_his_estado = id_his_estado;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.activo = activo;
        this.id_omnibus = id_omnibus;
        this.vendedor = vendedor;
    }

    public int getId_his_estado() {
        return id_his_estado;
    }

    public void setId_his_estado(int id_his_estado) {
        this.id_his_estado = id_his_estado;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getId_omnibus() {
        return id_omnibus;
    }

    public void setId_omnibus(int id_omnibus) {
        this.id_omnibus = id_omnibus;
    }

	public UUID getVendedor() {
		return vendedor;
	}

	public void setVendedor(UUID vendedor) {
		this.vendedor = vendedor;
	}


}