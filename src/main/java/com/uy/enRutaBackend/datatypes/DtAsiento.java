package com.uy.enRutaBackend.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DtAsiento {

    private int id_asiento;
    private int numero_asiento;
    private int id_omnibus;

    public DtAsiento() {}

    public DtAsiento(int id_asiento, int numero_asiento, int id_omnibus) {
        this.id_asiento = id_asiento;
        this.numero_asiento = numero_asiento;
        this.id_omnibus = id_omnibus;
    }

    public int getId_asiento() {
        return id_asiento;
    }

    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public int getNumero_asiento() {
        return numero_asiento;
    }

    public void setNumero_asiento(int numero_asiento) {
        this.numero_asiento = numero_asiento;
    }

    public int getId_omnibus() {
        return id_omnibus;
    }

    public void setId_omnibus(int id_omnibus) {
        this.id_omnibus = id_omnibus;
    }
}