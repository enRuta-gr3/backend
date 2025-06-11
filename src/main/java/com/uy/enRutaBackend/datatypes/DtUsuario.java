package com.uy.enRutaBackend.datatypes;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uy.enRutaBackend.entities.Buzon_notificacion;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtUsuario {
    
	private String tipo_usuario;

    private UUID uuidAuth;
    
    private String ci;
    
    private String nombres;
    
    private String apellidos;
        
    private String email;
    
    private String contraseña;
    
    private String contraseña_nueva;
    
    private Date fecha_nacimiento;
    
    private boolean eliminado;
    
    private Date ultimo_inicio_sesion;
    
    private Date fecha_creacion;
    
    private boolean esEstudiante;
    
    private boolean esJubilado;
    
    private boolean estado_descuento;
    
    private List<Buzon_notificacion> notificaciones;
    
    
    public DtUsuario() {}
    
    public DtUsuario(String tipo_usuario, UUID uuidAuth, String ci, String nombres, String apellidos, String email) {
    	this.tipo_usuario = tipo_usuario;
    	this.uuidAuth = uuidAuth;
    	this.ci = ci;
    	this.nombres = nombres;
    	this.apellidos = apellidos;
    	this.email = email;
    }
    
    public DtUsuario(String tipo_usuario, UUID uuidAuth, String ci, String nombres, String apellidos, String email, Date fecha_nacimiento) {
    	this.tipo_usuario = tipo_usuario;
    	this.uuidAuth = uuidAuth;
    	this.ci = ci;
    	this.nombres = nombres;
    	this.apellidos = apellidos;
    	this.email = email;
    	this.fecha_nacimiento = fecha_nacimiento;
    }
    
    public DtUsuario(String tipo_usuario, String ci, String nombres, String apellidos, String email, String contraseña, Date fecha_nacimiento, boolean eliminado, Date ultimo_inicio_sesion, Date fecha_creacion, boolean esEstudiante, boolean esJubilado, boolean estado_descuento) {
    	this.tipo_usuario = tipo_usuario;
    	this.ci = ci;
    	this.nombres = nombres;
    	this.apellidos = apellidos;
    	this.email = email;
    	this.contraseña = contraseña;
    	this.fecha_nacimiento = fecha_nacimiento;
    	this.eliminado = eliminado;
    	this.ultimo_inicio_sesion = ultimo_inicio_sesion;
    	this.esEstudiante = esEstudiante;
    	this.esJubilado = esJubilado;
    	this.fecha_creacion = fecha_creacion;    	
    	this.estado_descuento = estado_descuento;

    }

    @Override
	public String toString() {
		return "DtUsuario [tipo_usuario=" + tipo_usuario + ", ci=" + ci + ", nombres=" + nombres + ", apellidos="
				+ apellidos + ", email=" + email + ", fecha_nacimiento=" + fecha_nacimiento + ", fecha_creacion="
				+ fecha_creacion + ", esEstudiante=" + esEstudiante + ", esJubilado=" + esJubilado + "]";
	}
    
    public UUID getUuidAuth() {
        return uuidAuth;
    }

    public String getContraseña_nueva() {
		return contraseña_nueva;
	}

	public void setContraseña_nueva(String contraseña_nueva) {
		this.contraseña_nueva = contraseña_nueva;
	}

    public void setUuidAuth(UUID uuidAuth) {
        this.uuidAuth = uuidAuth;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Date getUltimo_inicio_sesion() {
        return ultimo_inicio_sesion;
    }

    public void setUltimo_inicio_sesion(Date ultimo_inicio_sesion) {
        this.ultimo_inicio_sesion = ultimo_inicio_sesion;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public boolean isEstado_descuento() {
        return estado_descuento;
    }

    public void setEstado_descuento(boolean estado_descuento) {
        this.estado_descuento = estado_descuento;
    }

    public List<Buzon_notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Buzon_notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

	public boolean isEsEstudiante() {
		return esEstudiante;
	}

	public void setEsEstudiante(boolean esEstudiante) {
		this.esEstudiante = esEstudiante;
	}

	public boolean isEsJubilado() {
		return esJubilado;
	}

	public void setEsJubilado(boolean esJubilado) {
		this.esJubilado = esJubilado;
	}
}