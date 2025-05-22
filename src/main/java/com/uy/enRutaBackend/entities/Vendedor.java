package com.uy.enRutaBackend.entities;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("VENDEDOR")

public class Vendedor extends Usuario {
	
	@OneToMany(mappedBy = "id_venta")
    private List<Venta_Compra> ventas;

	public Vendedor() {}
	
    public Vendedor(String ci, String nombres, String apellidos, String email, String contraseña, Date fecha_nacimiento, boolean eliminado, Date ultimo_inicio_sesion, Date fecha_creacion) {
    	super(ci, nombres, apellidos, email, contraseña, fecha_nacimiento, eliminado, ultimo_inicio_sesion, fecha_creacion);
    }
    
    public List<Venta_Compra> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta_Compra> ventas) {
        this.ventas = ventas;
    }
}

