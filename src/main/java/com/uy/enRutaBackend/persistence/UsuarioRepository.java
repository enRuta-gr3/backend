package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, UUID>{
	Usuario findByEmail(String email);
	Usuario findByCi(String ci);
}
