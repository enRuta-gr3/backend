package com.uy.enRutaBackend.exceptions;

/**
 * Excepcion utilizada para indicar que no hay viajes para listar.
 */
@SuppressWarnings("serial")
public class NoExistenViajesException extends Exception {
	
	public NoExistenViajesException(String string) {
		super(string);
	}	
}
