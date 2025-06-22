package com.uy.enRutaBackend.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uy.enRutaBackend.entities.Buzon_notificacion;
import com.uy.enRutaBackend.entities.Notificacion;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.persistence.BuzonNotificacionRepository;
import com.uy.enRutaBackend.persistence.NotificacionRepository;
import com.uy.enRutaBackend.persistence.UsuarioRepository;
import com.uy.enRutaBackend.services.EmailService;

@Component
public class NotificacionesHelper {
	
	@Autowired
	private NotificacionRepository notificacionRepository;
	@Autowired
    private BuzonNotificacionRepository buzonRepository;
	@Autowired
    private UsuarioRepository usuarioRepository;
	@Autowired
	private EmailService mailService;
	
	public Buzon_notificacion obtenerBuzon(Usuario usuario) {
		Buzon_notificacion buzon = usuario.getNotificaciones();
        if (buzon == null) {
            System.out.println("⚠️ Usuario sin buzón: " + usuario.getUuidAuth());
            buzon = crearBuzonUsuarioExistente(usuario);
        }
        return buzon;
	}

	/**
	 * @param usuario
	 * @return
	 */
	private Buzon_notificacion crearBuzonUsuarioExistente(Usuario usuario) {
		Buzon_notificacion buzon;
		buzon = new Buzon_notificacion();
		buzon.setUsuario(usuario);            
		buzonRepository.save(buzon);
		
		usuario.setBuzonNotificacion(buzon);
		usuarioRepository.save(usuario);
		
		System.out.println("Se creo nuevo buzón para el usuario.");
		return buzon;
	}

	public void generarNotificacion(Buzon_notificacion buzon, String mensaje, String destinatario) {
        Notificacion noti = new Notificacion();
        noti.setMensaje(mensaje);
        noti.setFechaEnvio(new Date());
        noti.setFiltro_destinatario(destinatario);
        noti.setLeido(false);
        noti.setBuzon_notificacion(buzon);
        notificacionRepository.save(noti);
	}

	public void enviarNotificacionEmail(Usuario usuario, String asunto, String mensaje) {
		if(usuario.getEmail() != null) {
			mailService.send(usuario.getEmail(), asunto, mensaje);			
		} else {
			System.out.println("⚠️ El cliente no tiene mail: " + usuario.getUuidAuth());
		}
	}

	public void enviarNotificacionPush(Usuario usuario) {
		// TODO Auto-generated method stub		
	}
}
