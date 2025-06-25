package com.uy.enRutaBackend.entities;



import java.util.Date;
import java.util.List;

import com.uy.enRutaBackend.datatypes.DtLocalidad;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Omnibus")
public class Omnibus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_omnibus")
    private int id_omnibus;

    @Column(name = "capacidad")
    private int capacidad;
    
    @Column(name = "nro_coche")
    private int nroCoche;
    
    @ManyToOne
    private Localidad localidad_actual;
    
    @Column(name = "activo")
    private boolean activo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_fin")
    private Date fecha_fin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    
    @OneToMany(mappedBy = "id_asiento", cascade = CascadeType.REMOVE, orphanRemoval = true)    
    private List<Asiento> asientos;

    @OneToMany(mappedBy = "id_viaje")
    private List<Viaje> viajes;
    
    @OneToMany(mappedBy = "id_his_estado")
    private List<Historico_estado> historico_estado;


    public Omnibus() {}

    public Omnibus(int capacidad, int nro_coche, boolean activo, Date fecha_fin, Localidad localidad_actual) {
        this.capacidad = capacidad;
        this.nroCoche = nro_coche;
        this.activo = activo;
        this.fecha_fin = fecha_fin;
        this.localidad_actual = localidad_actual;
        this.fechaCreacion = new Date();
    }

    public Omnibus(int i, int j, boolean b, Date valueOf, DtLocalidad loc1) {
		// TODO Auto-generated constructor stub
	}

	public int getId_omnibus() {
        return id_omnibus;
    }

    public void setId_omnibus(int id_omnibus) {
        this.id_omnibus = id_omnibus;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getNro_coche() {
        return nroCoche;
    }

    public void setNro_coche(int nro_coche) {
        this.nroCoche = nro_coche;
    }

    public Localidad getLocalidad_actual() {
        return localidad_actual;
    }

    public void setLocalidad_actual(Localidad localidad_actual) {
        this.localidad_actual = localidad_actual;
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

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    public List<Viaje> getViajes() {
        return viajes;
    }

    public void setViajes(List<Viaje> viajes) {
        this.viajes = viajes;
    }

    public List<Historico_estado> getHistorico_estado() {
        return historico_estado;
    }

    public void setHistorico_estado(List<Historico_estado> historico_estado) {
        this.historico_estado = historico_estado;
    }
}