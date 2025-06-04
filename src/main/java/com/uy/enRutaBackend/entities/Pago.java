package com.uy.enRutaBackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pago")
public class Pago {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
	private int id_pago;
	
    @Column(name = "monto")
	private double monto;
	
    @Enumerated(EnumType.STRING)
	private EstadoTransaccion estado_trx;
	
    @OneToOne
    @JoinColumn(name = "venta_compra")
	private Venta_Compra venta_compra;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "medio_de_pago", nullable = false)
    private Medio_de_Pago medio_de_pago;
    
    
    
    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public EstadoTransaccion getEstado_trx() {
        return estado_trx;
    }

    public void setEstado_trx(EstadoTransaccion estado_trx) {
        this.estado_trx = estado_trx;
    }

    public Venta_Compra getVenta_compra() {
        return venta_compra;
    }

    public void setVenta_compra(Venta_Compra venta_compra) {
        this.venta_compra = venta_compra;
    }

	public Medio_de_Pago getMedio_de_pago() {
		return medio_de_pago;
	}

	public void setMedio_de_pago(Medio_de_Pago medio_de_pago) {
		this.medio_de_pago = medio_de_pago;
	}
}

