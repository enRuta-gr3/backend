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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Sesion")
public class Sesion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_sesion;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@Column(name = "token")
	private String access_token;
	
	@Column(name = "fecha_inicio_sesion")
	private Date fechaInicioSesion;
	
	@Column(name = "activo")
	private boolean estado;
	
	@Column(name = "vigencia")
	private int vigencia;
}
