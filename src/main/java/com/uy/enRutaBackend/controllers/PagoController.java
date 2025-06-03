package com.uy.enRutaBackend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.icontrollers.IServicePago;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

	private final IServicePago servicePago;
	
}
