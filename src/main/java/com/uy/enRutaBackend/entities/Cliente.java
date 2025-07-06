package com.uy.enRutaBackend.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {
	
	@OneToMany(mappedBy = "id_venta")
    private List<Venta_Compra> compras;
	
    @Column(name = "es_estudiante")
    private boolean esEstudiante;
    
    @Column(name = "es_jubilado")
    private boolean esJubilado;
    
    @Column(name = "estado_descuento")
    private boolean estado_descuento;
    
    @Column(name = "push_token")
    private String pushToken;
	
	public Cliente() {}

    public Cliente(String ci, String nombres, String apellidos, String email, String contraseña, Date fecha_nacimiento, boolean eliminado, Date ultimo_inicio_sesion, Date fecha_creacion, boolean esEstudiante, boolean esJubilado, boolean estado_descuento) {
    	super(ci, nombres, apellidos, email, contraseña, fecha_nacimiento, eliminado, ultimo_inicio_sesion, fecha_creacion);
    	this.esEstudiante = esEstudiante;
    	this.esJubilado = esJubilado;
    	this.estado_descuento = estado_descuento;
    }

	public List<Venta_Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<Venta_Compra> compras) {
		this.compras = compras;
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

	public boolean isEstado_descuento() {
		return estado_descuento;
	}

	public void setEstado_descuento(boolean estado_descuento) {
		this.estado_descuento = estado_descuento;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
    
    


}