package com.uy.enRutaBackend.entities;

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

}
