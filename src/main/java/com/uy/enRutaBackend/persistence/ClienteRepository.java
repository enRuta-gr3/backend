package com.uy.enRutaBackend.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Usuario;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, UUID>{
	
	Cliente findByUuidAuth(UUID uuidAuth);

	Usuario findByCi(String ci);

}
