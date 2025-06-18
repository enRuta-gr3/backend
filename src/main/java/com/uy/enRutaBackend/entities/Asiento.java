package com.uy.enRutaBackend.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Asiento")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento") 
    private int id_asiento;
    
    @Column(name = "numero_asiento")
    private int numero_asiento;
    
    @ManyToOne
    @JoinColumn(name = "id_omnibus")
    private Omnibus omnibus;

    @OneToMany(mappedBy = "asiento")
    private List<Pasaje> pasajes; 

    @OneToMany(mappedBy = "asiento")
    private List<DisAsiento_Viaje> disAsientoViajes;

    public Asiento() {}

    public Asiento(int numero_asiento, Omnibus omnibus, List<Pasaje> pasajes, List<DisAsiento_Viaje> disAsientoViajes) {
        this.numero_asiento = numero_asiento;
        this.omnibus = omnibus;
        this.pasajes = pasajes;
        this.disAsientoViajes = disAsientoViajes;
    }

    public int getId_asiento() {
        return id_asiento;
    }

    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public int getNumeroAsiento() {
        return numero_asiento;
    }

    public void setNumeroAsiento(int numero_asiento) {
        this.numero_asiento = numero_asiento;
    }

    public Omnibus getOmnibus() {
        return omnibus;
    }

    public void setOmnibus(Omnibus omnibus) {
        this.omnibus = omnibus;
    }

    public List<Pasaje> getPasajes() {
        return pasajes;
    }

    public void setPasajes(List<Pasaje> pasajes) {
        this.pasajes = pasajes;
    }

    public List<DisAsiento_Viaje> getDisAsientoViajes() {
        return disAsientoViajes;
    }

    public void setDisAsientoViajes(List<DisAsiento_Viaje> disAsientoViajes) {
        this.disAsientoViajes = disAsientoViajes;
    }
}
