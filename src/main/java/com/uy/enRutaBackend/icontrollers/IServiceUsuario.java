package com.uy.enRutaBackend.icontrollers;

import java.util.UUID;

import org.json.JSONObject;

import com.uy.enRutaBackend.datatypes.DtUsuario;

public interface IServiceUsuario {
	
	 public DtUsuario registrarUsuario(DtUsuario usuario) throws Exception;
	 public UUID buscarUUIDPorEmail(String email);
	 public JSONObject login(String email, String password);	 
}

