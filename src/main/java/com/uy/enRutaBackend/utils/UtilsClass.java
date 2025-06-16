package com.uy.enRutaBackend.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.uy.enRutaBackend.datatypes.DtResultadoCargaMasiva;

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
	
	public void actualizarResultado(DtResultadoCargaMasiva resultadoCargaMasiva, String estado, Object registrado) {
		if(registrado != null && estado.equals("ok")) {
			resultadoCargaMasiva.setTotalLineasOk(resultadoCargaMasiva.getTotalLineasOk()+1);
			resultadoCargaMasiva.agregarElemento(registrado);
		} else {
			resultadoCargaMasiva.setTotalLineasError(resultadoCargaMasiva.getTotalLineasError()+1);
		}
	}

}
