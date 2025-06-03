package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{
	Usuario findByEmail(String email);
	Usuario findByCi(String ci);
	Usuario findByEmailOrCi(String email, String ci);
}
