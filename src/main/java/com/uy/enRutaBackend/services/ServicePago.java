package com.uy.enRutaBackend.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtMedio_de_Pago;
import com.uy.enRutaBackend.datatypes.DtMercadoPago;
import com.uy.enRutaBackend.datatypes.DtPaypal;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.Medio_de_Pago;
import com.uy.enRutaBackend.entities.Pago;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceIntegracionPaypal;
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
	private final IServiceIntegracionPaypal paypalService;
	
	@Value("${mercado.pago.properties.access.token}")
	private String mpAccessToken;
	@Value("${mercado.pago.properties.id}")
	private String mpId;
	@Value("${mercado.pago.properties.title}")
	private String mpTitle;
	@Value("${mercado.pago.properties.description}")
	private String mpDescription;
	@Value("${mercado.pago.properties.category_id}")
	private String mpCategoryId;
	@Value("${mercado.pago.properties.currency_id}")
	private String mpCurrencyId;

	
	@Autowired
	public ServicePago(PagoRepository pagoRepository, MedioDePagoRepository mpRepository,
			VendedorRepository vendedorRepository, ClienteRepository clienteRepository,
			IServiceIntegracionPaypal paypalService) {
		super();
		this.pagoRepository = pagoRepository;
		this.mpRepository = mpRepository;
		this.vendedorRepository = vendedorRepository;
		this.clienteRepository = clienteRepository;
		this.paypalService = paypalService;
	}

	@Override
	public ResultadoOperacion<?> solicitarMediosPago(DtVenta_Compra compra) {
		List<DtMedio_de_Pago> mps = new ArrayList<DtMedio_de_Pago>();
		try {
			if(compra.getVendedor() != null) {
				Vendedor v = vendedorRepository.findById(compra.getVendedor().getUuidAuth()).get();
				if(v != null)
					mps =  mediosDePagoMostrador(mps);
				 else {
					return new ResultadoOperacion(false, "Vendedor no valido", ErrorCode.LISTA_VACIA);	
				}
			} else {
				Cliente c = clienteRepository.findById(compra.getCliente().getUuidAuth()).get();
				if(c != null) {
					mps = mediosDePagoOnline(mps);
				} else {
					return new ResultadoOperacion(false, "Cliente no valido", ErrorCode.LISTA_VACIA);	
				}				
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
			if(mpEntity.getPerfiles_habilitados().toLowerCase().contains("vendedor") && mpEntity.isHabilitado()) {
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
			if(mpEntity.getPerfiles_habilitados().toLowerCase().contains("cliente") && mpEntity.isHabilitado()) {
				mps.add(entityToDt(mpEntity));
			}
		}
		return mps;
	}
	
	@Override
	public Pago crearPago(double monto, int mpId) {
		Medio_de_Pago mp = mpRepository.findById(mpId).get();
		Pago pago = new Pago();
		pago.setMonto(monto);
		pago.setMedio_de_pago(mp);
		return pagoRepository.save(pago);
	}
	
	@Override
	public ResultadoOperacion<?> solicitarParametrosPago(DtVenta_Compra compra, Venta_Compra venta) {
		int idMP = venta.getPago().getMedio_de_pago().getId_medio_de_pago();
		switch (idMP) {
		case 1:
			if(venta.getPago().getMedio_de_pago().isHabilitado())
				return solicitarParametrosEfectivo(compra, venta);
		case 2:
			if(venta.getPago().getMedio_de_pago().isHabilitado())
				return solicitarParametrosMercadoPago(compra, venta);
		case 3:
			if(venta.getPago().getMedio_de_pago().isHabilitado())
				return solicitarParametrosPayPal(compra, venta);
		default:
			return new ResultadoOperacion(false, "No se pudo completar la operacion", ErrorCode.REQUEST_INVALIDO);
		}
	}

	@Override
	public ResultadoOperacion<?> solicitarParametrosMercadoPago(DtVenta_Compra compra, Venta_Compra venta) {		
		try {
			DtMercadoPago mercPago = llenarDtMercadoPago(venta);
			if(mercPago != null) {	
				return new ResultadoOperacion(true, "Datos obtenidos correctamente", mercPago);
			} else {
				return new ResultadoOperacion(false, ErrorCode.DATOS_INSUFICIENTES.getMsg(), ErrorCode.DATOS_INSUFICIENTES);
			}
		} catch(Exception e) {
			return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), ErrorCode.REQUEST_INVALIDO);
		}
	}

	private DtMercadoPago llenarDtMercadoPago(Venta_Compra venta) {
		DtMercadoPago mercPago = new DtMercadoPago();
		mercPago.setAccess_token(mpAccessToken);
		mercPago.setCategory_id(mpCategoryId);
		mercPago.setCurrency_id(mpCurrencyId);
		mercPago.setDescription(mpDescription);
		mercPago.setQuantity(1);
		mercPago.setTitle(mpTitle);
		mercPago.setUnit_price(venta.getPago().getMonto());
		mercPago.setId_venta(venta.getId_venta());
		return mercPago;
	}

	@Override
	public ResultadoOperacion<?> solicitarParametrosPayPal(DtVenta_Compra compra, Venta_Compra venta) {
		try {
			BigDecimal monto = new BigDecimal(calcularMontoUsd(venta)).setScale(2, RoundingMode.HALF_UP);
			DtPaypal paypal = paypalService.crearOrdenDePago(monto, compra.getPago().getUrlRedir(), venta.getId_venta());
			paypal.setId_venta(venta.getId_venta());
			if(paypal != null) {	
				return new ResultadoOperacion(true, "Datos obtenidos correctamente", paypal);
			} else {
				return new ResultadoOperacion(false, ErrorCode.DATOS_INSUFICIENTES.getMsg(), ErrorCode.DATOS_INSUFICIENTES);
			}
		} catch(Exception e) {
			return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), ErrorCode.REQUEST_INVALIDO);
		}
	}


	private double calcularMontoUsd(Venta_Compra venta) {
		double monto = (venta.getPago().getMonto() / venta.getPago().getMedio_de_pago().getCotizacion());
		if(contarDecimales(String.valueOf(monto)) == 0)
			return monto;
		else {
			return Math.round(monto * 100.0) / 100.0;
		}		
	}
	
	public int contarDecimales(String monto) {
        if (monto.contains(".")) {
            return monto.split("\\.")[1].length();
        }
        return 0;
    }

	@Override
	public ResultadoOperacion<?> solicitarParametrosEfectivo(DtVenta_Compra compra, Venta_Compra venta) {
		try {
			DtVenta_Compra aDevolver = new DtVenta_Compra();
			aDevolver.setId_venta(venta.getId_venta());			
			if(aDevolver != null) {	
				return new ResultadoOperacion(true, "Datos obtenidos correctamente", aDevolver);
			} else {
				return new ResultadoOperacion(false, ErrorCode.DATOS_INSUFICIENTES.getMsg(), ErrorCode.DATOS_INSUFICIENTES);
			}
		} catch(Exception e) {
			return new ResultadoOperacion(false, ErrorCode.REQUEST_INVALIDO.getMsg(), ErrorCode.REQUEST_INVALIDO);
		}
	}

	@Override
	public void actualizarPago(Pago pago) {
		pagoRepository.save(pago);		
	}

	@Override
	public String verificarPagoPaypal(String id_orden) {
		String estado = new String();
		try {
			estado = paypalService.capturePayment(id_orden);
		} catch (IOException e) {
			return e.getLocalizedMessage();
		}
		return estado;
	}
}
