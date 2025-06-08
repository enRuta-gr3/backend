package com.uy.enRutaBackend.errors;

public enum ErrorCode {
	LISTA_VACIA("No hay datos para mostrar"),
	ERROR_DE_CREACION("Error en el registro."),
	CREDENCIALES_INVALIDAS("Usuario o contraseña incorrecto"),
	YA_EXISTE("Ya existe un objeto con esos datos."),
	REQUEST_INVALIDO("Error en la operación."),
	ERROR_LISTADO("Error al obtener el listado"),
	DATOS_INSUFICIENTES("Faltan datos para procesar la solicitud."),
	SIN_RESULTADOS("No se encontraron datos que cumplan el request")
	;
	private final String msg;

	ErrorCode(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
