package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Departamento;

@Repository
public interface DepartamentoRepository extends CrudRepository<Departamento, Integer>{

	Departamento findByNombre(String nombreDepartamento);

}
