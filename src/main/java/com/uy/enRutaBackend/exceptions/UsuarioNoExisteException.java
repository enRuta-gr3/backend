package com.uy.enRutaBackend.exceptions;

/**
 * Excepcion utilizada para indicar que el usuario que se está buscando no existe.
 */
@SuppressWarnings("serial")
public class UsuarioNoExisteException extends Exception {
	
	public UsuarioNoExisteException(String string) {
		super(string);
	}	
}
