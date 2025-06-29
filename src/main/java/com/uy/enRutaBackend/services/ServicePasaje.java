package com.uy.enRutaBackend.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtAsiento;
import com.uy.enRutaBackend.datatypes.DtCliente;
import com.uy.enRutaBackend.datatypes.DtDepartamento;
import com.uy.enRutaBackend.datatypes.DtEstadisticaViajesMes;
import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtPasaje;
import com.uy.enRutaBackend.datatypes.DtVenta_Compra;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.Cliente;
import com.uy.enRutaBackend.entities.EstadoPasaje;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Venta_Compra;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServicePasaje;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.PasajeRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.persistence.persistence;
import com.uy.enRutaBackend.utils.UtilsClass;

@Service
public class ServicePasaje implements IServicePasaje {

    private final persistence persistence;
    private final AsientoRepository asientoRepository;
    private final ViajeRepository viajeRepository;
    private final PasajeRepository pasajeRepository;
    private final ModelMapper mapper;
    private final UtilsClass utils;

    public ServicePasaje(persistence persistence,
                         AsientoRepository asientoRepository,
                         ViajeRepository viajeRepository, ModelMapper mapper, UtilsClass utils,
                         PasajeRepository pasajeRepository) {
        this.persistence = persistence;
        this.asientoRepository = asientoRepository;
        this.viajeRepository = viajeRepository;
        this.mapper = mapper;
        this.utils = utils;
        this.pasajeRepository = pasajeRepository;
    }
    
    public List<Pasaje> CrearPasajes(List<DtOmnibus> omnibusDTOs, Venta_Compra venta_compra) {
        List<Pasaje> listaPasajes = new ArrayList<>();

        for (DtOmnibus dtoOmnibus : omnibusDTOs) {
            int idViaje = dtoOmnibus.getViajes().get(0).getId_viaje();
            Viaje viaje = viajeRepository.findById(idViaje).orElseThrow();
            double monto = viaje.getPrecio_viaje();

            for (DtAsiento asientoDTO : dtoOmnibus.getAsientos()) {
                int idAsiento = asientoDTO.getId_asiento();
                Asiento asiento = asientoRepository.findById(idAsiento).orElseThrow();

                if (asiento.getOmnibus().getId_omnibus() != viaje.getOmnibus().getId_omnibus()) {
                    throw new IllegalArgumentException("El asiento no pertenece al ómnibus del viaje.");
                }

                Pasaje pasaje = new Pasaje(monto, viaje, asiento, venta_compra, EstadoPasaje.VIGENTE);
                listaPasajes.add(pasaje);
            }
        }

        RegistrarPasajes(listaPasajes);
        
        return listaPasajes;
    }

    @Override
    public void RegistrarPasajes(List<Pasaje> pasajes) {
        for (Pasaje pasaje : pasajes) {
            boolean isSaved = persistence.savePasaje(pasaje);
            if (isSaved) {
                System.out.println("✅ Pasaje registrado: " + pasaje);
            } else {
                System.out.println("❌ Error al registrar pasaje: " + pasaje);
            }
        }
    }

	@Override
	public DtPasaje entityToDt(Pasaje pasaje) {
		DtPasaje pasajeDt = new DtPasaje();
		pasajeDt.setId_pasaje(pasaje.getId_pasaje());
		pasajeDt.setViaje(crearDtViaje(pasaje.getViaje()));
		pasajeDt.setAsiento(crearDtAsiento(pasaje.getAsiento()));
		pasajeDt.setPrecio(pasaje.getPrecio());
		pasajeDt.setVenta_compra(ventaCompraDt(pasaje.getVenta_compra()));
		return pasajeDt;
	}


	private DtVenta_Compra ventaCompraDt(Venta_Compra venta_compra) {
		DtVenta_Compra venta = new DtVenta_Compra();
		venta.setId_venta(venta_compra.getId_venta());
		venta.setCliente(crearDtCliente(venta_compra.getCliente()));
		return venta;
	}

	private DtCliente crearDtCliente(Cliente cliente) {
		DtCliente clienteDt = new DtCliente();
		clienteDt.setNombres(cliente.getNombres());
		clienteDt.setApellidos(cliente.getApellidos());
		clienteDt.setCi(cliente.getCi());
		clienteDt.setEsEstudiante(cliente.isEsEstudiante());
		clienteDt.setEsJubilado(cliente.isEsJubilado());
		clienteDt.setEstado_descuento(cliente.isEstado_descuento());
		clienteDt.setTipoDescuentoCliente();
		return clienteDt;
	}

	private DtAsiento crearDtAsiento(Asiento asiento) {
		DtAsiento asientoDt = new DtAsiento();
		asientoDt.setNumero_asiento(asiento.getNumeroAsiento());
		
		return asientoDt;
	}

	private DtViaje crearDtViaje(Viaje viaje) {
		DtViaje viajeDt = new DtViaje();
		viajeDt.setLocalidadOrigen(crearLocalidadDt(viaje.getLocalidadOrigen()));
		viajeDt.setLocalidadDestino(crearLocalidadDt(viaje.getLocalidadDestino()));
		viajeDt.setFecha_partida(utils.dateToString(viaje.getFecha_partida()));
		viajeDt.setHora_partida(utils.timeToString(viaje.getHora_partida()));
		viajeDt.setFecha_llegada(utils.dateToString(viaje.getFecha_llegada()));
		viajeDt.setHora_llegada(utils.timeToString(viaje.getHora_llegada()));
		
		return viajeDt;
	}


	private DtLocalidad crearLocalidadDt(Localidad localidad) {
		DtLocalidad localidadDt = new DtLocalidad();
		localidadDt.setNombreLocalidad(localidad.getNombre());
		localidadDt.setDepartamento(mapper.map(localidad.getDepartamento(), DtDepartamento.class));
		return localidadDt;
	}


	@Override
	public ResultadoOperacion<?> solicitarHistorial(List<Venta_Compra> comprasUsuario) {
		List<DtPasaje> historialPasajes = buscarPasajesEnCompras(comprasUsuario);
		if(!historialPasajes.isEmpty()) {
			return new ResultadoOperacion(true, "Historial obtenido correctamente", historialPasajes);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
		
	}


	/**
	 * @param comprasUsuario
	 * @return
	 */
	private List<DtPasaje> buscarPasajesEnCompras(List<Venta_Compra> comprasUsuario) {
		List<DtPasaje> historialPasajes = new ArrayList<DtPasaje>();
		for(Venta_Compra compra : comprasUsuario) {
			List<Pasaje> pasajes = pasajeRepository.findAllByVentaCompra(compra);
			for(Pasaje pasaje : pasajes) {
				if(!pasaje.getEstadoPasaje().equals(EstadoPasaje.DEVUELTO))
					historialPasajes.add(entityToDt(pasaje));
			}
		}
		return historialPasajes;
	}


	@Override
	public ResultadoOperacion<?> listarPasajesPorViaje(DtViaje viajeDt) {
		Viaje viaje = mapper.map(viajeDt, Viaje.class);
		List<Pasaje> pasajes = pasajeRepository.findByViaje(viaje);
		
		List<DtPasaje> pasajesDt = toDtList(pasajes);
		if(!pasajesDt.isEmpty()) {
			return new ResultadoOperacion(true, "Listado de pasajes obtenido correctamente", pasajesDt);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}


	private DtVenta_Compra llenarDtVenta(Venta_Compra venta_compra) {
		DtVenta_Compra venta = new DtVenta_Compra();
		venta.setCliente(llenarDtCliente(venta_compra.getCliente()));
		venta.setId_venta(venta_compra.getId_venta());
		return venta;
	}


	private DtCliente llenarDtCliente(Cliente c) {
		DtCliente cliente = new DtCliente();
		if(c.getCi() != null)
			cliente.setCi(c.getCi());
		cliente.setNombres(c.getNombres());
		cliente.setApellidos(c.getApellidos());
		return cliente;
	}


	@Override
	public ResultadoOperacion<?> listarPasajesPorVenta(Venta_Compra compra) {
		List<Pasaje> pasajes = new ArrayList<Pasaje>();
		pasajes = pasajeRepository.findAllByVentaCompraAndEstadoPasaje(compra, EstadoPasaje.VIGENTE);
		List<DtPasaje> pasajesDt = toDtList(pasajes);
		if(!pasajesDt.isEmpty()) {
			return new ResultadoOperacion(true, "Listado de pasajes obtenido correctamente", pasajesDt);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}
	}

	/**
	 * @param pasajes
	 * @return
	 */
	private List<DtPasaje> toDtList(List<Pasaje> pasajes) {
		List<DtPasaje> pasajesDt = new ArrayList<DtPasaje>();
		for(Pasaje pasaje : pasajes) {
			if(!pasaje.getEstadoPasaje().equals(EstadoPasaje.DEVUELTO)) {
				DtPasaje pasajeDt = new DtPasaje();
				pasajeDt = entityToDt(pasaje);
				pasajesDt.add(pasajeDt);				
			}
		}
		return pasajesDt;
	}
	
	@Override
	public ResultadoOperacion<?> pasajesVendidosPorMes() {
		int anio = LocalDate.now(ZoneId.of("America/Montevideo")).getYear();
		List<Pasaje> pasajesAnio = pasajeRepository.obtenerVendidosPorAnio(anio);

		if (pasajesAnio.size() > 0) {
			Map<YearMonth, Long> pasajesPorMes = pasajesAnio.stream().collect(Collectors
					.groupingBy(pasaje -> YearMonth.from(pasaje.getFechaVenta().toLocalDate()), Collectors.counting()));
			
			List<DtEstadisticaViajesMes> estadisticaPorMes = pasajesPorMes.entrySet().stream()
					.map(est -> crearDtPorMes(est.getKey(), est.getValue())).collect(Collectors.toList());

			return new ResultadoOperacion(true, "La estadistica se creo correctamente.", estadisticaPorMes);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}

	}
	
	@Override
	public ResultadoOperacion<?> pasajesDevueltosPorMes() {
		int anio = LocalDate.now(ZoneId.of("America/Montevideo")).getYear();
		List<Pasaje> pasajesDevueltosAnio = pasajeRepository.obtenerDevueltosPorAnio(anio);

		if (pasajesDevueltosAnio.size() > 0) {
			Map<YearMonth, Long> pasajesPorMes = pasajesDevueltosAnio.stream().collect(Collectors
					.groupingBy(pasaje -> YearMonth.from(pasaje.getFechaDevolucion().toLocalDate()), Collectors.counting()));
			
			List<DtEstadisticaViajesMes> estadisticaPorMes = pasajesPorMes.entrySet().stream()
					.map(est -> crearDtPorMes(est.getKey(), est.getValue())).collect(Collectors.toList());

			return new ResultadoOperacion(true, "La estadistica se creo correctamente.", estadisticaPorMes);
		} else {
			return new ResultadoOperacion(false, ErrorCode.LISTA_VACIA.getMsg(), ErrorCode.LISTA_VACIA);
		}

	}

	private DtEstadisticaViajesMes crearDtPorMes(YearMonth mes, Long cantidad) {
		DtEstadisticaViajesMes pasajesMes = new DtEstadisticaViajesMes();
		String[] separaAnioMes = mes.toString().split("-");
		pasajesMes.setMes(separaAnioMes[1]);
		pasajesMes.setAnio(separaAnioMes[0]);
		pasajesMes.setCantidad(cantidad.intValue());
		return pasajesMes;
	}
}
