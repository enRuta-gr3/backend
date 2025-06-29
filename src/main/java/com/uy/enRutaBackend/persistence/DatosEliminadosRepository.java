package com.uy.enRutaBackend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uy.enRutaBackend.entities.DatosEliminados;

public interface DatosEliminadosRepository extends JpaRepository<DatosEliminados, Long> {
}