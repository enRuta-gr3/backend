package com.uy.enRutaBackend.datatypes;

public class DtNotificacionesPush {
	
	private String to;
	private String title;
	private Integer badge;
	private String body;
	private NotificacionesPushData data;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getBadge() {
		return badge;
	}
	public void setBadge(Integer badge) {
		this.badge = badge;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public NotificacionesPushData getData() {
		return data;
	}
	public void setData(NotificacionesPushData data) {
		this.data = data;
	}
}
