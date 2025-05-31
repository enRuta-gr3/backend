package com.uy.enRutaBackend.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "Localidad")
public class Localidad {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_localidad;
    
    @Column(name = "nombre")
    private String nombre;
    
    @OneToMany(mappedBy = "id_viaje")
    private List<Viaje> viajes;    
    
    @OneToMany(mappedBy = "id_omnibus")
    private List<Omnibus> Omnibus;
    
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;
        
    public Localidad() {}
    
    public Localidad(String nombre) {
        this.nombre = nombre;
        this.viajes = new ArrayList<>();
    }
    
    public Localidad(String nombre, Departamento departamento) {
        this.nombre = nombre;
        this.departamento = departamento;
        this.viajes = new ArrayList<>();
    }
    
    public int getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(int id_localidad) {
        this.id_localidad = id_localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public List<Viaje> getViajes() {
		return viajes;
	}

	public void setViajes(List<Viaje> viajes) {
		this.viajes = viajes;
	}

	public List<Omnibus> getOmnibus() {
		return Omnibus;
	}

	public void setOmnibus(List<Omnibus> omnibus) {
		Omnibus = omnibus;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
}