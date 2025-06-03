package com.uy.enRutaBackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVentaCompraResponse;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceVenta_Compra;

@RestController
@RequestMapping("api/venta")
public class Venta_CompraController {
	
    @Autowired
    private IServiceVenta_Compra ventaService;

    @PostMapping("/calcularVenta")
    public ResponseEntity<?> calcularVenta(@RequestBody List<DtPasaje> request) {
        DtVentaCompraResponse resultado = ventaService.calcularVenta(request);

        ResultadoOperacion<DtVentaCompraResponse> response = new ResultadoOperacion<>(
            true,
            "Procesado Correctamente",
            resultado
        );

        return ResponseEntity.ok(response);
    }
}
