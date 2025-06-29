package com.uy.enRutaBackend.icontrollers;

import java.sql.SQLException;

public interface IServiceSupabase {

	public void eliminarUsuarioPorEmailSQL(String email);
	boolean verificarExistenciaEnSupabase(String email) throws SQLException;
}
