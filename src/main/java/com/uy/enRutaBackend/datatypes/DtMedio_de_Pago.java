package com.uy.enRutaBackend.datatypes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtMedio_de_Pago {

	private int id_medio_de_pago;
	
	private String nombre;

	private String perfiles_habilitados;
    
    private List<DtPago> pagos;
    
    public int getId_medio_de_pago() {
        return id_medio_de_pago;
    }

    public void setId_medio_de_pago(int id_medio_de_pago) {
        this.id_medio_de_pago = id_medio_de_pago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPerfiles_habilitados() {
        return perfiles_habilitados;
    }

    public void setPerfiles_habilitados(String perfiles_habilitados) {
        this.perfiles_habilitados = perfiles_habilitados;
    }
    
    public List<DtPago> getPagos() {
        return pagos;
    }

    public void setPagos(List<DtPago> pagos) {
        this.pagos = pagos;
    }
    
}