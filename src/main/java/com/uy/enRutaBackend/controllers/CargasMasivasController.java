package com.uy.enRutaBackend.controllers;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.ICsvService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/cargasMasivas")
public class CargasMasivasController {
	
	private final ICsvService csvService;

	@Autowired
	public CargasMasivasController(ICsvService csvService) {
		this.csvService = csvService;
	}

	@PostMapping(value = "/cargarArchivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(
	    summary = "Subir un archivo CSV",
	    description = "Endpoint para cargar archivos CSV usando multipart/form-data",
	    requestBody = @RequestBody(
	        content = @Content(
	            mediaType = "multipart/form-data",
	            schema = @Schema(name = "file", type = "string", format = "binary")
	            ,
	            encoding = @Encoding(name = "file", contentType = "application/octet-stream")
	        )
	    )
	)
	public ResponseEntity<?> cargarArchivo(@RequestParam("file") MultipartFile archivo) {
		ResultadoOperacion<?> res = csvService.cargarArchivo(archivo);
		if(res.isSuccess()) {
			return ResponseEntity.ok(res);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
	
	@GetMapping("/crearUsuarios")
	@Operation(summary = "Realizar alta masiva de usuarios desde archivo csv")
    public ResponseEntity<?> crearUsuarios() {
		ResultadoOperacion<?> res = csvService.crearUsuarios();
		if(res.isSuccess()) {
			return ResponseEntity.ok(res);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
	}
}
