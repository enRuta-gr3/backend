package com.uy.enRutaBackend.entities;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "buzon_notificacion")
public class Buzon_notificacion {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_buzon_notificacion")
    private int id_buzon_notificacion;

    @OneToMany(mappedBy = "buzonNotificacion" , cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notificacion> notificaciones;

    @OneToOne
	@JoinColumn(name = "usuario_id") 
	private Usuario usuario;
	
	public int getId_buzon_notificacion() {
		return id_buzon_notificacion;
	}
	
	public void setId_buzon_notificacion(int id_buzon_notificacion) {
		this.id_buzon_notificacion = id_buzon_notificacion;
	}
    
	public List<Notificacion> getNotificaciones() {
		return notificaciones;
	}
    
	public void setNotificaciones(List<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }



}

