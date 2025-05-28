package com.uy.enRutaBackend.datatypes;

import java.sql.Date;
import java.sql.Time;

public class DtViaje {

    private int id_viaje;
    private Date fecha_partida;
    private Time hora_partida;
    private Date fecha_llegada;
    private Time hora_llegada;
    private double precio_viaje;
    private String estado;

    private int id_localidad_origen;
    private int id_localidad_destino;
    private int id_omnibus;

    public DtViaje() {}

    public DtViaje(int id_viaje, Date fecha_partida, Time hora_partida, Date fecha_llegada, Time hora_llegada,
                   double precio_viaje, String estado, int id_localidad_origen, int id_localidad_destino, int id_omnibus) {
        this.id_viaje = id_viaje;
        this.fecha_partida = fecha_partida;
        this.hora_partida = hora_partida;
        this.fecha_llegada = fecha_llegada;
        this.hora_llegada = hora_llegada;
        this.precio_viaje = precio_viaje;
        this.estado = estado;
        this.id_localidad_origen = id_localidad_origen;
        this.id_localidad_destino = id_localidad_destino;
        this.id_omnibus = id_omnibus;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_localidad_origen() {
        return id_localidad_origen;
    }

    public void setId_localidad_origen(int id_localidad_origen) {
        this.id_localidad_origen = id_localidad_origen;
    }

    public int getId_localidad_destino() {
        return id_localidad_destino;
    }

    public void setId_localidad_destino(int id_localidad_destino) {
        this.id_localidad_destino = id_localidad_destino;
    }

    public int getId_omnibus() {
        return id_omnibus;
    }

    public void setId_omnibus(int id_omnibus) {
        this.id_omnibus = id_omnibus;
    }
}
