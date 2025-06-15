package com.uy.enRutaBackend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.ICsvService;

@Service
public class CsvService implements ICsvService {
	
	private static final String DIRECTORIO_CARGA = System.getProperty("user.dir") + "/cargas";

	@Override
	public ResultadoOperacion<?> cargarArchivo(MultipartFile archivo) {
		try {
		Files.createDirectories(Paths.get(DIRECTORIO_CARGA));
		
		Path rutaArchivo = Paths.get(DIRECTORIO_CARGA, archivo.getOriginalFilename());
		Files.write(rutaArchivo, archivo.getBytes(), StandardOpenOption.CREATE);

		return new ResultadoOperacion(true, "Archivo cargado correctamente.", rutaArchivo.toString());
		} catch (IOException e) {
			return new ResultadoOperacion(false, "Error al guardar el archivo", ErrorCode.ERROR_PROCESANDO_CARGA);
		}
	}

}
