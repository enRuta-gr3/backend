package com.uy.enRutaBackend.icontrollers;

import java.util.UUID;

import org.json.JSONObject;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;

public interface IServiceUsuario {

	void correrValidaciones(DtUsuario usuario) throws UsuarioExistenteException ;
	public DtUsuario registrarUsuario(DtUsuario usuario) throws Exception;
	public UUID buscarUUIDPorEmail(String email);
	public JSONObject login(DtUsuario request) throws Exception;
	DtUsuario registrarUsuarioSinVerificacion(DtUsuario usuario) throws UsuarioExistenteException, Exception;
}
