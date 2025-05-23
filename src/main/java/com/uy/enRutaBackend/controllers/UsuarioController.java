package com.uy.enRutaBackend.controllers;

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
	
	public ResultadoOperacion registrarUsuario(@RequestBody DtUsuario usuario) {
		try {
			DtUsuario usuRegistro = serviceUsuario.registrarUsuario(usuario);
			return new ResultadoOperacion(true, OK_MESSAGE, usuario.getCi());
		} catch (Exception e){
			if(e instanceof UsuarioExistenteException) {
				return new ResultadoOperacion(false, e.getMessage(), usuario.getCi());
			} else {
				return new ResultadoOperacion(false, ERROR_MESSAGE, usuario.getCi());
			}		
		}
    }	
}
