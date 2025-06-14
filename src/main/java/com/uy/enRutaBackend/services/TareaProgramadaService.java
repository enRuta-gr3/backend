package com.uy.enRutaBackend.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.query.sqm.tree.update.SqmUpdateStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.entities.Historico_estado;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.TareaProgramada;
import com.uy.enRutaBackend.icontrollers.ITareaProgramadaService;
import com.uy.enRutaBackend.persistence.HistoricoEstadoRepository;
import com.uy.enRutaBackend.persistence.OmnibusRepository;
import com.uy.enRutaBackend.persistence.TareaProgramadaRepository;

@Service
public class TareaProgramadaService implements ITareaProgramadaService {

    private static final Logger logger = LoggerFactory.getLogger(TareaProgramadaService.class);

    @Autowired
    private TareaProgramadaRepository tareaRepo;

    @Autowired
    private OmnibusRepository omnibusRepo;

    @Autowired
    private HistoricoEstadoRepository historicoEstadoRepo;

    @Scheduled(fixedRate = 50000) // Cada 50 segundos
    public void verificarTareasPendientes() {
        Timestamp ahora = new Timestamp(new Date().getTime());
        List<TareaProgramada> tareas = tareaRepo.findByFechaEjecucionBefore(ahora);

        for (TareaProgramada tarea : tareas) {
            Omnibus omnibus = tarea.getOmnibus();
            boolean nuevoEstado = tarea.isNuevoEstado();

            // Evitar duplicados (tolerancia de milisegundos)
            boolean yaRegistrado = historicoEstadoRepo
                .findByOmnibus(omnibus)
                .stream()
                .anyMatch(h -> h.isActivo() == nuevoEstado &&
                               h.getFechaInicio() != null &&
                               Math.abs(h.getFechaInicio().getTime() - tarea.getFechaEjecucion().getTime()) < 1000);

            if (yaRegistrado) {
                logger.info("Ya existe historial para ómnibus ID {}, estado {}, fecha {}", 
                    omnibus.getId_omnibus(), nuevoEstado, tarea.getFechaEjecucion());
                tareaRepo.delete(tarea);
                continue;
            }

            // Aplicar nuevo estado al ómnibus
            omnibus.setActivo(nuevoEstado);

            Historico_estado hist = new Historico_estado();
            hist.setOmnibus(omnibus);
            hist.setVendedor(tarea.getVendedor());
            hist.setActivo(nuevoEstado);
            hist.setFechaInicio(tarea.getFechaEjecucion());

            if (!nuevoEstado) {
                // 🟡 DESACTIVANDO: buscar próxima reactivación
                Optional<TareaProgramada> nextReactivationOpt =
                    tareaRepo.findTopByOmnibusAndNuevoEstadoAndFechaEjecucionAfterOrderByFechaEjecucionAsc(
                        omnibus, true, tarea.getFechaEjecucion());

                if (nextReactivationOpt.isPresent()) {
                	Timestamp fechaReactivacion = new Timestamp(nextReactivationOpt.get().getFechaEjecucion().getTime());
                    hist.setFechaFin(fechaReactivacion);
                    omnibus.setFecha_fin(fechaReactivacion); // ✅ SETEAR fecha_fin en Omnibus
                } else {
                    hist.setFechaFin(null);
                    omnibus.setFecha_fin(null);
                    logger.warn("No se encontró reactivación para ómnibus ID {}", omnibus.getId_omnibus());
                }
                
             // 🆕 ACTUALIZAR HISTORIAL ANTERIOR DE ACTIVACIÓN
                historicoEstadoRepo.findTopByOmnibusAndActivoOrderByFechaInicioDesc(omnibus, true)
                    .ifPresent(prevTrue -> {
                        prevTrue.setFechaFin(tarea.getFechaEjecucion());
                        historicoEstadoRepo.save(prevTrue);
                        logger.info("Se cerró el historial TRUE anterior (ID {}) con fecha_fin = {}", 
                                    prevTrue.getId_his_estado(), tarea.getFechaEjecucion());
                    });

            } else {
                // 🟢 ACTIVANDO: limpiar fecha_fin y cerrar historial anterior
                omnibus.setFecha_fin(null); // ✅ RESET fecha_fin

                historicoEstadoRepo.findTopByOmnibusAndActivoOrderByFechaInicioDesc(omnibus, false)
                    .ifPresent(prev -> {
                        prev.setFechaFin(tarea.getFechaEjecucion());
                        historicoEstadoRepo.save(prev);
                    });

                hist.setFechaFin(null);
            }

            omnibusRepo.save(omnibus);
            historicoEstadoRepo.save(hist);
            tareaRepo.delete(tarea);

            logger.info("Tarea ejecutada para ómnibus ID {} - Nuevo estado: {}", omnibus.getId_omnibus(), nuevoEstado);
        }
    }
}