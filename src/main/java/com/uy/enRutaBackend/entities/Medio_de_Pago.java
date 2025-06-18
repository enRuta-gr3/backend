package com.uy.enRutaBackend.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Medio_de_Pago")
public class Medio_de_Pago {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medio_de_pago")
	private int id_medio_de_pago;
	
    @Column(name = "nombre")
	private String nombre;
	
    @Column(name = "perfiles_habilitados")
	private String perfiles_habilitados;
    
    @Column(name = "cotizacion", nullable = true)
    private double cotizacion;
    
    @Column(name = "habilitado")
    private boolean habilitado;
    
    @OneToMany(mappedBy = "id_pago")
    private List<Pago> pagos;
    
    
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
    
    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

	public double getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(double cotizacion) {
		this.cotizacion = cotizacion;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
    
}
