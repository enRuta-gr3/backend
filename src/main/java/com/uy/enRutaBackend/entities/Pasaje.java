package com.uy.enRutaBackend.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


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
    private Venta_Compra ventaCompra;
    
    @Enumerated(EnumType.STRING)
    @Column(name="estado_pasaje")
    private EstadoPasaje estadoPasaje;
    
    @Column(name = "fecha_venta")
    private Date fechaVenta;
    
    @Column(name = "fecha_devolucion")
    private Date fechaDevolucion;
    
    public Pasaje() {}
    
    public Pasaje(double precio, Viaje viaje, Asiento asiento, Venta_Compra venta_compra, EstadoPasaje estadoPasaje) {
        this.precio = precio;
        this.viaje = viaje;
        this.asiento = asiento;
        this.ventaCompra = venta_compra;
        this.estadoPasaje = estadoPasaje;
        this.fechaVenta = Date.valueOf(LocalDate.now(ZoneId.of("America/Montevideo")));
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
        return ventaCompra;
    }

    public void setVenta_compra(Venta_Compra venta_compra) {
        this.ventaCompra = venta_compra;
    }

	public EstadoPasaje getEstadoPasaje() {
		return estadoPasaje;
	}

	public void setEstadoPasaje(EstadoPasaje estadoPasaje) {
		this.estadoPasaje = estadoPasaje;
	}

	public Date getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
    
    
}