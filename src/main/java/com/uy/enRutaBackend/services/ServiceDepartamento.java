package com.uy.enRutaBackend.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtDepartamento;
import com.uy.enRutaBackend.entities.Departamento;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceDepartamento;
import com.uy.enRutaBackend.persistence.DepartamentoRepository;

@Service
public class ServiceDepartamento implements IServiceDepartamento {
	
	private DepartamentoRepository repository;
	private static final String OK_MESSAGE = "Operación realizada con éxito";

	@Autowired
	public ServiceDepartamento(DepartamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public ResultadoOperacion<DtDepartamento> listarDepartamentos() {
		List<DtDepartamento> deptosDt = new ArrayList<DtDepartamento>();
		List<Departamento> deptos = (List<Departamento>) repository.findAll();
		for(Departamento depto : deptos) {
			DtDepartamento deptoDt = entityToDt(depto);
			deptosDt.add(deptoDt);
		}
		if(deptosDt.size() > 0) {
			return new ResultadoOperacion(true, OK_MESSAGE, deptosDt);			
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		} 
	}

	private DtDepartamento entityToDt(Departamento depto) {
		ModelMapper mapper = new ModelMapper();		
		return mapper.map(depto, DtDepartamento.class);
	}

}
