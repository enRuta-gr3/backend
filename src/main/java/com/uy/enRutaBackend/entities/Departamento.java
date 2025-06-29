package com.uy.enRutaBackend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Departamento")
public class Departamento {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
	private int idDepartamento;
    
    @Column(name = "nombre_departamento")
	private String nombre;
    
    @OneToMany(mappedBy = "id_localidad", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Localidad> localidades;



    public Departamento() {}

    public Departamento(String nombre, List<Localidad> localidades) {
        this.nombre = nombre;
        this.localidades = localidades;
    }

    public int getId_departamento() {
        return idDepartamento;
    }

    public void setId_departamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }
}

