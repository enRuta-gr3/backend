package com.uy.enRutaBackend.utils;

public enum AsuntoEmail {
	
	DEVOLUCION("Información sobre devolución de pasajes."),
	CIERRE_VENTA("Cambio en el estado de su viaje."),
	RECUPERAR_CONTRASEÑA("Recuperación de contraseña.");

	
	private final String asunto;

	AsuntoEmail(String asunto) {
		this.asunto = asunto;
	}
	
	public String getAsunto() {
		return asunto;
	}
	
}
