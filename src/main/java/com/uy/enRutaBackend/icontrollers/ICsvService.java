package com.uy.enRutaBackend.icontrollers;

import org.springframework.web.multipart.MultipartFile;

import com.uy.enRutaBackend.errors.ResultadoOperacion;

public interface ICsvService {

	ResultadoOperacion<?> cargarArchivo(MultipartFile archivo);

	ResultadoOperacion<?> crearUsuarios();

}
