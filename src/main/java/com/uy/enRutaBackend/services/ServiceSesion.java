package com.uy.enRutaBackend.services;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtSesion;
import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Sesion;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.icontrollers.IServiceSesion;
import com.uy.enRutaBackend.persistence.SesionRepository;
import com.uy.enRutaBackend.persistence.UsuarioRepository;

@Service
public class ServiceSesion implements IServiceSesion{
	
	private final SesionRepository sesionRepository;
	private final UsuarioRepository usuarioRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public ServiceSesion(SesionRepository repository,  UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
		this.sesionRepository = repository;
		this.usuarioRepository = usuarioRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public DtSesion crearSesion(DtUsuario usuario, String token) throws Exception {
		if(!existeToken(token)) {
			DtSesion sesionDt = crearDt(token);
			
			Sesion sesion = dtToEntityCrear(sesionDt);
			agregarUsuarioEnSesion(sesion, usuario);
			sesionRepository.save(sesion);
			
			DtSesion aMostrar = entityToDtMostrar(sesion, usuario);
			
			return aMostrar;
		} else {
			throw new Exception("No se pudo iniciar sesion");
		}
	}

	private DtSesion crearDt(String token) {
		DtSesion sesionDt = new DtSesion();
		sesionDt.setAccess_token(token);
		sesionDt.setActivo(true);
		sesionDt.setFechaInicioSesion(new Date());
		sesionDt.setVigencia(1);
		return sesionDt;
	}

	private void agregarUsuarioEnSesion(Sesion sesion, DtUsuario usuDt) {
		Usuario usu = usuarioRepository.findByCi(usuDt.getCi());
		sesion.setUsuario(usu);		
	}

	private DtSesion entityToDtMostrar(Sesion sesion, DtUsuario usuario) {
		DtSesion sesionDt = new DtSesion();
		sesionDt.setAccess_token(sesion.getAccessToken());
		sesionDt.setUsuario(usuario);
		return sesionDt;
	}

	private Sesion dtToEntityCrear(DtSesion sesion) {
		Sesion aGuardar = modelMapper.map(sesion, Sesion.class);
		aGuardar.setAccessToken(sesion.getAccess_token());
		return aGuardar;
	}

	private boolean existeToken(String token) {
		boolean existe = false;
		Sesion tokenEnBase = sesionRepository.findByAccessToken(token);
		if(tokenEnBase != null) {
			existe = true;
		}
		return existe;
	}

	public String cerrar(Usuario usu, String token) {
		Sesion sesion = sesionRepository.findByUsuarioAndAccessToken(usu, token);
		if(sesion != null && sesion.isActivo()) {
			sesion.setActivo(false);
			sesionRepository.save(sesion);
			return "Sesión cerrada correctamente";
		} else {
			return "La sesión ya fue cerrada";
		}
	}

}
