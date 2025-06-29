package com.uy.enRutaBackend.persistence;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{
	Usuario findByEmail(String email);
	Usuario findByCi(String ci);
	Usuario findByEmailOrCi(String email, String ci);
    long countByTipoUsuarioAndEliminadoFalse(String tipoUsuario);
    long countByEliminadoFalse();
    Optional<Usuario> findByUuidAuth(UUID uuidAuth);
    
    @Query("""
    	    SELECT COUNT(u)
    	    FROM Usuario u
    	    WHERE u.eliminado = false
    	      AND u.ultimo_inicio_sesion IS NOT NULL
    	      AND u.ultimo_inicio_sesion >= :fechaLimite
    	""")
    	long countActivos(@Param("fechaLimite") Date fechaLimite);

    	@Query("""
    	    SELECT COUNT(u)
    	    FROM Usuario u
    	    WHERE u.eliminado = false
    	      AND (u.ultimo_inicio_sesion IS NULL OR u.ultimo_inicio_sesion < :fechaLimite)
    	""")
    	long countInactivos(@Param("fechaLimite") Date fechaLimite);
    	
    	
}
