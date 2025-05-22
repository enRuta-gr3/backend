package com.uy.enRutaBackend.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Venta_Compra")
public class Venta_Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private int id_venta;

    @ManyToOne(optional = true)
    @JoinColumn(name = "vendedor_id", nullable = true)
    private Vendedor vendedor;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "descuento_id", nullable = true)
    private Descuento descuento;
    
    @OneToOne
    @JoinColumn(name = "pago")
    private Pago pago;
    
    @OneToMany
    @JoinColumn(name = "pasaje")
    private List<Pasaje> pasajes;
    
}