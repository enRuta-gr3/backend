package com.uy.enRutaBackend.datatypes;

import java.util.List;


public class DtDescuento {

    private int id_descuento;

    private String tipo;

    private double porcentaje_descuento;
    
    private List<DtVenta_Compra> venta_compra;

    public DtDescuento() {}

    public int getId_descuento() {
        return id_descuento;
    }

    public void setId_descuento(int id_descuento) {
        this.id_descuento = id_descuento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(double porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }
    
    public List<DtVenta_Compra> getVentaCompra() {
        return venta_compra;
    }

    public void setVentaCompra(List<DtVenta_Compra> venta_compra) {
        this.venta_compra = venta_compra;
    }
    
    
}