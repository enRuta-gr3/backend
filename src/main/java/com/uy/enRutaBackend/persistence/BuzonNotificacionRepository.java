package com.uy.enRutaBackend.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Buzon_notificacion;

@Repository
public interface BuzonNotificacionRepository extends CrudRepository<Buzon_notificacion, Integer>{

}
