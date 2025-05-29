package com.uy.enRutaBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;

@RestController
public class LocalidadController {
	
	private final IServiceLocalidad localidadService;

	@Autowired
	public LocalidadController(IServiceLocalidad localidadService) {
		this.localidadService = localidadService;
	}

}
