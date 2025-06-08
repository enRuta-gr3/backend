package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.entities.Vendedor;

@Repository
public interface VendedorRepository extends CrudRepository<Vendedor, UUID>{

	Usuario findByCi(String ci);

}
