package com.uy.enRutaBackend.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBeanBuilder;
import com.uy.enRutaBackend.datatypes.DtResultadoCargaMasiva;
import com.uy.enRutaBackend.datatypes.DtUsuarioCargaMasiva;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.ICsvService;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;

@Service
public class CsvService implements ICsvService {
	
	private static final String DIRECTORIO_CARGA = System.getProperty("user.dir") + "/cargas";
	private final IServiceUsuario serviceUsuario;
	
	public CsvService(IServiceUsuario serviceUsuario) {
		this.serviceUsuario = serviceUsuario;
	}

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

	@Override
	public ResultadoOperacion<?> crearUsuarios() {
		List<DtUsuarioCargaMasiva> leidosCsv = new ArrayList<DtUsuarioCargaMasiva>();
		DtResultadoCargaMasiva resultado = new DtResultadoCargaMasiva();
		try {
			leidosCsv = leerCsvUsuario(DIRECTORIO_CARGA + "/usuarios.csv");
			resultado = serviceUsuario.procesarUsuarios(leidosCsv);
			if(resultado != null) {
				System.out.println(" *CARGA MASIVA USUARIOS* - Carga exitosa");
				return new ResultadoOperacion(true, "Usuarios procesados correctamente.", resultado);				
			} else {
				System.out.println(" *CARGA MASIVA USUARIOS* - Error procesando usuarios.");
				return new ResultadoOperacion(false, "No se procesaron los usuarios, verifique los datos", ErrorCode.ERROR_DE_CREACION);
			}
		} catch (IllegalStateException e) {
			System.out.println(" *CARGA MASIVA USUARIOS* - Error procesando usuarios. " + e.getMessage());
			return new ResultadoOperacion(false, "Error procesando usuarios. " + e.getMessage(), ErrorCode.ERROR_DE_CREACION);
		} catch (FileNotFoundException e) {
			System.out.println(" *CARGA MASIVA USUARIOS* - Error procesando usuarios. " + e.getMessage());
			return new ResultadoOperacion(false, "Error procesando usuarios. " + e.getMessage(), ErrorCode.ERROR_DE_CREACION);
		} catch (Exception e) {
			System.out.println(" *CARGA MASIVA USUARIOS* - Error procesando usuarios. " + e.getMessage());
			return new ResultadoOperacion(false, "Error procesando usuarios. " + e.getMessage(), ErrorCode.ERROR_DE_CREACION);
		}
	}

	private List<DtUsuarioCargaMasiva> leerCsvUsuario(String archivo) throws IllegalStateException, FileNotFoundException {
		List<DtUsuarioCargaMasiva> usuarios = new CsvToBeanBuilder<DtUsuarioCargaMasiva>(new FileReader(archivo))
				.withType(DtUsuarioCargaMasiva.class)
				.withIgnoreLeadingWhiteSpace(true)
				.build().parse();
		return usuarios;
	}
	
}
