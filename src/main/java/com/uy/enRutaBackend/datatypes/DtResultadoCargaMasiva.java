package com.uy.enRutaBackend.datatypes;

import java.util.ArrayList;
import java.util.List;

public class DtResultadoCargaMasiva {
	
	private int totalLineasARegistrar;
	private int totalLineasError;
	private int totalLineasOk;
	
	List<Object> objetos;
	
	public DtResultadoCargaMasiva() {
		this.totalLineasARegistrar = 0;
		this.totalLineasError = 0;
		this.totalLineasOk = 0;
		this.objetos = new ArrayList<Object>();
	}
	
	public int getTotalLineasARegistrar() {
		return totalLineasARegistrar;
	}
	public void setTotalLineasARegistrar(int totalLineasARegistrar) {
		this.totalLineasARegistrar = totalLineasARegistrar;
	}
	public int getTotalLineasError() {
		return totalLineasError;
	}
	public void setTotalLineasError(int totalLineasError) {
		this.totalLineasError = totalLineasError;
	}
	public int getTotalLineasOk() {
		return totalLineasOk;
	}
	public void setTotalLineasOk(int totalLineasOk) {
		this.totalLineasOk = totalLineasOk;
	}
	
	public void agregarElemento(Object elemento) {
		objetos.add(elemento);
    }

    public List<Object> getElementos() {
        return objetos;
    }
}
