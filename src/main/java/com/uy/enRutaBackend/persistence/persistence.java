package com.uy.enRutaBackend.persistence;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.entities.Viaje;

@Component
public class persistence {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean saveUser(Usuario usuario) {
        return saveEntity(usuario);
    }

    @Transactional
    public boolean saveLocalidad(Localidad localidad) {
        return saveEntity(localidad);
    }

    @Transactional
    public boolean saveOmnibus(Omnibus omnibus) {
        return saveEntity(omnibus);
    }

    @Transactional
    public boolean saveAsiento(Asiento asiento) {
        return saveEntity(asiento);
    }

    @Transactional
    public boolean saveViaje(Viaje viaje) {
        return saveEntity(viaje);
    }

    @Transactional
    public boolean savePasaje(Pasaje pasaje) {
        return saveEntity(pasaje);
    }

    @Transactional
    private boolean saveEntity(Object entity) {
        try {
            entityManager.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Transactional
    public Usuario findByEmail(String email) {
        try {
            return entityManager.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró ningún usuario con ese email
        }
    }

}
