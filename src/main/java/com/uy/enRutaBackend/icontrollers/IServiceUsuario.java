package com.uy.enRutaBackend.icontrollers;

import java.util.UUID;

import org.json.JSONObject;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;

public interface IServiceUsuario {

	void correrValidaciones(DtUsuario usuario) throws UsuarioExistenteException ;
	public ResultadoOperacion<?> registrarUsuario(DtUsuario usuario) throws Exception;
	public UUID buscarUUIDPorEmail(String email);
	ResultadoOperacion<?> iniciarSesion(DtUsuario request);
	DtUsuario registrarUsuarioSinVerificacion(DtUsuario usuario) throws UsuarioExistenteException, Exception;
	ResultadoOperacion<?> buscarUsuarioPorCi(DtUsuario usuario);
}
