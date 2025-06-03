package com.uy.enRutaBackend.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DisAsiento_Viaje")
public class DisAsiento_Viaje {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disAsiento")
	private int id_disAsiento;
	
	@ManyToOne
	@JoinColumn(name = "asiento_id") 
	private Asiento asiento;
	
	@ManyToOne
	@JoinColumn(name = "viaje_id") 
	private Viaje viaje;
	
	@Enumerated(EnumType.STRING)
	private EstadoAsiento estado;
	
	@Column(nullable = true)
	private String idBloqueo;
	
	@Column(nullable = true)
	private Date fechaBloqueo;

	public int getId_disAsiento() {
		return id_disAsiento;
	}

	public void setId_disAsiento(int id_disAsiento) {
		this.id_disAsiento = id_disAsiento;
	}

	public Asiento getAsiento() {
		return asiento;
	}

	public void setAsiento(Asiento asiento) {
		this.asiento = asiento;
	}

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

	public EstadoAsiento getEstado() {
		return estado;
	}

	public void setEstado(EstadoAsiento estado) {
		this.estado = estado;
	}

	public String getIdBloqueo() {
		return idBloqueo;
	}

	public void setIdBloqueo(String idBloqueo) {
		this.idBloqueo = idBloqueo;
	}

	public Date getFechaBloqueo() {
		return fechaBloqueo;
	}

	public void setFechaBloqueo(Date fechaBloqueo) {
		this.fechaBloqueo = fechaBloqueo;
	}
}
