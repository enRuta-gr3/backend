package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Notificacion;

@Repository
public interface NotificacionRepository extends CrudRepository<Notificacion, Integer> {

}
