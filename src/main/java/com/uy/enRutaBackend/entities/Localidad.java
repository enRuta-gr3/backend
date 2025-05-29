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
    
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private Departamento id_departamento;
    
    @OneToMany(mappedBy = "id_viaje")
    private List<Viaje> viajes;    
        
    public Localidad() {}
    
    public Localidad(String nombre) {
        this.nombre = nombre;
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
}