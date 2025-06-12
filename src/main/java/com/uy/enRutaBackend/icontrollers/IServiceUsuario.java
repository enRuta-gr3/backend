package com.uy.enRutaBackend.icontrollers;

import java.io.IOException;
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
	public ResultadoOperacion<?> cambiarPassword(DtUsuario datos);
	public ResultadoOperacion<?> confirmarRecuperacion(String token, String nuevaPassword);
	public ResultadoOperacion<?> solicitarRecuperacion(String email);
	public ResultadoOperacion<?> eliminarUsuario(String token, DtUsuario datos);
	ResultadoOperacion<?> modificarPerfil(DtUsuario usuario);
}
