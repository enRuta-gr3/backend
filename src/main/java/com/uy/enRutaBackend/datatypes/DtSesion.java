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

	public DtUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(DtUsuario usuario) {
		this.usuario = usuario;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Date getFechaInicioSesion() {
		return fechaInicioSesion;
	}

	public void setFechaInicioSesion(Date fechaInicioSesion) {
		this.fechaInicioSesion = fechaInicioSesion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public int getVigencia() {
		return vigencia;
	}

	public void setVigencia(int vigencia) {
		this.vigencia = vigencia;
	}


}
