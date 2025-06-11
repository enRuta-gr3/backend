package com.uy.enRutaBackend.errors;

public enum ErrorCode {
	LISTA_VACIA("No hay datos para mostrar."),
	ERROR_DE_CREACION("Error en el registro."),
	CREDENCIALES_INVALIDAS("Usuario o contraseña incorrecto"),
	YA_EXISTE("Ya existe un objeto con esos datos."),
	REQUEST_INVALIDO("Error en la operación."),
	ERROR_LISTADO("Error al obtener el listado."),
	DATOS_INSUFICIENTES("Se requiere minimo ci o correo."),
	SIN_RESULTADOS("Usuario no encontrado."),
	SIN_RESULTADOS_VENDEDOR("Vendedor no encontrado."),
	USUARIO_YA_ELIMINADO("El usuario ya esta eliminado."),
	TOKEN_INVALIDO("Session invalida o expirada."),
	NO_AUTORIZADO("No tiene permiso para eliminar este usuario."),
	FECHAS_INVALIDAS("Debe proporcionar fecha de inicio y fecha de fin si el ómnibus se marca como inactivo."),
	OPERACION_INVALIDA("El ómnibus tiene viajes asignados en ese período y no puede darse de baja.")
	;
	private final String msg;

	ErrorCode(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
