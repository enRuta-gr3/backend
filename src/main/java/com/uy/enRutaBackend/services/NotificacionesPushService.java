package com.uy.enRutaBackend.services;

import org.springframework.beans.factory.annotation.Value;

public class NotificacionesPushService {
	
	@Value("${https://exp.host/--/api/v2/push/send}")
	private String notificacionesUrl;

	public 
}
