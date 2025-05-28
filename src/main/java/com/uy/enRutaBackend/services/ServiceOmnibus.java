package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.datatypes.DtAsiento;
import com.uy.enRutaBackend.datatypes.DtHistoricoEstado;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.LocalidadRepository;
import com.uy.enRutaBackend.persistence.OmnibusRepository;

import org.springframework.transaction.annotation.Transactional;


import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOmnibus implements IServiceOmnibus{

    @Autowired
    private OmnibusRepository omnibusRepository;

    @Autowired
    private LocalidadRepository localidadRepository;
    
    @Autowired
    private AsientoRepository asientoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResultadoOperacion<DtOmnibus> registrarOmnibus(DtOmnibus dtOmnibus) {
        try {
        	// Verificar si existe registrado el num de coche
        	boolean existe = omnibusRepository.existsByNroCoche(dtOmnibus.getNro_coche());
           
        	if (existe) {
                return new ResultadoOperacion<>(false, "Numero de coche ya existente.", "400");
            }
            
            // Convertir DTO a entidad y setear localidad
            Omnibus Omnibus = dtoToEntity(dtOmnibus);
            Omnibus.setLocalidad_actual(localidadRepository.findById(dtOmnibus.getId_localidad_actual()).orElseThrow());
            
            // Persistir omnibus
            Omnibus guardado = omnibusRepository.save(Omnibus);
            DtOmnibus respuesta = entityToDto(guardado);
            
            // Crear los asientos del omnibus
            List<Asiento> asientos = new ArrayList<>();
            for (int i = 1; i <= Omnibus.getCapacidad(); i++) {
                Asiento asiento = new Asiento();
                asiento.setNumeroAsiento(i);
                asiento.setOmnibus(Omnibus);
                asientos.add(asiento);
            }
            asientoRepository.saveAll(asientos); // Persistís TODOS los asientos
            Omnibus.setAsientos(asientos);

            return new ResultadoOperacion<>(true, "Ómnibus registrado correctamente", respuesta);
        } catch (Exception e) {
            return new ResultadoOperacion<>(false, "Error al registrar ómnibus: " + e.getMessage(), null);
        }
    }

    @Transactional(readOnly = true)
    public List<DtOmnibus> listarOmnibus() {
        List<Omnibus> lista = (List<Omnibus>) omnibusRepository.findAll();

        return lista.stream()
                    .map(this::entityToDto)
                    .toList();
    }
    
    private Omnibus dtoToEntity(DtOmnibus dto) {
        Omnibus omnibus = new Omnibus();
        omnibus.setCapacidad(dto.getCapacidad());
        omnibus.setNro_coche(dto.getNro_coche());
        omnibus.setActivo(dto.isActivo());
        omnibus.setFecha_fin(dto.getFecha_fin());
        // Localidad se busca desde el repositorio
        return omnibus;
    }

    private DtOmnibus entityToDto(Omnibus omnibus) {
        DtOmnibus dto = modelMapper.map(omnibus, DtOmnibus.class);

        // Convertir asientos
        if (omnibus.getAsientos() != null) {
        	List<DtAsiento> asientos = omnibus.getAsientos().stream()
                    .map(asiento -> new DtAsiento(
                        asiento.getId_asiento(),
                        asiento.getNumeroAsiento(),
                        omnibus.getId_omnibus()))
                    .toList();
            dto.setAsientos(asientos);
        }
        
        // Convertir viajes
        if (omnibus.getViajes() != null) {
            List<DtViaje> viajes = omnibus.getViajes().stream().map(v -> {
                DtViaje dt = new DtViaje();
                dt.setId_viaje(v.getId_viaje());
                dt.setFecha_partida(v.getFecha_partida());
                dt.setHora_partida(v.getHora_partida());
                dt.setFecha_llegada(v.getFecha_llegada());
                dt.setHora_llegada(v.getHora_llegada());
                dt.setPrecio_viaje(v.getPrecio_viaje());
                dt.setEstado(v.getEstado().toString());
                dt.setId_localidad_origen(v.getLocalidadOrigen().getId_localidad());
                dt.setId_localidad_destino(v.getLocalidadDestino().getId_localidad());
                dt.setId_omnibus(v.getOmnibus().getId_omnibus());
                return dt;
            }).toList();
            dto.setViajes(viajes);
        }

        // Convertir histórico de estados
        if (omnibus.getHistorico_estado() != null) {
            List<DtHistoricoEstado> historicos = omnibus.getHistorico_estado().stream().map(h -> {
                DtHistoricoEstado dt = new DtHistoricoEstado();
                dt.setId_his_estado(h.getId_his_estado());
                dt.setFecha_inicio(h.getFecha_inicio());
                dt.setFecha_fin(h.getFecha_fin());
                dt.setActivo(h.isActivo());
                dt.setId_omnibus(h.getOmnibus().getId_omnibus());
                return dt;
            }).toList();
            dto.setHistorico_estado(historicos);
        }

        // Localidad actual solo por id (ya está en el DTO)
        dto.setId_localidad_actual(omnibus.getLocalidad_actual().getId_localidad());

        return dto;
    }

}


/*
package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;
import com.uy.enRutaBackend.persistence.persistence;

import org.springframework.stereotype.Service;

@Service
public class ServiceOmnibus implements IServiceOmnibus {
	
	
    private final persistence persistence;

    // Inyección de la dependencia Persistence
    public ServiceOmnibus(persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void RegistrarOmnibus(Omnibus omnibus) {
        if (omnibus != null && omnibus.getCapacidad() > 0) {
            boolean isSaved = persistence.saveOmnibus(omnibus);
            if (isSaved) {
                System.out.println("✅ Omnibus registrado y persistido correctamente.");
            } else {
                System.out.println("❌ Error al registrar omnibus.");
            }
        } else {
            System.out.println("❌ Datos de omnibus inválidos.");
        }
    }
    
	
	
}
	*/