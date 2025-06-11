package com.uy.enRutaBackend.entities;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {
	
	public Administrador() {}
	
    public Administrador(String ci, String nombres, String apellidos, String email, String contraseña, Date fecha_nacimiento, boolean eliminado, Date ultimo_inicio_sesion, Date fecha_creacion) {
    	super(ci, nombres, apellidos, email, contraseña, fecha_nacimiento, eliminado, ultimo_inicio_sesion, fecha_creacion);
    }

}


