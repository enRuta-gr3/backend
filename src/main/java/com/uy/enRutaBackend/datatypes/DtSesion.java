package com.uy.enRutaBackend.datatypes;

import java.util.Date;

import com.uy.enRutaBackend.entities.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtSesion {
	private Usuario usuario;
	private String access_token;
	private Date fechaInicioSesion;
	private boolean estado;
	private int vigencia;

	public DtSesion() {}
	
	public DtSesion(Usuario usuario, String token, Date fechaInicioSesion, boolean activo, int vigencia) {
		super();
		this.usuario = usuario;
		this.access_token = token;
		this.fechaInicioSesion = fechaInicioSesion;
		this.estado = activo;
		this.vigencia = vigencia;
	}

}
