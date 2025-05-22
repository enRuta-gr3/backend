package com.uy.enRutaBackend.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Historico_estado")
public class Historico_estado {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_his_estado")	
	private int id_his_estado;
	
    @Column(name = "fecha_inicio")
	private Date fecha_inicio;
	
    @Column(name = "activo")
	private boolean activo;
    
    @Column(name = "fecha_fin")
	private Date fecha_fin;
    
    @ManyToOne
    @JoinColumn(name = "id_omnibus")
    private Omnibus omnibus;
    

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

    public Omnibus getOmnibus() {
        return omnibus;
    }

    public void setOmnibus(Omnibus omnibus) {
        this.omnibus = omnibus;
    }
}

