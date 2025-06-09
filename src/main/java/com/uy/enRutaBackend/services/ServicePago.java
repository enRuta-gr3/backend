package com.uy.enRutaBackend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtMedio_de_Pago;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Medio_de_Pago;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServicePago;
import com.uy.enRutaBackend.persistence.ClienteRepository;
import com.uy.enRutaBackend.persistence.MedioDePagoRepository;
import com.uy.enRutaBackend.persistence.PagoRepository;
import com.uy.enRutaBackend.persistence.VendedorRepository;

@Service
public class ServicePago implements IServicePago {

	private final PagoRepository pagoRepository;
	private final MedioDePagoRepository mpRepository;
	private final VendedorRepository vendedorRepository;
	private final ClienteRepository clienteRepository;
	
	@Autowired
	public ServicePago(PagoRepository pagoRepository, MedioDePagoRepository mpRepository,
			VendedorRepository vendedorRepository, ClienteRepository clienteRepository) {
		super();
		this.pagoRepository = pagoRepository;
		this.mpRepository = mpRepository;
		this.vendedorRepository = vendedorRepository;
		this.clienteRepository = clienteRepository;
	}

	@Override
	public ResultadoOperacion<?> solicitarMediosPago(DtVenta_Compra compra) {
		List<DtMedio_de_Pago> mps = new ArrayList<DtMedio_de_Pago>();
		try {
			if(compra.getVendedor() != null) {
				Vendedor v = vendedorRepository.findById(compra.getVendedor().getUuidAuth()).get();
				if(v != null)
					mps =  mediosDePagoMostrador(mps);
			} else {
				Cliente c = clienteRepository.findById(compra.getCliente().getUuidAuth()).get();
				if(c != null)
					mps = mediosDePagoOnline(mps);				
			}
			
			if(mps == null || mps.isEmpty()) {
				return new ResultadoOperacion(false, "Error obteniendo los medios de pago", ErrorCode.LISTA_VACIA);				
			}
		} catch (Exception e) {
			return new ResultadoOperacion(false, "Error obteniendo los medios de pago", ErrorCode.REQUEST_INVALIDO);
		}		
		return new ResultadoOperacion(true, "Medios de pago disponibles.", mps);
	}

	private List<DtMedio_de_Pago> mediosDePagoMostrador(List<DtMedio_de_Pago> mps) {
		List<Medio_de_Pago> mpsEntity = (List<Medio_de_Pago>) mpRepository.findAll();
		for(Medio_de_Pago mpEntity : mpsEntity) {
			if(mpEntity.getPerfiles_habilitados().toLowerCase().contains("vendedor")) {
				mps.add(entityToDt(mpEntity));
			}
		}
		return mps;
	}


	private DtMedio_de_Pago entityToDt(Medio_de_Pago mpEntity) {
		DtMedio_de_Pago dtMp = new DtMedio_de_Pago();
		dtMp.setId_medio_de_pago(mpEntity.getId_medio_de_pago());
		dtMp.setNombre(mpEntity.getNombre());
		return dtMp;
	}


	private List<DtMedio_de_Pago> mediosDePagoOnline(List<DtMedio_de_Pago> mps) {
		List<Medio_de_Pago> mpsEntity = (List<Medio_de_Pago>) mpRepository.findAll();
		for(Medio_de_Pago mpEntity : mpsEntity) {
			if(mpEntity.getPerfiles_habilitados().toLowerCase().contains("cliente")) {
				mps.add(entityToDt(mpEntity));
			}
		}
		return mps;
	}
}
