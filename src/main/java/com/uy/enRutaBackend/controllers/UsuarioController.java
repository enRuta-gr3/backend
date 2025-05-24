package com.uy.enRutaBackend.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;

import jakarta.validation.Valid;
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
				return new ResultadoOperacion(false, e.getMessage(), usuRegistro.toString());
			} else {
				return new ResultadoOperacion(false, ERROR_MESSAGE, usuRegistro.toString());
			}		
		}
    }

	private ResultadoOperacion<?> registrarUsuarioSinVerificacion(DtUsuario usuario, DtUsuario usuRegistro) {
		try {
			usuRegistro = serviceUsuario.registrarUsuarioSinVerificacion(usuario);
			return new ResultadoOperacion(true, OK_MESSAGE, usuRegistro.toString());
		} catch (Exception e){
			if(e instanceof UsuarioExistenteException) {
				return new ResultadoOperacion(false, e.getMessage(), usuRegistro.toString());
			} else {
				return new ResultadoOperacion(false, ERROR_MESSAGE, usuRegistro.toString());
			}		
		}
	}	
	
	public JSONObject login(DtUsuario request) {
		JSONObject result = new JSONObject();
			try {
				result = serviceUsuario.login(request);
			} catch (Exception e) {
				result = new JSONObject("error");
			}
		return result;
	}
}
