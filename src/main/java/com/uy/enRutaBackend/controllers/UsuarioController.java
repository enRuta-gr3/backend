package com.uy.enRutaBackend.controllers;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;

import lombok.Getter;

@RestController
@Getter
public class UsuarioController {
	private static final String OK_MESSAGE = "Operación realizada con éxito";
	private static final String ERROR_MESSAGE = "Error al realizar la operación";
	@Autowired
    private IServiceUsuario serviceUsuario;
	
	public ResultadoOperacion<?> registrarUsuario(@RequestBody DtUsuario usuario) {
		DtUsuario usuRegistro = new DtUsuario();
		try {
			serviceUsuario.correrValidaciones(usuario);
			if(usuario.getTipo_usuario().equalsIgnoreCase("CLIENTE") && (usuario.getEmail() != null && !usuario.getEmail().isEmpty())) {
				usuRegistro = serviceUsuario.registrarUsuario(usuario);
				return new ResultadoOperacion(true, OK_MESSAGE, usuRegistro.toString());
			} else {
				return registrarUsuarioSinVerificacion(usuario, usuRegistro);
			}
		} catch (Exception e){
			if(e instanceof UsuarioExistenteException) {
				return new ResultadoOperacion(false, e.getMessage(), e.getMessage());
			} else {
				return new ResultadoOperacion(false, ERROR_MESSAGE, e.getMessage());
			}		
		}
    }

	private ResultadoOperacion<?> registrarUsuarioSinVerificacion(DtUsuario usuario, DtUsuario usuRegistro) {
		try {
			usuRegistro = serviceUsuario.registrarUsuarioSinVerificacion(usuario);
			return new ResultadoOperacion(true, OK_MESSAGE, usuRegistro.toString());
		} catch (Exception e){
			if(e instanceof UsuarioExistenteException) {
				return new ResultadoOperacion(false, e.getMessage(), e.getMessage());
			} else {
				return new ResultadoOperacion(false, ERROR_MESSAGE, e.getMessage());
			}		
		}
	}	
	
	public ResultadoOperacion<?> iniciarSesion(DtUsuario request) {
		JSONObject result = new JSONObject();
		try {
			result = serviceUsuario.iniciarSesion(request);
		} catch (Exception e) {
			result = new JSONObject("error : " + e.getMessage());
		}
		if (result.has("access_token")) {
			return new ResultadoOperacion(true, OK_MESSAGE, result.toString());
		} else {
			return new ResultadoOperacion(false, ERROR_MESSAGE, result.toString());
		}
	}
}
