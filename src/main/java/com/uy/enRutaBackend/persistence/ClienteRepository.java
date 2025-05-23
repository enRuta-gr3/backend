package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Cliente;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, UUID>{

}
