package com.uy.enRutaBackend.icontrollers;

import java.util.UUID;

import org.json.JSONObject;

import com.uy.enRutaBackend.datatypes.usuarioDTO;

public interface IServiceUsuario {
	
	 public UUID registrarUsuario(usuarioDTO usuario);
	 public UUID buscarUUIDPorEmail(String email);
	 public JSONObject login(String email, String password);
	 
}

