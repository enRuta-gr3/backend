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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

}
