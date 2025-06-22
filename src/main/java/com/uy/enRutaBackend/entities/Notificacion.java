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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int id_notificacion;
    
    @ManyToOne
    @JoinColumn(name = "buzon_id")
    private Buzon_notificacion buzonNotificacion;
	
    private boolean leido;
    
    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "filtro_destinatario")
    private String filtro_destinatario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_envio")
    private Date fechaEnvio;
    
	public int getId_notificacion() {
        return id_notificacion;
    }

	public void setId_notificacion(int id_notificacion) {
		this.id_notificacion = id_notificacion;
	}

	public Buzon_notificacion getBuzon_notificacion() {
		return buzonNotificacion;
	}

	public void setBuzon_notificacion(Buzon_notificacion buzon_notificacion) {
		this.buzonNotificacion = buzon_notificacion;
	}

	public boolean isLeido() {
		return leido;
	}

	public void setLeido(boolean leido) {
		this.leido = leido;
	}

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

	public String getFiltro_destinatario() {
        return filtro_destinatario;
    }

	public void setFiltro_destinatario(String filtro_destinatario) {
        this.filtro_destinatario = filtro_destinatario;
    }

	public Date getFechaEnvio() {
		return fechaEnvio;
    }

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
    }

}
