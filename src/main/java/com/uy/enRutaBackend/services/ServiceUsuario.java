package com.uy.enRutaBackend.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;
import com.uy.enRutaBackend.persistence.UsuarioRepository;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class ServiceUsuario implements IServiceUsuario {

	private static final Logger log = LoggerFactory.getLogger(ServiceUsuario.class);
	private static final String SUPABASE_URL = "https://zvynuwmrfmktqwhdjpoe.supabase.co";
	private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2eW51d21yZm1rdHF3aGRqcG9lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzMTM3OTMsImV4cCI6MjA1OTg4OTc5M30.T7zfUyRGDl7lctJyJ98TWrp1crjzlkx5VmX-r_x_t4c";

	private final UsuarioRepository repository;
	private final ModelMapper modelMapper;


	@Autowired
	public ServiceUsuario(UsuarioRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
		this.modelMapper = modelMapper;
	}

	public DtUsuario registrarUsuario(DtUsuario usuario) throws Exception {
		try {
			HttpClient client = HttpClient.newHttpClient();
			
			JSONObject body = completarData(usuario);

			if(body.has("email")) {
				boolean existeCorreo = verificarExistenciaCorreo(body.getString("email"));
				if(existeCorreo)
					throw new UsuarioExistenteException("El usuario con email " + usuario.getEmail() + " ya tiene cuenta.");
			}
			
			boolean existeCedula = verificarExistenciaCedula(body.getString("cedula"));
				
			if(existeCedula)
				throw new UsuarioExistenteException("El usuario con cedula " + usuario.getCi() + " ya tiene cuenta.");
				
			log.info("Json a enviar a Supabase: " + body.toString());

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SUPABASE_URL + "/auth/v1/signup"))
					.header("Content-Type", "application/json").header("apikey", API_KEY)
					.POST(HttpRequest.BodyPublishers.ofString(body.toString())).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String responseBody = response.body();

			log.info("Respuesta Supabase: " + responseBody);

			JSONObject json = new JSONObject(responseBody);

			if (json.has("user")) {
				JSONObject user = json.getJSONObject("user");
				Usuario registrado = repository.findById(UUID.fromString(user.getString("id"))).orElse(null);
				return entityToDtAMostrar(registrado);
				
			} else if (json.has("msg") && json.getString("msg").contains("already registered")) {
				UUID idExistente = buscarUUIDPorEmail(usuario.getEmail());
				
				if (idExistente != null) {
					log.info("El usuario con email " + usuario.getEmail() + " ya existe.");
					log.info("Su UUID es  " + idExistente);
				}
				throw new UsuarioExistenteException("El usuario con email " + usuario.getEmail() + " ya existe.");
			} else {
				throw new Exception(json.getString("msg"));
			}
		} catch (Exception e) {
			log.info("❌ Error registrando usuario: " + e.getMessage());
			throw e;
		}
	}

	private boolean verificarExistenciaCedula(String ci) {
		if(repository.findByCi(ci) != null)
			return true;
		else
			return false;
	}

	private boolean verificarExistenciaCorreo(String email) {
		if(repository.findByEmail(email) != null)
			return true;
		else
			return false;
	}

	private JSONObject completarData(DtUsuario usuario) {
		JSONObject data = new JSONObject().put("nombres", usuario.getNombres())
				.put("apellidos", usuario.getApellidos()).put("tipo_usuario", usuario.getTipo_usuario())
				.put("fecha_nacimiento", usuario.getFecha_nacimiento()).put("eliminado", usuario.isEliminado())
				.put("ultimo_inicio_sesion", usuario.getUltimo_inicio_sesion())
				.put("fecha_creacion", usuario.getFecha_creacion())
				.put("estado_descuento", usuario.isEstado_descuento()).put("cedula", usuario.getCi())
				.put("es_estudiante", usuario.isEsEstudiante()).put("es_jubilado", usuario.isEsJubilado());

		JSONObject body = new JSONObject().put("email", usuario.getEmail()).put("cedula", usuario.getCi()).put("password", usuario.getContraseña())
				.put("data", data);
		
		return body;
	}

	public UUID buscarUUIDPorEmail(String email) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(SUPABASE_URL + "/auth/v1/admin/users?email=" + email))
					.header("Authorization", "Bearer " + API_KEY).header("apikey", API_KEY).GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				JSONArray data = new JSONArray(response.body());
				if (!data.isEmpty()) {
					JSONObject user = data.getJSONObject(0);
					return UUID.fromString(user.getString("id"));
				}
			} else {
				System.out.println("❌ Error buscando UUID: " + response.body());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject login(String email, String password) {

		try {
			HttpClient client = HttpClient.newHttpClient();

			JSONObject body = new JSONObject().put("email", email).put("password", password);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(SUPABASE_URL + "/auth/v1/token?grant_type=password"))
					.header("Content-Type", "application/json").header("apikey", API_KEY)
					.POST(HttpRequest.BodyPublishers.ofString(body.toString())).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			JSONObject json = new JSONObject(response.body());

			if (response.statusCode() == 200) {
				return json;
			} else {
				System.out.println("❌ Error en login: " + json.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private DtUsuario entityToDt(Usuario usu) {
		return modelMapper.map(usu, DtUsuario.class);
	}
	
	private DtUsuario entityToDtAMostrar(Usuario usu) {
		 ModelMapper modelMapper = new ModelMapper();
		 modelMapper.typeMap(Usuario.class, DtUsuario.class)
		            .addMappings(mapper -> mapper.skip(DtUsuario::setContraseña)); //ver que mas habria q esconder.
		 return modelMapper.map(usu, DtUsuario.class);
	}
	
	private Usuario dtToEntity(DtUsuario usuario) {
		return modelMapper.map(usuario, Usuario.class);
	}

}
