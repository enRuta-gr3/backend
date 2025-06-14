package com.uy.enRutaBackend.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class TareaProgramada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Omnibus omnibus;

    @Column(nullable = false)
    private boolean nuevoEstado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_ejecucion")
    private Date fechaEjecucion;
    
    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Vendedor vendedor;

    @Column
    private String descripcion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Omnibus getOmnibus() {
		return omnibus;
	}

	public void setOmnibus(Omnibus omnibus) {
		this.omnibus = omnibus;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public boolean isNuevoEstado() {
		return nuevoEstado;
	}

	public void setNuevoEstado(boolean nuevoEstado) {
		this.nuevoEstado = nuevoEstado;
	}

	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
    
}
