package com.uy.enRutaBackend.icontrollers;

import com.uy.enRutaBackend.entities.Usuario;

public interface IServicePushNotifications {
	void enviarNotificacionPush(Usuario usuario, String titulo, String mensaje);
}
