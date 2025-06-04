package com.uy.enRutaBackend.entities;

import java.util.Optional;

import jakarta.persistence.*;


@Entity
@Table(name = "Pasaje")
public class Pasaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasaje")
    private int id_pasaje;
    
    @Column(name = "precio")
    private double precio;

    @ManyToOne
    @JoinColumn(name = "id_viaje")
    private Viaje viaje;
    
    @ManyToOne
    @JoinColumn(name = "id_asiento")
    private Asiento asiento;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_venta_compra", nullable = false)
    private Venta_Compra venta_compra;
    
    public Pasaje() {}
    
    public Pasaje(double precio, Viaje viaje, Asiento asiento, Venta_Compra venta_compra) {
        this.precio = precio;
        this.viaje = viaje;
        this.asiento = asiento;
        this.venta_compra = venta_compra;
    }

    
    
    public int getId_pasaje() {
        return id_pasaje;
    }

    public void setId_pasaje(int id_pasaje) {
        this.id_pasaje = id_pasaje;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }
    
    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public Venta_Compra getVenta_compra() {
        return venta_compra;
    }

    public void setVenta_compra(Venta_Compra venta_compra) {
        this.venta_compra = venta_compra;
    }
    
    
}