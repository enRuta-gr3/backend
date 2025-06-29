package com.uy.enRutaBackend.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.TareaProgramada;

@Repository
public interface TareaProgramadaRepository extends JpaRepository<TareaProgramada, Long> {

    List<TareaProgramada> findByFechaEjecucionBefore(Date fecha);

    List<TareaProgramada> findByOmnibusAndFechaEjecucionBetween(Omnibus omnibus, Date desde, Date hasta);

    Optional<TareaProgramada> findTopByOmnibusAndNuevoEstadoAndFechaEjecucionAfterOrderByFechaEjecucionAsc(
        Omnibus omnibus, boolean nuevoEstado, Date fechaEjecucion
    );
}
