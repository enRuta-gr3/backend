package com.uy.enRutaBackend.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Buzon_notificacion;
import com.uy.enRutaBackend.entities.Notificacion;

@Repository
public interface NotificacionRepository extends CrudRepository<Notificacion, Integer>{

	List<Notificacion> findByBuzonNotificacion(Buzon_notificacion buzon);

}
