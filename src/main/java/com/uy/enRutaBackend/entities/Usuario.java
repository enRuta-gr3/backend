package com.uy.enRutaBackend.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Setter;

@Setter
@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
public class Usuario {
    
    @Id
    @Column(name = "uuid_auth")
    private UUID uuidAuth;
    
    @Column(name = "cedula", unique = true)
    private String ci;
    
    @Column(name = "nombres")
    private String nombres;
    
    @Column(name = "apellidos")
    private String apellidos;
        
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "contraseña")
    private String contraseña;
    
    @Column(name = "fecha_nacimiento")
    private Date fecha_nacimiento;
    
    @Column(name = "eliminado")
    private boolean eliminado;
    
    @Column(name = "ultimo_inicio_sesion")
    private Date ultimo_inicio_sesion;
    
    @Column(name = "fecha_creacion")
    private Date fecha_creacion;
       
    @OneToMany(mappedBy = "id_buzon")
    private List<Buzon_notificacion> notificaciones;
    
    @OneToMany(mappedBy = "id_sesion", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Sesion> sesiones;
    
    public Usuario() {}
    
    public Usuario(String ci, String nombres, String apellidos, String email, String contraseña, Date fecha_nacimiento, boolean eliminado, Date ultimo_inicio_sesion, Date fecha_creacion) {
    	this.ci = ci;
    	this.nombres = nombres;
    	this.apellidos = apellidos;
    	this.email = email;
    	this.contraseña = contraseña;
    	this.fecha_nacimiento = fecha_nacimiento;
    	this.eliminado = eliminado;
    	this.ultimo_inicio_sesion = ultimo_inicio_sesion;
    	this.fecha_creacion = fecha_creacion;  	   	

    }

    public UUID getUuidAuth() {
        return uuidAuth;
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
    
    
}