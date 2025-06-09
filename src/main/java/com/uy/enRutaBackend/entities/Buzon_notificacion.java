package com.uy.enRutaBackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Buzon_notifiacion")
public class Buzon_notificacion {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_buzon")
    private int id_buzon;

	@ManyToOne
	@JoinColumn(name = "usuario_id") 
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "notificaciones_id")
	private Notificacion notificacion;
	
    private boolean leido;
    
    
    
    public int getId() {
        return id_buzon;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Notificacion getNotificacion() {
        return notificacion;
    }

    public void setBuzon(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}

