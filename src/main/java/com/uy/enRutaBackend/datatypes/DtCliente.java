package com.uy.enRutaBackend.datatypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtCliente extends DtUsuario {
	
	
    private List<DtVenta_Compra> compras;
	
    private List<DtPasaje> pasajesComprados;
	
    private boolean esEstudiante;
    
    private boolean esJubilado;
    
    private boolean estado_descuento;
	
	public DtCliente() {
		super();
	}

	public List<DtVenta_Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<DtVenta_Compra> compras) {
		this.compras = compras;
	}

	public List<DtPasaje> getPasajesComprados() {
		return pasajesComprados;
	}

	public void setPasajesComprados(List<DtPasaje> pasajesComprados) {
		this.pasajesComprados = pasajesComprados;
	}

	public boolean isEsEstudiante() {
		return esEstudiante;
	}

	public void setEsEstudiante(boolean esEstudiante) {
		this.esEstudiante = esEstudiante;
	}

	public boolean isEsJubilado() {
		return esJubilado;
	}

	public void setEsJubilado(boolean esJubilado) {
		this.esJubilado = esJubilado;
	}

	public boolean isEstado_descuento() {
		return estado_descuento;
	}

	public void setEstado_descuento(boolean estado_descuento) {
		this.estado_descuento = estado_descuento;
	}
    
    


}

