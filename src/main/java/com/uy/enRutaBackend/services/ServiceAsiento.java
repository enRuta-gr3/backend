package com.uy.enRutaBackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtAsiento;
import com.uy.enRutaBackend.datatypes.DtDisAsiento;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.DisAsientoViajeRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;

@Service
public class ServiceAsiento implements IServiceAsiento {

	private static final String OK_MESSAGE = "Operación realizada con éxito";
	
    private final AsientoRepository asientoRepository;
    private final ViajeRepository viajeRepository;
    private final DisAsientoViajeRepository asientoViajeRepository;
    private final ModelMapper mapper;

    @Autowired
    public ServiceAsiento(AsientoRepository asientoRepository, ViajeRepository viajeRepository, DisAsientoViajeRepository asientoViajeRepository, ModelMapper mapper) {
		this.asientoRepository = asientoRepository;
		this.viajeRepository = viajeRepository;
		this.asientoViajeRepository = asientoViajeRepository;
		this.mapper = mapper;
	}
    
    @Override
    public ResultadoOperacion<?> listarAsientosDeOmnibus(DtViaje viaje) {
    	Optional<Viaje> obtenido = viajeRepository.findById(viaje.getId_viaje());
    	Omnibus bus = obtenido.get().getOmnibus();
    	List<Asiento> asientos = asientoRepository.findByOmnibus(bus);
    	List<DtDisAsiento> asientosDisponibles = new ArrayList<DtDisAsiento>();
    	
    	for(Asiento asiento : asientos) {
    		DisAsiento_Viaje disponibilidad = (DisAsiento_Viaje) asientoViajeRepository.findByAsientoAndViaje(asiento, obtenido.get());
    		if(disponibilidad != null) {
	    		DtDisAsiento disponibilidadDt = toDt(disponibilidad);
	    		asientosDisponibles.add(disponibilidadDt);
    		} else {
    			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
    		}
    	}
    	
    	if(asientosDisponibles.size() > 0) {
    		return new ResultadoOperacion(true, OK_MESSAGE, asientosDisponibles);
    	} else {
    		return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
    	}
    }

	private DtDisAsiento toDt(DisAsiento_Viaje disponibilidad) {
		DtDisAsiento disponibilidadDt = new DtDisAsiento();
		disponibilidadDt.setId_disAsiento(disponibilidad.getId_disAsiento());
		disponibilidadDt.setAsiento(formatAsiento(disponibilidad.getAsiento()));
		disponibilidadDt.setEstado(disponibilidad.getEstado());
		disponibilidadDt.setViaje(fotmatViaje(disponibilidad.getViaje()));
		return disponibilidadDt;
	}

	private DtViaje fotmatViaje(Viaje viaje) {
		DtViaje viajeDt = new DtViaje();
		viajeDt.setId_viaje(viaje.getId_viaje());
		return viajeDt;
	}

	private DtAsiento formatAsiento(Asiento asiento) {
		DtAsiento asientoDt = new DtAsiento();
		asientoDt.setId_asiento(asiento.getId_asiento());
		asientoDt.setNumero_asiento(asiento.getNumeroAsiento());
		asientoDt.setId_omnibus(asiento.getOmnibus().getId_omnibus());
		return asientoDt;
	}
}
