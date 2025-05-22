package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.uy.enRutaBackend.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, UUID>{

}
