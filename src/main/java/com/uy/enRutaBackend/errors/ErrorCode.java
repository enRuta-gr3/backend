package com.uy.enRutaBackend.errors;

public enum ErrorCode {
	LISTA_VACIA("No hay datos para mostrar"),
	ERROR_DE_CREACION("Error en el registro."),
	CREDENCIALES_INVALIDAS("Usuario o contraseña incorrecto"),
	YA_EXISTE("Ya existe un objeto con esos datos."),
	REQUEST_INVALIDO("Error en la operación."),
	ERROR_LISTADO("Error al obtener el listado"),
	DATOS_INSUFICIENTES("Se requiere minimo ci o correo"),
	SIN_RESULTADOS("Usuario no encontrado"),
	USUARIO_YA_ELIMINADO("El usuario ya esta eliminado"),
	TOKEN_INVALIDO("Session invalida o expirada"),
	NO_AUTORIZADO("No tiene permiso para eliminar este usuario"),
	ERROR_CONSULTANDO_BASE("Ocurrió un error al ir contra la base de datos.")
	;
	private final String msg;

	ErrorCode(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
