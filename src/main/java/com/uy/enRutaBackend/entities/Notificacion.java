package com.uy.enRutaBackend.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int id_notificacion;
    
    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "filtro_destinatario")
    private String filtro_destinatario;

    @OneToMany(mappedBy = "id_buzon")
    private List<Buzon_notificacion> destinatarios;
    
    public int getId() {
        return id_notificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFiltroDestinatario() {
        return filtro_destinatario;
    }

    public void setFiltroDestinatario(String filtro_destinatario) {
        this.filtro_destinatario = filtro_destinatario;
    }

    public List<Buzon_notificacion> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<Buzon_notificacion> destinatarios) {
        this.destinatarios = destinatarios;
    }

}
