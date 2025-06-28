package com.uy.enRutaBackend.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uy.enRutaBackend.datatypes.DtNotificacionesPush;
import com.uy.enRutaBackend.datatypes.NotificacionesPushData;
import com.uy.enRutaBackend.datatypes.Respuesta;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.icontrollers.IServicePushNotifications;

@Service
public class PushNotificationsService implements IServicePushNotifications {
	
	@Value("${push.notifications.url}")
	private String pushNotificationServiceurl;
	
	
	public void enviarNotificacionPush(Usuario usuario, String titulo, String mensaje) {
		try {
		RestTemplate restTemplate = new RestTemplate();
		DtNotificacionesPush dtNotificacion = createDt(usuario, titulo, mensaje);
		
		ResponseEntity<String> respuesta = restTemplate.postForEntity(pushNotificationServiceurl, dtNotificacion, String.class);
		String respuestaBody = respuesta.getBody();
		ObjectMapper mapper = new ObjectMapper();
		Respuesta res = mapper.readValue(respuestaBody, Respuesta.class);
		String status = res.data().status();

		System.out.println(respuestaBody);
			if(status.equalsIgnoreCase("ok")) {
				System.out.println("Notificacion push enviada correctamente.");
			} else {
				System.out.println("Error enviando notificacion push de " + titulo + ", al cliente " + usuario.getUuidAuth());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	private DtNotificacionesPush createDt(Usuario usuario, String titulo, String mensaje) {
		Cliente cliente = (Cliente) usuario;
		DtNotificacionesPush dtNotificacion = new DtNotificacionesPush();
		dtNotificacion.setBody(mensaje);
		dtNotificacion.setTitle(titulo);
		dtNotificacion.setTo(cliente.getPushToken());
		dtNotificacion.setBadge(1);
		dtNotificacion.setData(new NotificacionesPushData(usuario.getUuidAuth().toString()));
		return dtNotificacion;
	}

	public String getPushNotificationServiceurl() {
		return pushNotificationServiceurl;
	}

	public void setPushNotificationServiceurl(String pushNotificationServiceurl) {
		this.pushNotificationServiceurl = pushNotificationServiceurl;
	}
}
