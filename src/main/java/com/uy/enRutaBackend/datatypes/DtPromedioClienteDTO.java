package com.uy.enRutaBackend.datatypes;

import java.util.UUID;

public class DtPromedioClienteDTO {

    private UUID clienteId;
    private String nombre;
    private Double promedio;

    public DtPromedioClienteDTO(UUID clienteId, String nombre, Double promedio) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.promedio = promedio;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPromedio() {
        return promedio;
    }
}
