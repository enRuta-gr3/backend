package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.uy.enRutaBackend.entities.Vendedor;

public interface VendedorRepository extends CrudRepository<Vendedor, UUID>{

}
