package com.uy.enRutaBackend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;
import com.uy.enRutaBackend.persistence.LocalidadRepository;

@Service
public class ServiceLocalidad implements IServiceLocalidad {

    private final LocalidadRepository repository;

    @Autowired
    public ServiceLocalidad(LocalidadRepository repository) {
        this.repository = repository;
    }

	@Override
	public ResultadoOperacion<?> RegistrarLocalidad(DtLocalidad localidadDt) {
		if (localidadDt != null && localidadDt.getNombreLocalidad() != null
				&& !localidadDt.getNombreLocalidad().isEmpty() && localidadDt.getDepartamento() != null) {
			Localidad aCrear = formatearDatosDtLocalidad(localidadDt);
			boolean existe = verificarExistenciaLocalidadEnDepartamento(aCrear);
			if (!existe) {
				Localidad localidadCreada = repository.save(aCrear);
				if (localidadCreada != null) {
					DtLocalidad creadaDt = entityToDt(localidadCreada);
					System.out.println("Localidad registrada y persistida correctamente.");
					return new ResultadoOperacion(true, "Localidad registrada y persistida correctamente.", creadaDt);
				} else {
					System.out.println("Error al registrar localidad.");
					return new ResultadoOperacion(false, ErrorCode.ERROR_DE_CREACION.getMsg(),
							ErrorCode.ERROR_DE_CREACION);
				}
			} else {
				System.out.println("La localidad ya existe.");
				return new ResultadoOperacion(false, "La localidad ya existe.",
						ErrorCode.ERROR_DE_CREACION);
			}
		} else {
			System.out.println("Datos de localidad inválidos.");
			return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), ErrorCode.REQUEST_INVALIDO);
		}
	}

    private boolean verificarExistenciaLocalidadEnDepartamento(Localidad aCrear) {
    	boolean existe = false;
    	if(repository.findByDepartamentoIdDepartamento(aCrear.getDepartamento().getId_departamento()) != null 
    			&& !repository.findByDepartamentoIdDepartamento(aCrear.getDepartamento().getId_departamento()).isEmpty())
    		existe = true;
		return existe;
	}

	private Localidad formatearDatosDtLocalidad(DtLocalidad localidadDt) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(localidadDt, Localidad.class);
	}

	private DtLocalidad entityToDt(Localidad localidad) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(localidad, DtLocalidad.class);
	}
    
}
