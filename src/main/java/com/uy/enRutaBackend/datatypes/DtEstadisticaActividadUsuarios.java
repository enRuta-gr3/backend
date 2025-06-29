package com.uy.enRutaBackend.datatypes;

public class DtEstadisticaActividadUsuarios {
    private long totalActivos;
    private long totalInactivos;

    public DtEstadisticaActividadUsuarios(long totalActivos, long totalInactivos) {
        this.totalActivos = totalActivos;
        this.totalInactivos = totalInactivos;
    }

    public long getTotalActivos() {
        return totalActivos;
    }

    public long getTotalInactivos() {
        return totalInactivos;
    }
}
