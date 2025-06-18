package com.uy.enRutaBackend.exceptions;

/**
 * Excepcion utilizada para indicar que el usuario que se desea dar de alta ya existe.
 */
@SuppressWarnings("serial")
public class LocalidadExistenteException extends Exception {
	
	public LocalidadExistenteException(String string) {
		super(string);
	}	
}
