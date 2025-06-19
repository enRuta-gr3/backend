package com.uy.enRutaBackend.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import com.uy.enRutaBackend.datatypes.DtResultadoCargaMasiva;
import com.uy.enRutaBackend.datatypes.DtSesion;
import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.datatypes.DtUsuarioCargaMasiva;
import com.uy.enRutaBackend.entities.Administrador;
import com.uy.enRutaBackend.entities.Buzon_notificacion;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.PasswordResetToken;
import com.uy.enRutaBackend.entities.Sesion;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.exceptions.UsuarioExistenteException;
import com.uy.enRutaBackend.exceptions.UsuarioNoExisteException;
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
import com.uy.enRutaBackend.utils.UtilsClass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class ServiceUsuario implements IServiceUsuario {

    private final UtilsClass utilsClass;

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
	public ServiceUsuario(UsuarioRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
			JwtManager jwtManager, IServiceSesion sesionService, ClienteRepository clienteRepository,
			VendedorRepository vendedorRepository, AdministradorRepository administradorRepository,
			PasswordResetTokenRepository resetTokenRepository, EmailService emailService,
			IServiceSupabase iserviceSupabase, SesionRepository sesionRepository, UtilsClass utilsClass) {
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
		this.utilsClass = utilsClass;
	}

	public void correrValidaciones(DtUsuario usuario) throws UsuarioExistenteException {
		if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
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
		if (repository.findByCi(ci) != null)
			return true;
		else
			return false;
	}

	@Transactional(readOnly = true)
	private boolean verificarExistenciaCorreo(String email) {
		if (repository.findByEmail(email) != null)
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
				return new ResultadoOperacion<>(false, "Debe proporcionar al menos una cédula o un correo electrónico.",
						ErrorCode.DATOS_INSUFICIENTES.name());
			}

			if (usuario.getTipo_usuario().equalsIgnoreCase("CLIENTE") && !emailVacio) {
				usuRegistro = registrarUsuarioSupabase(usuario);
				
				agregarContraseña(usuRegistro);
				Usuario usuarioset = dtToEntity(usuRegistro);
				
			    Buzon_notificacion buzon = new Buzon_notificacion();
			    buzon.setUsuario(usuarioset);
			    usuarioset.setNotificaciones(buzon);
			    agregarContraseña(usuario);
			    repository.save(usuarioset);
			    
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
		} catch (Exception e) {
			if (e instanceof UsuarioExistenteException) {
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
		JSONObject data = new JSONObject().put("nombres", usuario.getNombres()).put("apellidos", usuario.getApellidos())
				.put("tipo_usuario", usuario.getTipo_usuario()).put("fecha_nacimiento", usuario.getFecha_nacimiento())
				.put("eliminado", usuario.isEliminado()).put("ultimo_inicio_sesion", usuario.getUltimo_inicio_sesion())
				.put("fecha_creacion", new Date()).put("estado_descuento", usuario.isEstado_descuento())
				.put("cedula", usuario.getCi()).put("es_estudiante", usuario.isEsEstudiante())
				.put("es_jubilado", usuario.isEsJubilado());

		JSONObject body = new JSONObject().put("email", usuario.getEmail()).put("cedula", usuario.getCi())
				.put("password", usuario.getContraseña()).put("data", data);

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

	    Buzon_notificacion buzon = new Buzon_notificacion();
	    buzon.setUsuario(usuarioCrear);
	    usuarioCrear.setNotificaciones(buzon);

		repository.save(usuarioCrear);
		return entityToDtRegistroLogin(usuarioCrear);
	}

	private void verificarDescuento(DtUsuario usuario) {
		if (usuario.getTipo_usuario().equalsIgnoreCase("CLIENTE"))
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
		if (usuario.getContraseña() == null || usuario.getContraseña().isEmpty())
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
		/*JSONObject json = new JSONObject();

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
		} else {*/
			DtSesion sesion;
			try {
				sesion = authenticate(request);
				if (sesion.getAccess_token() == null || sesion.getAccess_token().isEmpty())
					return new ResultadoOperacion(false, ErrorCode.CREDENCIALES_INVALIDAS.getMsg(),
							"Usuario o contraseña incorrectos");
				else
					return new ResultadoOperacion(true, "Usuario logueado correctamente", sesion);
			} catch (Exception e) {
				return new ResultadoOperacion(false, ErrorCode.CREDENCIALES_INVALIDAS.getMsg(), e.getMessage());
			}
		//}
	}

	private DtSesion authenticate(DtUsuario request) throws Exception {
		Usuario solicitante;
		DtSesion sesion = new DtSesion();
		if (request.getEmail() != null && request.getEmail().contains("@"))
			solicitante = repository.findByEmail(request.getEmail());
		else
			solicitante = repository.findByCi(request.getEmail());

		if(solicitante.isEliminado()) {
			throw new UsuarioNoExisteException("El usuario ha sido eliminado.");
		}
		
		if (solicitante != null && passwordEncoder.matches(request.getContraseña(), solicitante.getContraseña())) {
			String tok = jwtManager.generateToken(solicitante);
			sesion = sesionService.crearSesion(entityToDtRegistroLogin(solicitante), tok);
			
			log.info(sesion.toString());
			
			actualizarUltimoInicio(solicitante);
		}

		return sesion;
	}

	private void actualizarUltimoInicio(Usuario solicitante) {
		solicitante.setUltimo_inicio_sesion(new Date());
		repository.save(solicitante);
	}

	private DtUsuario entityToDtRegistroLogin(Usuario solicitante) {
		return new DtUsuario(definirTipoUsuario(solicitante), solicitante.getUuidAuth(), solicitante.getCi(),
				solicitante.getNombres(), solicitante.getApellidos(), solicitante.getEmail(), solicitante.getFecha_nacimiento());
	}

	private String definirTipoUsuario(Usuario solicitante) {
		String tipo = new String();
		if (solicitante instanceof Cliente)
			tipo = "CLIENTE";
		else if (solicitante instanceof Administrador)
			tipo = "ADMINISTRADOR";
		else if (solicitante instanceof Vendedor)
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
				.addMappings(mapper -> mapper.skip(DtUsuario::setContraseña)); // ver que mas habria q esconder.
		return modelMapper.map(usu, DtUsuario.class);
	}

	private Usuario dtToEntity(DtUsuario usuario) {
		if (usuario.getTipo_usuario().equalsIgnoreCase("CLIENTE"))
			return modelMapper.map(usuario, Cliente.class);
		else if (usuario.getTipo_usuario().equalsIgnoreCase("ADMINISTRADOR"))
			return modelMapper.map(usuario, Administrador.class);
		else if (usuario.getTipo_usuario().equalsIgnoreCase("VENDEDOR"))
			return modelMapper.map(usuario, Vendedor.class);
		return modelMapper.map(usuario, Usuario.class);
	}

	@Override
	public ResultadoOperacion<?> buscarUsuarioPorCi(DtUsuario usuarioDt) {
		Usuario usuario = new Usuario();
		if (usuarioDt.getTipo_usuario().equalsIgnoreCase("CLIENTE"))
			usuario = clienteRepository.findByCi(usuarioDt.getCi());
		else if (usuarioDt.getTipo_usuario().equalsIgnoreCase("VENDEDOR"))
			usuario = vendedorRepository.findByCi(usuarioDt.getCi());
		else if (usuarioDt.getTipo_usuario().equalsIgnoreCase("ADMINISTRADOR"))
			usuario = administradorRepository.findByCi(usuarioDt.getCi());
		if (usuario != null) {
			DtUsuario dtUsuario = entityToDtPerfil(usuario);
			return new ResultadoOperacion(true, "Usuario obtenido correctamente", dtUsuario);
		} else {
			return new ResultadoOperacion(false, ErrorCode.SIN_RESULTADOS.getMsg(), ErrorCode.SIN_RESULTADOS);
		}
	}

	private DtUsuario entityToDtPerfil(Usuario usuario) {
		return new DtUsuario(definirTipoUsuario(usuario), usuario.getUuidAuth(), usuario.getCi(), usuario.getNombres(),
				usuario.getApellidos(), usuario.getEmail(), usuario.getFecha_nacimiento());

	}

	@Override
	public ResultadoOperacion<?> cambiarPassword(DtUsuario datos) {
		Usuario user = null;
		String identificador = datos.getEmail(); // puede venir un email o una ci

		if (identificador == null || identificador.trim().isEmpty()) {
			return new ResultadoOperacion<>(false, "Debe proporcionar un email o cédula",
					ErrorCode.DATOS_INSUFICIENTES);
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
	    emailService.send(
	    	    email,
	    	    "Recuperación de contraseña",
	    	    "Recuperá tu contraseña aquí: " + resetLink + "\n\nEste enlace es válido por 30 minutos."
	    	);

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

		Usuario usuarioSesion = sesion.getUsuario(); // usuario que tiene la sesión activa

		// Determinar qué identificador vino (email o ci)
		String identificador = datos.getEmail() != null ? datos.getEmail() : datos.getCi();

		if (identificador == null) {
			return new ResultadoOperacion<>(false, "Debe especificar email o cédula para eliminar",
					ErrorCode.DATOS_INSUFICIENTES);
		}

		Usuario userAEliminar = identificador.contains("@") ? repository.findByEmail(identificador)
				: repository.findByCi(identificador);

		if (userAEliminar == null) {
			return new ResultadoOperacion<>(false, "Usuario no encontrado", ErrorCode.SIN_RESULTADOS);
		}

		// Verificar si el usuario autenticado es el mismo que el que quiere eliminar
		if (!usuarioSesion.getUuidAuth().equals(userAEliminar.getUuidAuth())) {
			return new ResultadoOperacion<>(false, "No tiene permisos para eliminar este usuario",
					ErrorCode.NO_AUTORIZADO);
		}

		if (userAEliminar.isEliminado()) {
			return new ResultadoOperacion<>(false, "El usuario ya está eliminado", ErrorCode.USUARIO_YA_ELIMINADO);
		}

		try {
			eliminarDeSupabase(userAEliminar);
		} catch (Exception e) {
			return new ResultadoOperacion<>(false, "Error al eliminar el usuario de Supabase", e.getMessage());
		}

		eliminarUsuario(userAEliminar);
		return new ResultadoOperacion<>(true, "Usuario eliminado correctamente", null);
	}

	/**
	 * @param userAEliminar
	 */
	private void eliminarDeSupabase(Usuario userAEliminar) throws Exception {
		if (userAEliminar.getEmail() != null) {
			try {
				iserviceSupabase.eliminarUsuarioPorEmailSQL(userAEliminar.getEmail());
			} catch (Exception e) {
				throw e;
			}
		}
	}

	/**
	 * @param userAEliminar
	 */
	private void eliminarUsuario(Usuario userAEliminar) {
		// Marcar como eliminado
		userAEliminar.setEliminado(true);
		userAEliminar.setEmail(null);
		userAEliminar.setCi(null);

		repository.save(userAEliminar);
	}

	@Override
	public ResultadoOperacion<?> modificarPerfil(DtUsuario usuario) {
		Usuario aModificar = repository.findById(usuario.getUuidAuth()).get();
		if(!usuario.getEmail().equals(aModificar.getEmail())) {
			Usuario tieneEmail = repository.findByEmail(usuario.getEmail());
			if(tieneEmail != null) {
				return new ResultadoOperacion(false, "La dirección de correo ya fue registrada", ErrorCode.YA_EXISTE);
			}
		}
		Usuario modificado = new Usuario();
		if (aModificar instanceof Cliente) {
			modificado = actualizarCliente(aModificar, usuario);
		} else if (aModificar instanceof Vendedor) {
			modificado = actualizarVendedor(aModificar, usuario);
		} else if (aModificar instanceof Administrador) {
			modificado = actualizarAdministrador(aModificar, usuario);
		}

		DtUsuario usuDt = new DtUsuario();
		if (modificado != null) {
			usuDt.setApellidos(modificado.getApellidos());
			usuDt.setNombres(modificado.getNombres());
			usuDt.setCi(modificado.getCi());
			usuDt.setEmail(modificado.getEmail());
			usuDt.setFecha_nacimiento(modificado.getFecha_nacimiento());

			return new ResultadoOperacion(true, "Datos actualizados correctamente", usuDt);
		} else {
			return new ResultadoOperacion(false, ErrorCode.SIN_RESULTADOS.getMsg(), ErrorCode.SIN_RESULTADOS);
		}
	}

	private Administrador actualizarAdministrador(Usuario aModificar, DtUsuario usuario) {
		Administrador a = (Administrador) aModificar;
		a.setEmail(usuario.getEmail());
		a.setNombres(usuario.getNombres());
		a.setApellidos(usuario.getApellidos());
		a.setFecha_nacimiento(usuario.getFecha_nacimiento());
		a.setFecha_creacion(aModificar.getFecha_creacion());
		return administradorRepository.save(a);
	}

	private Vendedor actualizarVendedor(Usuario aModificar, DtUsuario usuario) {
		Vendedor v = (Vendedor) aModificar;
		v.setEmail(usuario.getEmail());
		v.setNombres(usuario.getNombres());
		v.setApellidos(usuario.getApellidos());
		v.setFecha_nacimiento(usuario.getFecha_nacimiento());
		v.setFecha_creacion(aModificar.getFecha_creacion());
		return vendedorRepository.save(v);
	}

	private Cliente actualizarCliente(Usuario aModificar, DtUsuario usuario) {
		Cliente c = (Cliente) aModificar;
		c.setEmail(usuario.getEmail());
		c.setNombres(usuario.getNombres());
		c.setApellidos(usuario.getApellidos());
		c.setFecha_nacimiento(usuario.getFecha_nacimiento());
		c.setFecha_creacion(aModificar.getFecha_creacion());
		return clienteRepository.save(c);
	}

	@Override
	public ResultadoOperacion<?> listarUsuarios() {
		List<DtUsuario> usuariosDtList = new ArrayList<DtUsuario>();
		List<Usuario> usuariosList = repository.findAll();
		
		for(Usuario u : usuariosList) {
			if(!u.isEliminado()) {
				DtUsuario usuarioDt = new DtUsuario();
				if(u instanceof Cliente) {
					Cliente c = (Cliente) u;
					usuarioDt.setTipo_usuario("CLIENTE");
					usuarioDt.setNombres(c.getNombres());
					usuarioDt.setApellidos(c.getApellidos());
					usuarioDt.setEmail(c.getEmail());
					usuarioDt.setEliminado(c.isEliminado());
					usuarioDt.setUltimo_inicio_sesion(c.getUltimo_inicio_sesion());
					usuarioDt.setFecha_creacion(c.getFecha_creacion());
					usuarioDt.setCi(c.getCi());
					usuarioDt.setEsEstudiante(c.isEsEstudiante());
					usuarioDt.setEsJubilado(c.isEsJubilado());
					usuarioDt.setEstado_descuento(c.isEstado_descuento());
				} else if (u instanceof Vendedor) {
					Vendedor v = (Vendedor) u;
					usuarioDt.setTipo_usuario("VENDEDOR");
					usuarioDt.setNombres(v.getNombres());
					usuarioDt.setApellidos(v.getApellidos());
					usuarioDt.setEmail(v.getEmail());
					usuarioDt.setEliminado(v.isEliminado());
					usuarioDt.setFecha_creacion(v.getFecha_creacion());
					usuarioDt.setUltimo_inicio_sesion(v.getUltimo_inicio_sesion());
					usuarioDt.setCi(v.getCi());
				} else if (u instanceof Administrador) {
					Administrador a = (Administrador) u;
					usuarioDt.setTipo_usuario("ADMINISTRADOR");
					usuarioDt.setNombres(a.getNombres());
					usuarioDt.setApellidos(a.getApellidos());
					usuarioDt.setEmail(a.getEmail());
					usuarioDt.setFecha_creacion(a.getFecha_creacion());
					usuarioDt.setEliminado(a.isEliminado());
					usuarioDt.setUltimo_inicio_sesion(a.getUltimo_inicio_sesion());
					usuarioDt.setCi(a.getCi());
				}
				usuariosDtList.add(usuarioDt);
			}
		}
		if(!usuariosDtList.isEmpty()) {
			return new ResultadoOperacion(true, "Usuarios listados correctamente", usuariosDtList);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}

	@Override
	public DtResultadoCargaMasiva procesarUsuarios(List<DtUsuarioCargaMasiva> leidosCsv) throws Exception{
		DtResultadoCargaMasiva resultadoCargaMasiva = new DtResultadoCargaMasiva();
		for (DtUsuarioCargaMasiva usuarioLeido : leidosCsv) {
			resultadoCargaMasiva.setTotalLineasARegistrar(resultadoCargaMasiva.getTotalLineasARegistrar()+1);
			DtUsuario usuarioDt = crearDtRegistro(usuarioLeido);
			try {
				DtUsuario registrado = registrarUsuarioSinVerificacion(usuarioDt);
				if(registrado != null) {
					utilsClass.actualizarResultado(resultadoCargaMasiva, "ok", registrado);					
				} else {
					utilsClass.actualizarResultado(resultadoCargaMasiva, "error", null);					
				}
			} catch (Exception e) {
				utilsClass.actualizarResultado(resultadoCargaMasiva, "error", null);	
				System.out.println("No se pudo procesar el usuario" + usuarioDt.toString());
			}	
		}
		return resultadoCargaMasiva;
	}


	

	private DtUsuario crearDtRegistro(DtUsuarioCargaMasiva usuarioLeido) {
		DtUsuario usuDt = new DtUsuario();
		usuDt.setTipo_usuario(usuarioLeido.getTipoUsuario());
		usuDt.setCi(usuarioLeido.getCedula());
		usuDt.setApellidos(usuarioLeido.getApellidos());
		usuDt.setNombres(usuarioLeido.getNombres());
		usuDt.setEmail(usuarioLeido.getEmail());
		usuDt.setFecha_nacimiento(java.sql.Date.valueOf(usuarioLeido.getFechaNacimiento()));
		return usuDt;
	}
}
