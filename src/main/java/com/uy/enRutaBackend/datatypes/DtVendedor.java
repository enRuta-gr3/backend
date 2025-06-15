package com.uy.enRutaBackend.datatypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uy.enRutaBackend.entities.Historico_estado;
import com.uy.enRutaBackend.entities.Venta_Compra;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtVendedor extends DtUsuario{

    private List<Venta_Compra> ventas;

    private List<Historico_estado> historicoEstados;

	public DtVendedor() {
		super();
	}
	
	public List<Venta_Compra> getVentas() {
		return ventas;
	}

	public List<Historico_estado> getHistoricoEstados() {
		return historicoEstados;
	}

	public void setHistoricoEstados(List<Historico_estado> historicoEstados) {
		this.historicoEstados = historicoEstados;
	}

	public void setVentas(List<Venta_Compra> ventas) {
		this.ventas = ventas;
	}
}

