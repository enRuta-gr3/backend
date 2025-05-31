package com.uy.enRutaBackend.datatypes;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uy.enRutaBackend.entities.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtSesion {
	private DtUsuario usuario;
	private String access_token;
	private Date fechaInicioSesion;
	private boolean activo;
	private int vigencia;

	public DtSesion() {}
	
	public DtSesion(DtUsuario usuario, String token, Date fechaInicioSesion, boolean activo, int vigencia) {
		super();
		this.usuario = usuario;
		this.access_token = token;
		this.fechaInicioSesion = fechaInicioSesion;
		this.activo = activo;
		this.vigencia = vigencia;
	}

}
