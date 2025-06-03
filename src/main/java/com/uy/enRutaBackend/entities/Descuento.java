package com.uy.enRutaBackend.entities;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "Descuento")
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descuento")
    private int id_descuento;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "porcentaje_descuento", nullable = false)
    private double porcentaje_descuento;
    
    @OneToMany(mappedBy = "id_venta")
    private List<Venta_Compra> venta_compra;

    public Descuento() {}

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
    
    public List<Venta_Compra> getVentaCompra() {
        return venta_compra;
    }

    public void setVentaCompra(List<Venta_Compra> venta_compra) {
        this.venta_compra = venta_compra;
    }
    
    
}

