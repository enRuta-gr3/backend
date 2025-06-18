package com.uy.enRutaBackend.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtDepartamento;
import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.datatypes.DtLocalidadCargaMasiva;
import com.uy.enRutaBackend.datatypes.DtResultadoCargaMasiva;
import com.uy.enRutaBackend.entities.Departamento;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;
import com.uy.enRutaBackend.persistence.DepartamentoRepository;
import com.uy.enRutaBackend.persistence.LocalidadRepository;
import com.uy.enRutaBackend.utils.UtilsClass;

@Service
public class ServiceLocalidad implements IServiceLocalidad {

	private final UtilsClass utilsClass;
	
    private final LocalidadRepository repository;
    private final DepartamentoRepository deptoRepository;
    private static final String OK_MESSAGE = "Operación realizada con éxito";

    @Autowired
    public ServiceLocalidad(UtilsClass utilsClass, LocalidadRepository repository, DepartamentoRepository deptoRepository) {
        this.utilsClass = utilsClass;
        this.repository = repository;
        this.deptoRepository = deptoRepository;
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
	
	@Override
	public ResultadoOperacion<?> listarLocalidades() {
		List<DtLocalidad> listLocalidadesDt = new ArrayList<DtLocalidad>();
		List<Localidad> localidades = (List<Localidad>) repository.findAll();
		for(Localidad localidad : localidades) {
			DtLocalidad localidadDt = entityToDt(localidad);
			listLocalidadesDt.add(localidadDt);
		}
		if(listLocalidadesDt.size() > 0) {
			return new ResultadoOperacion(true, OK_MESSAGE, listLocalidadesDt);			
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}

    private boolean verificarExistenciaLocalidadEnDepartamento(Localidad aCrear) {
    	boolean existe = false;
    	List<Localidad> localidadesDepto = repository.findByDepartamentoIdDepartamento(aCrear.getDepartamento().getId_departamento());
    	if(localidadesDepto != null && !localidadesDepto.isEmpty()) {
    		for(Localidad localidad : localidadesDepto) {
    			if(aCrear.getNombre().equals(localidad.getNombre())) {
    				existe = true;    				
    			}
    		}
    	}    		
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

	@Override
	public DtResultadoCargaMasiva procesarLocalidades(List<DtLocalidadCargaMasiva> leidosCsv) {
		DtResultadoCargaMasiva resultadoCargaMasiva = new DtResultadoCargaMasiva();
		for(DtLocalidadCargaMasiva localidadLeida : leidosCsv) {
			resultadoCargaMasiva.setTotalLineasARegistrar(resultadoCargaMasiva.getTotalLineasARegistrar()+1);
			DtLocalidad localidadDt;
			try {
				localidadDt = crearDtRegistro(localidadLeida);
				try {
					DtLocalidad localidadRegistrada = crearLocalidad(localidadDt);
					if(localidadRegistrada != null) {
						utilsClass.actualizarResultado(resultadoCargaMasiva, "ok", localidadRegistrada);					
					} else {
						utilsClass.actualizarResultado(resultadoCargaMasiva, "error", null);
						System.out.println("No se pudo procesar la localidad. " + localidadDt.toString());
					}
				} catch (Exception e) {
					utilsClass.actualizarResultado(resultadoCargaMasiva, "error", null);	
					System.out.println("No se pudo procesar la localidad. " + localidadDt.toString());
				}	
			} catch (Exception e) {
				utilsClass.actualizarResultado(resultadoCargaMasiva, "error", null);	
				System.out.println("No se pudo procesar la localidad. " + e.getMessage());
			}
			
		}
		return resultadoCargaMasiva;
	}

	private DtLocalidad crearDtRegistro(DtLocalidadCargaMasiva localidadLeida) throws Exception {
		DtLocalidad locDt = new DtLocalidad();
		locDt.setNombreLocalidad(localidadLeida.getNombreLocalidad());
		Departamento depto = deptoRepository.findByNombre(localidadLeida.getNombreDepartamento());
		DtDepartamento deptoDt = new DtDepartamento();
		if(depto != null) {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.map(depto, deptoDt);
			locDt.setDepartamento(deptoDt);
			return locDt;
		} else {
			throw new Exception("Departamento no es válido.");
		}	
	}
	

	private DtLocalidad crearLocalidad(DtLocalidad localidadDt) throws Exception {
		Localidad loc = formatearDatosDtLocalidad(localidadDt);
		boolean existe = verificarExistenciaLocalidadEnDepartamento(loc);
		DtLocalidad creadaDt = new DtLocalidad();
		if (!existe) {
			Localidad localidadCreada = repository.save(loc);
			if (localidadCreada != null) {
				creadaDt = entityToDt(localidadCreada);
			}
		} else {
			throw new Exception("Ya existe la localidad");
		}
		return creadaDt;
	}
    
}
