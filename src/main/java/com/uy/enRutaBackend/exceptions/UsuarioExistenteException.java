package com.uy.enRutaBackend.exceptions;

/**
 * Excepcion utilizada para indicar que el usuario que se desea dar de alta ya existe.
 */
@SuppressWarnings("serial")
public class UsuarioExistenteException extends Exception {
	
	public UsuarioExistenteException(String string) {
		super(string);
	}	
}
