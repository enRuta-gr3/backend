package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.uy.enRutaBackend.entities.Administrador;

public interface AdministradorRepository extends CrudRepository<Administrador, UUID>{

}
