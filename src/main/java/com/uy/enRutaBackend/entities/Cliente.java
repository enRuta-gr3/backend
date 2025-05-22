package com.uy.enRutaBackend.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {
	
	@OneToMany(mappedBy = "id_venta")
    private List<Venta_Compra> compras;
	
	@OneToMany(mappedBy = "id_pasaje")
    private List<Pasaje> pasajesComprados;
	
    @Column(name = "esEstudiante")
    private boolean esEstudiante;
    
    @Column(name = "esJubilado")
    private boolean esJubilado;
	
	public Cliente() {}

    public Cliente(String ci, String nombres, String apellidos, String email, String contraseña, Date fecha_nacimiento, boolean eliminado, Date ultimo_inicio_sesion, Date fecha_creacion, boolean estado_descuento, boolean esEstudiante, boolean esJubilado) {
    	super(ci, nombres, apellidos, email, contraseña, fecha_nacimiento, eliminado, ultimo_inicio_sesion, fecha_creacion);
    	this.esEstudiante = esEstudiante;
    	this.esJubilado = esJubilado;
    }
    
    
    public List<Pasaje> getPasajesComprados() {
        return pasajesComprados;
    }

    public void setPasajesComprados(List<Pasaje> pasajesComprados) {
        this.pasajesComprados = pasajesComprados;
    }

    public boolean isEsEstudiante() {
        return esEstudiante;
    }

    public void setEsEstudiante(boolean esEstudiante) {
        this.esEstudiante = esEstudiante;
    }

    public boolean isEsJubilado() {
        return esJubilado;
    }

    public void setEsJubilado(boolean esJubilado) {
        this.esJubilado = esJubilado;
    }

}