package com.uy.enRutaBackend.datatypes;

import java.util.Date;

public class DtNotificacion {
	
	private int id_notificacion;    
    private int buzonNotificacion;
    private boolean leido;
    private String mensaje;
    private Date fechaEnvio;
    
    public DtNotificacion() {}
    
	public DtNotificacion(int id_notificacion, int buzonNotificacion, boolean leido, String mensaje, Date fechaEnvio) {
		this.id_notificacion = id_notificacion;
		this.buzonNotificacion = buzonNotificacion;
		this.leido = leido;
		this.mensaje = mensaje;
		this.fechaEnvio = fechaEnvio;
	}
	public int getId_notificacion() {
		return id_notificacion;
	}
	public void setId_notificacion(int id_notificacion) {
		this.id_notificacion = id_notificacion;
	}
	public int getBuzonNotificacion() {
		return buzonNotificacion;
	}
	public void setBuzonNotificacion(int buzonNotificacion) {
		this.buzonNotificacion = buzonNotificacion;
	}
	public boolean isLeido() {
		return leido;
	}
	public void setLeido(boolean leido) {
		this.leido = leido;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
}
