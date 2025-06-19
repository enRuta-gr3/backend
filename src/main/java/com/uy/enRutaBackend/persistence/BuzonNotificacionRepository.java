package com.uy.enRutaBackend.persistence;


import com.uy.enRutaBackend.entities.Buzon_notificacion;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Usuario;

@Repository
public interface BuzonNotificacionRepository extends CrudRepository<Buzon_notificacion, Integer>{
    Buzon_notificacion findByUsuario(Usuario usuario);
}

