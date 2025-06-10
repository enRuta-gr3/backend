package com.uy.enRutaBackend.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
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

import com.uy.enRutaBackend.datatypes.DtSesion;
import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Administrador;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.PasswordResetToken;
import com.uy.enRutaBackend.entities.Sesion;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;
import com.uy.enRutaBackend.icontrollers.IServiceSesion;
import com.uy.enRutaBackend.icontrollers.IServiceSupabase;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;
import com.uy.enRutaBackend.persistence.AdministradorRepository;
import com.uy.enRutaBackend.persistence.ClienteRepository;
import com.uy.enRutaBackend.persistence.PasswordResetTokenRepository;
import com.uy.enRutaBackend.persistence.SesionRepository;
import com.uy.enRutaBackend.persistence.UsuarioRepository;
import com.uy.enRutaBackend.persistence.VendedorRepository;
import com.uy.enRutaBackend.security.jwt.JwtManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class ServiceUsuario implements IServiceUsuario {

    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final AdministradorRepository administradorRepository;

	private static final Logger log = LoggerFactory.getLogger(ServiceUsuario.class);
	private static final String SUPABASE_URL = "https://zvynuwmrfmktqwhdjpoe.supabase.co";
	private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2eW51d21yZm1rdHF3aGRqcG9lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzMTM3OTMsImV4cCI6MjA1OTg4OTc5M30.T7zfUyRGDl7lctJyJ98TWrp1crjzlkx5VmX-r_x_t4c";

	private final UsuarioRepository repository;
	private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;
    private final IServiceSesion sesionService;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailService emailService;
    private final IServiceSupabase iserviceSupabase;
    private final SesionRepository sesionRepository;

	@Autowired
	public ServiceUsuario(UsuarioRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, JwtManager jwtManager, IServiceSesion sesionService, ClienteRepository clienteRepository,
			VendedorRepository vendedorRepository, AdministradorRepository administradorRepository,
			PasswordResetTokenRepository resetTokenRepository, EmailService emailService, IServiceSupabase iserviceSupabase, SesionRepository sesionRepository) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtManager = jwtManager;
		this.sesionService = sesionService;
		this.clienteRepository = clienteRepository;
		this.vendedorRepository = vendedorRepository;
		this.administradorRepository = administradorRepository;
		this.resetTokenRepository = resetTokenRepository;
		this.emailService = emailService;
		this.iserviceSupabase = iserviceSupabase;
		this.sesionRepository = sesionRepository;
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
	
	public ResultadoOperacion<?> registrarUsuario(DtUsuario usuario) {
	    DtUsuario usuRegistro = new DtUsuario();
	    try {
	        correrValidaciones(usuario);
	        System.out.println("✅ Validaciones pasaron");

	        System.out.println("📥 Email: '" + usuario.getEmail() + "'");
	        System.out.println("📥 CI: '" + usuario.getCi() + "'");

	        boolean emailVacio = (usuario.getEmail() == null || usuario.getEmail().trim().length() == 0);
	        boolean ciVacio = (usuario.getCi() == null || usuario.getCi().trim().length() == 0);

	        System.out.println("📋 Email Vacio: " + emailVacio);
	        System.out.println("📋 CI Vacio: " + ciVacio);

	        if (emailVacio && ciVacio) {
	            System.out.println("❌ Registro rechazado: sin email ni cédula");
	            return new ResultadoOperacion<>(false, "Debe proporcionar al menos una cédula o un correo electrónico.", ErrorCode.DATOS_INSUFICIENTES.name());
	        }

	        if (usuario.getTipo_usuario().equalsIgnoreCase("CLIENTE") && !emailVacio) {
	            usuRegistro = registrarUsuarioSupabase(usuario);
	        } else {
	            usuRegistro = registrarUsuarioSinVerificacion(usuario);
	        }

	        return new ResultadoOperacion<>(true, "Operación realizada con éxito", usuRegistro);

	    } catch (Exception e) {
	        if (e instanceof UsuarioExistenteException) {
	            return new ResultadoOperacion<>(false, ErrorCode.YA_EXISTE.getMsg(), e.getMessage());
	        } else {
	            return new ResultadoOperacion<>(false, ErrorCode.ERROR_DE_CREACION.getMsg(), e.getMessage());
	        }
	    }
	}



	private ResultadoOperacion<?> registrarSinVerificacion(DtUsuario usuario, DtUsuario usuRegistro) {
		try {
			usuRegistro = registrarUsuarioSinVerificacion(usuario);
			return new ResultadoOperacion(true, "Operación realizada con éxito", usuRegistro);
		} catch (Exception e){
			if(e instanceof UsuarioExistenteException) {
				return new ResultadoOperacion(false, ErrorCode.YA_EXISTE.getMsg(), e.getMessage());
			} else {
				return new ResultadoOperacion(false, ErrorCode.ERROR_DE_CREACION.getMsg(), e.getMessage());
			}		
		}
	}

	private DtUsuario registrarUsuarioSupabase(DtUsuario usuario) throws Exception {
		try {
			JSONObject body = completarData(usuario);

			System.out.println("Json a enviar a Supabase: " + body.toString());

			JSONObject respuestaJson = invocarSupabase(body);

			return validarRespuesta(usuario, respuestaJson);
		} catch (Exception e) {
			System.out.println("ERROR registrando usuario: " + e.getMessage());
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
				throw new UsuarioExistenteException("El usuario con email " + usuario.getEmail() + " ya existe.");
			} else {
				throw new Exception(respuestaJson.getString("msg"));
			}
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
		return entityToDtRegistroLogin(usuarioCrear);
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

	public ResultadoOperacion<?> iniciarSesion(DtUsuario request) {
		JSONObject json = new JSONObject();
		
		if (request.getEmail().contains("@") && repository.findByEmail(request.getEmail()) instanceof Cliente) {
			try {
				HttpResponse<String> response = iniciarSesionSupabase(request.getEmail(), request.getContraseña());
				if (response.statusCode() == 200) {
					json = new JSONObject(response.body());
					Usuario solicitante = repository.findByEmail(request.getEmail());
					String tok = json.getString("access_token");
					DtSesion sesion = sesionService.crearSesion(entityToDtRegistroLogin(solicitante), tok);
					
					log.info(sesion.toString());
					return new ResultadoOperacion(true, "Usuario logueado correctamente", sesion);
				} else {
					log.error("Error en login: " + json);
					return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), json);
				}
			} catch (Exception e) {
				log.error("Error en login: " + e.getMessage());
				return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), e);
			}
		} else {
			DtSesion sesion;
			try {
				sesion = authenticate(request);
				if(sesion.getAccess_token() == null || sesion.getAccess_token().isEmpty())
					return new ResultadoOperacion(false, ErrorCode.CREDENCIALES_INVALIDAS.getMsg(), "Usuario o contraseña incorrectos");
				else
					return new ResultadoOperacion(true, "Usuario logueado correctamente", sesion);
			} catch (Exception e) {
				return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), e.getMessage());
			}
		}
	}

	private DtSesion authenticate(DtUsuario request) throws Exception {
		Usuario solicitante;
		DtSesion sesion = new DtSesion();
		if(request.getEmail() != null && request.getEmail().contains("@"))
			solicitante = repository.findByEmail(request.getEmail());
		else
			solicitante = repository.findByCi(request.getEmail());
		
		if(solicitante != null && passwordEncoder.matches(request.getContraseña(), solicitante.getContraseña())) {	
			String tok = jwtManager.generateToken(solicitante);
			sesion = sesionService.crearSesion(entityToDtRegistroLogin(solicitante), tok);		
			log.info(sesion.toString());
		}
			
		return sesion;
	}

	private DtUsuario entityToDtRegistroLogin(Usuario solicitante) {
		return new DtUsuario(definirTipoUsuario(solicitante), solicitante.getUuidAuth(), solicitante.getCi(), 
				solicitante.getNombres(), solicitante.getApellidos(), solicitante.getEmail());
	}

	private String definirTipoUsuario(Usuario solicitante) {
		String tipo = new String();
		if(solicitante instanceof Cliente)
			tipo = "CLIENTE";
		 else if(solicitante instanceof Administrador)
			 tipo = "ADMINISTRADOR";
		 else if(solicitante instanceof Vendedor)
			 tipo = "VENDEDOR";
		return tipo;
		
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

	@Override
	public ResultadoOperacion<?> buscarUsuarioPorCi(DtUsuario usuarioDt) {
		Usuario usuario = new Usuario();
		if(usuarioDt.getTipo_usuario().equalsIgnoreCase("CLIENTE"))
			usuario = clienteRepository.findByCi(usuarioDt.getCi());
		else if(usuarioDt.getTipo_usuario().equalsIgnoreCase("VENDEDOR"))
			usuario = vendedorRepository.findByCi(usuarioDt.getCi());
		else if(usuarioDt.getTipo_usuario().equalsIgnoreCase("ADMINISTRADOR"))
			usuario = administradorRepository.findByCi(usuarioDt.getCi());
		if(usuario != null) {
			DtUsuario dtUsuario = entityToDtPerfil(usuario);
			return new ResultadoOperacion(true, "Usuario obtenido correctamente", dtUsuario);
		} else {
			return new ResultadoOperacion(false, ErrorCode.SIN_RESULTADOS.getMsg(), ErrorCode.SIN_RESULTADOS);
		}
	}

	private DtUsuario entityToDtPerfil(Usuario usuario) {
		return new DtUsuario(definirTipoUsuario(usuario), usuario.getUuidAuth(), usuario.getCi(), 
				usuario.getNombres(), usuario.getApellidos(), usuario.getEmail(), usuario.getFecha_nacimiento());
		
	}
	
	@Override
	public ResultadoOperacion<?> cambiarPassword(DtUsuario datos) {
	    Usuario user = null;
	    String identificador = datos.getEmail(); // puede venir un email o una ci

	    if (identificador == null || identificador.trim().isEmpty()) {
	        return new ResultadoOperacion<>(false, "Debe proporcionar un email o cédula", ErrorCode.DATOS_INSUFICIENTES);
	    }

	    if (identificador.contains("@")) {
	        user = repository.findByEmail(identificador);
	    } else {
	        user = repository.findByCi(identificador);
	    }

	    if (user == null) {
	        return new ResultadoOperacion<>(false, "Credenciales incorrectas", ErrorCode.CREDENCIALES_INVALIDAS);
	    }

	    System.out.println("📥 Contraseña actual (plano): " + datos.getContraseña());
	    System.out.println("🔒 Contraseña guardada (hash): " + user.getContraseña());

	    if (!passwordEncoder.matches(datos.getContraseña(), user.getContraseña())) {
	        return new ResultadoOperacion<>(false, "Credenciales incorrectas", ErrorCode.CREDENCIALES_INVALIDAS);
	    }

	    String nuevaPasswordHash = passwordEncoder.encode(datos.getContraseña_nueva());
	    user.setContraseña(nuevaPasswordHash);
	    repository.save(user);

	    return new ResultadoOperacion<>(true, "Contraseña actualizada correctamente", null);
	}


	
	@Override
	public ResultadoOperacion<?> solicitarRecuperacion(String email) {
	    Usuario user = repository.findByEmail(email);
	    if (user == null) {
	        return new ResultadoOperacion<>(true, "Si el correo existe, se enviará un email con instrucciones", null);
	    }

	    String token = UUID.randomUUID().toString();
	    LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);
	    PasswordResetToken resetToken = new PasswordResetToken(email, token, expiration);
	    resetTokenRepository.save(resetToken);

	    // Enlace personalizado
	    String resetLink = "https://en-ruta.vercel.app/reset-password?token=" + token;
	    emailService.send(email, "Recuperación de contraseña", "Recuperá tu contraseña aquí: " + resetLink);

	    return new ResultadoOperacion<>(true, "Se envió un enlace de recuperación", null);
	}
	
	
	@Override
	public ResultadoOperacion<?> confirmarRecuperacion(String token, String nuevaPassword) {
	    PasswordResetToken resetToken = resetTokenRepository.findByToken(token);

	    if (resetToken == null || resetToken.getExpiration().isBefore(LocalDateTime.now())) {
	        return new ResultadoOperacion<>(false, "Token inválido o expirado", ErrorCode.REQUEST_INVALIDO);
	    }

	    Usuario user = repository.findByEmail(resetToken.getEmail());
	    if (user == null) {
	        return new ResultadoOperacion<>(false, "Usuario no encontrado", ErrorCode.SIN_RESULTADOS);
	    }

	    user.setContraseña(passwordEncoder.encode(nuevaPassword));
	    repository.save(user);
	    resetTokenRepository.delete(resetToken);

	    return new ResultadoOperacion<>(true, "Contraseña actualizada correctamente", null);
	}
	
	@Override
	public ResultadoOperacion<?> eliminarUsuario(String token, DtUsuario datos) {
	    Sesion sesion = sesionRepository.findByAccessToken(token);

	    if (sesion == null || !sesion.isActivo()) {
	        return new ResultadoOperacion<>(false, "Sesión inválida o expirada", ErrorCode.TOKEN_INVALIDO);
	    }

	    Usuario usuarioSesion = sesion.getUsuario();  // usuario que tiene la sesión activa

	    // Determinar qué identificador vino (email o ci)
	    String identificador = datos.getEmail() != null ? datos.getEmail() : datos.getCi();

	    if (identificador == null) {
	        return new ResultadoOperacion<>(false, "Debe especificar email o cédula para eliminar", ErrorCode.DATOS_INSUFICIENTES);
	    }

	    Usuario userAEliminar = identificador.contains("@")
	        ? repository.findByEmail(identificador)
	        : repository.findByCi(identificador);

	    if (userAEliminar == null) {
	        return new ResultadoOperacion<>(false, "Usuario no encontrado", ErrorCode.SIN_RESULTADOS);
	    }

	    // Verificar si el usuario autenticado es el mismo que el que quiere eliminar
	    if (!usuarioSesion.getUuidAuth().equals(userAEliminar.getUuidAuth())) {
	        return new ResultadoOperacion<>(false, "No tiene permisos para eliminar este usuario", ErrorCode.NO_AUTORIZADO);
	    }

	    if (userAEliminar.isEliminado()) {
	        return new ResultadoOperacion<>(false, "El usuario ya está eliminado", ErrorCode.USUARIO_YA_ELIMINADO);
	    }
	    
	    if (userAEliminar.getEmail() != null) {
	        try {
	            iserviceSupabase.eliminarUsuarioPorEmailSQL(userAEliminar.getEmail());
	        } catch (Exception e) {
	            return new ResultadoOperacion<>(false, "Error al eliminar el usuario de Supabase", e.getMessage());
	        }
	    }

	    // Marcar como eliminado
	    userAEliminar.setEliminado(true);
	    userAEliminar.setEmail(null);
	    userAEliminar.setCi(null);
	    
	    repository.save(userAEliminar);
	    return new ResultadoOperacion<>(true, "Usuario eliminado correctamente", null);
	}

}
