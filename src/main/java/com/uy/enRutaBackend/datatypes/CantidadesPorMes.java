package com.uy.enRutaBackend.datatypes;

import java.time.YearMonth;

public record CantidadesPorMes(YearMonth mes, long cantidadActivos, long cantidadInactivos) {
}
