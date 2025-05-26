package com.uy.enRutaBackend.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Administrador;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;
import com.uy.enRutaBackend.persistence.UsuarioRepository;
import com.uy.enRutaBackend.security.jwt.JwtManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class ServiceUsuario implements IServiceUsuario {

	private static final Logger log = LoggerFactory.getLogger(ServiceUsuario.class);
	private static final String SUPABASE_URL = "https://zvynuwmrfmktqwhdjpoe.supabase.co";
	private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2eW51d21yZm1rdHF3aGRqcG9lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzMTM3OTMsImV4cCI6MjA1OTg4OTc5M30.T7zfUyRGDl7lctJyJ98TWrp1crjzlkx5VmX-r_x_t4c";

	private final UsuarioRepository repository;
	private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;



	@Autowired
	public ServiceUsuario(UsuarioRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, JwtManager jwtManager) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtManager = jwtManager;
	}
	
	public void correrValidaciones(DtUsuario usuario) throws UsuarioExistenteException {
		if(usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
			boolean existeCorreo = verificarExistenciaCorreo(usuario.getEmail());
			
			if (existeCorreo)
				throw new UsuarioExistenteException("El usuario con email " + usuario.getEmail() + " ya tiene cuenta.");
		}
		
		boolean existeCedula = verificarExistenciaCedula(usuario.getCi());
				
		if (existeCedula)
			throw new UsuarioExistenteException("El usuario con cedula " + usuario.getCi() + " ya tiene cuenta.");
	}

	@Transactional(readOnly = true)
	private boolean verificarExistenciaCedula(String ci) {
		if(repository.findByCi(ci) != null)
			return true;
		else
			return false;
	}

	@Transactional(readOnly = true)
	private boolean verificarExistenciaCorreo(String email) {
		if(repository.findByEmail(email) != null)
			return true;
		else
			return false;
	}

	public DtUsuario registrarUsuario(DtUsuario usuario) throws Exception {
		try {
			JSONObject body = completarData(usuario);

			log.info("Json a enviar a Supabase: " + body.toString());

			JSONObject respuestaJson = invocarSupabase(body);

			return validarRespuesta(usuario, respuestaJson);
		} catch (Exception e) {
			log.info("❌ Error registrando usuario: " + e.getMessage());
			throw e;
		}
	}

	private JSONObject completarData(DtUsuario usuario) {
		JSONObject data = new JSONObject().put("nombres", usuario.getNombres())
				.put("apellidos", usuario.getApellidos()).put("tipo_usuario", usuario.getTipo_usuario())
				.put("fecha_nacimiento", usuario.getFecha_nacimiento()).put("eliminado", usuario.isEliminado())
				.put("ultimo_inicio_sesion", usuario.getUltimo_inicio_sesion())
				.put("fecha_creacion", new Date())
				.put("estado_descuento", usuario.isEstado_descuento()).put("cedula", usuario.getCi())
				.put("es_estudiante", usuario.isEsEstudiante()).put("es_jubilado", usuario.isEsJubilado());

		JSONObject body = new JSONObject().put("email", usuario.getEmail()).put("cedula", usuario.getCi()).put("password", usuario.getContraseña())
				.put("data", data);
		
		return body;
	}
	
	private JSONObject invocarSupabase(JSONObject body) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SUPABASE_URL + "/auth/v1/signup"))
				.header("Content-Type", "application/json").header("apikey", API_KEY)
				.POST(HttpRequest.BodyPublishers.ofString(body.toString())).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String responseBody = response.body();

		log.info("Respuesta Supabase: " + responseBody);

		JSONObject json = new JSONObject(responseBody);
		return json;
	}
	
	private DtUsuario validarRespuesta(DtUsuario usuario, JSONObject respuestaJson)
			throws UsuarioExistenteException, Exception {
		if (respuestaJson.has("user_metadata")) {
			JSONObject user = respuestaJson.getJSONObject("user_metadata");
			usuario.setUuidAuth(UUID.fromString(user.getString("sub")));
			return usuario;
//			JSONObject user = respuestaJson.getJSONObject("user");
//			Usuario registrado = repository.findById(UUID.fromString(user.getString("id"))).orElse(null);
//			Usuario registrado = repository.findById(UUID.fromString(user.getString("sub"))).orElse(null);
//			return entityToDtAMostrar(registrado);
			
		} else if (respuestaJson.has("msg") && respuestaJson.getString("msg").contains("already registered")) {
			UUID idExistente = buscarUUIDPorEmail(usuario.getEmail());
			
			if (idExistente != null) {
				log.info("El usuario con email " + usuario.getEmail() + " ya existe.");
				log.info("Su UUID es  " + idExistente);
			}
			throw new UsuarioExistenteException("El usuario con email " + usuario.getEmail() + " ya existe.");
		} else {
			throw new Exception(respuestaJson.getString("msg"));
		}
	}
	
	
	@Override
	@Transactional
	public DtUsuario registrarUsuarioSinVerificacion(DtUsuario usuario) throws Exception {
		try {
			agregarUUID(usuario);
		} catch (UsuarioExistenteException e) {
			throw e;
		}
		agregarFechaCreacion(usuario);
		agregarContraseña(usuario);
		verificarDescuento(usuario);
		
		Usuario usuarioCrear = dtToEntity(usuario);

		repository.save(usuarioCrear);
		return entityToDtAMostrar(usuarioCrear);
	}

	private void verificarDescuento(DtUsuario usuario) {
		if(usuario.getTipo_usuario().equalsIgnoreCase("CLIENTE"))				
			usuario.setEstado_descuento(true);		
	}

	private void agregarUUID(DtUsuario usuario) throws UsuarioExistenteException {
		UUID uuidKey = UUID.randomUUID();
		log.info(uuidKey.toString());
		usuario.setUuidAuth(uuidKey);
	}

	private void agregarFechaCreacion(DtUsuario usuario) {
		usuario.setFecha_creacion(new Date());		
	}

	private void agregarContraseña(DtUsuario usuario) {
		if(usuario.getContraseña() == null || usuario.getContraseña().isEmpty())
			usuario.setContraseña(usuario.getCi());
		usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
		log.info(usuario.getContraseña());
	}
	
	@Transactional(readOnly = true)
	private Usuario buscarPorUUID(UUID uuidKey) {
		return repository.findById(uuidKey).orElse(null);
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

	public JSONObject iniciarSesion(DtUsuario request) {
		JSONObject json = new JSONObject();
		if (request.getEmail().contains("@") && repository.findByEmail(request.getEmail()) instanceof Cliente) {
			try {
				HttpResponse<String> response = iniciarSesionSupabase(request.getEmail(), request.getContraseña());
				if (response.statusCode() == 200) {
					json = new JSONObject(response.body());
					log.info(json.toString());
				} else {
					log.error("Error en login: " + json.toString());
				}

			} catch (Exception e) {
				log.error("Error en login: " + e.getMessage());
			}
		} else {
			String tok = authenticate(request);
			json = new JSONObject("{ \"access_token\":" + tok + "}");
		}

		return json;
	}

	private String authenticate(DtUsuario request) {		
		Usuario solicitante;
		if(request.getEmail() != null && request.getEmail().contains("@"))
			solicitante = repository.findByEmail(request.getEmail());
		else
			solicitante = repository.findByCi(request.getEmail());
		
		if(solicitante == null || !passwordEncoder.matches(request.getContraseña(), solicitante.getContraseña())) {
			throw new RuntimeException("Credenciales inválidas");
		}
		return jwtManager.generateToken(solicitante);
	}

	private HttpResponse<String> iniciarSesionSupabase(String email, String password)
			throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();

		JSONObject body = new JSONObject().put("email", email).put("password", password);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(SUPABASE_URL + "/auth/v1/token?grant_type=password"))
				.header("Content-Type", "application/json").header("apikey", API_KEY)
				.POST(HttpRequest.BodyPublishers.ofString(body.toString())).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response;
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
		if(usuario.getTipo_usuario().equalsIgnoreCase("CLIENTE"))
			return modelMapper.map(usuario, Cliente.class);
		else if(usuario.getTipo_usuario().equalsIgnoreCase("ADMINISTRADOR"))
			return modelMapper.map(usuario, Administrador.class);
		else if(usuario.getTipo_usuario().equalsIgnoreCase("VENDEDOR"))
			return modelMapper.map(usuario, Vendedor.class);
		return modelMapper.map(usuario, Usuario.class);
	}

	

}
