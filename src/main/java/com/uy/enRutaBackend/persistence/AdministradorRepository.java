package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Administrador;

@Repository
public interface AdministradorRepository extends CrudRepository<Administrador, UUID>{

}
