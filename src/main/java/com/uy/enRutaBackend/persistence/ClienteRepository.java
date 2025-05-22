package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.uy.enRutaBackend.entities.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, UUID>{

}
