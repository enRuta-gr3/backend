package com.uy.enRutaBackend.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class UtilsClass {
	
	public String dateToString(Date fecha) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormatter.format(fecha); 
	}
	
	public String timeToString(Time hora) {
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
		return timeFormatter.format(hora);
	}

}
