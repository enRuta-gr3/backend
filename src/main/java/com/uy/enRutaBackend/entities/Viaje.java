package com.uy.enRutaBackend.entities;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "Viaje")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private int id_viaje;

    @Column(name = "fecha_partida")
    private Date fecha_partida;

    @Column(name = "hora_partida")
    private Time hora_partida;
    
    @Column(name = "fecha_llegada")
    private Date fecha_llegada;
    
    @Column(name = "hora_llegada")
    private Time hora_llegada;
    
    @Column(name = "precio")
    private double precio_viaje;
    
    @Enumerated(EnumType.STRING)
	private EstadoViaje estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_omnibus", nullable = false)
    private Omnibus omnibus;

    @OneToMany(mappedBy = "id_pasaje")
    private List<Pasaje> pasajes;
        
    @ManyToOne(optional = false)
    @JoinColumn(name = "localidad_origen_id", nullable = false)
    private Localidad localidadOrigen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "localidad_destino_id", nullable = false)
    private Localidad localidadDestino;
    
    @OneToMany(mappedBy = "id_disAsiento")
    private List<DisAsiento_Viaje> disponibilidad;  
    


    public Viaje() {}

    public Viaje(Date fecha_partida, Time hora_partida, Date fecha_llegada, Time hora_llegada,
                 double precio_viaje, EstadoViaje estado, Omnibus omnibus,
                 Localidad localidadOrigen, Localidad localidadDestino) {
        this.fecha_partida = fecha_partida;
        this.hora_partida = hora_partida;
        this.fecha_llegada = fecha_llegada;
        this.hora_llegada = hora_llegada;
        this.precio_viaje = precio_viaje;
        this.estado = estado;
        this.omnibus = omnibus;
        this.localidadOrigen = localidadOrigen;
        this.localidadDestino = localidadDestino;
    }

    public int getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(int id_viaje) {
        this.id_viaje = id_viaje;
    }

    public Date getFecha_partida() {
        return fecha_partida;
    }

    public void setFecha_partida(Date fecha_partida) {
        this.fecha_partida = fecha_partida;
    }

    public Time getHora_partida() {
        return hora_partida;
    }

    public void setHora_partida(Time hora_partida) {
        this.hora_partida = hora_partida;
    }

    public Date getFecha_llegada() {
        return fecha_llegada;
    }

    public void setFecha_llegada(Date fecha_llegada) {
        this.fecha_llegada = fecha_llegada;
    }

    public Time getHora_llegada() {
        return hora_llegada;
    }

    public void setHora_llegada(Time hora_llegada) {
        this.hora_llegada = hora_llegada;
    }

    public double getPrecio_viaje() {
        return precio_viaje;
    }

    public void setPrecio_viaje(double precio_viaje) {
        this.precio_viaje = precio_viaje;
    }

    public EstadoViaje getEstado() {
        return estado;
    }

    public void setEstado(EstadoViaje estado) {
        this.estado = estado;
    }

    public Omnibus getOmnibus() {
        return omnibus;
    }

    public void setOmnibus(Omnibus omnibus) {
        this.omnibus = omnibus;
    }

    public Localidad getLocalidadOrigen() {
        return localidadOrigen;
    }

    public void setLocalidadOrigen(Localidad localidadOrigen) {
        this.localidadOrigen = localidadOrigen;
    }

    public Localidad getLocalidadDestino() {
        return localidadDestino;
    }

    public void setLocalidadDestino(Localidad localidadDestino) {
        this.localidadDestino = localidadDestino;
    }

    public List<Pasaje> getPasajes() {
        return pasajes;
    }

    public void setPasajes(List<Pasaje> pasajes) {
        this.pasajes = pasajes;
    }
    
    public List<DisAsiento_Viaje> getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(List<DisAsiento_Viaje> disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
