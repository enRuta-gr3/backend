package com.uy.enRutaBackend.services;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uy.enRutaBackend.datatypes.CantidadesPorMes;
import com.uy.enRutaBackend.datatypes.DtHistoricoEstado;
import com.uy.enRutaBackend.datatypes.DtLocalidad;
import com.uy.enRutaBackend.datatypes.DtOmnibus;
import com.uy.enRutaBackend.datatypes.DtOmnibusCargaMasiva;
import com.uy.enRutaBackend.datatypes.DtOmnibusPorEstadoPorMes;
import com.uy.enRutaBackend.datatypes.DtPorcentajeOmnibusAsignados;
import com.uy.enRutaBackend.datatypes.DtResultadoCargaMasiva;
import com.uy.enRutaBackend.datatypes.DtViaje;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.EstadoAsiento;
import com.uy.enRutaBackend.entities.EstadoViaje;
import com.uy.enRutaBackend.entities.Historico_estado;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.TareaProgramada;
import com.uy.enRutaBackend.entities.Vendedor;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;
import com.uy.enRutaBackend.persistence.AsientoRepository;
import com.uy.enRutaBackend.persistence.DisAsientoViajeRepository;
import com.uy.enRutaBackend.persistence.HistoricoEstadoRepository;
import com.uy.enRutaBackend.persistence.LocalidadRepository;
import com.uy.enRutaBackend.persistence.OmnibusRepository;
import com.uy.enRutaBackend.persistence.TareaProgramadaRepository;
import com.uy.enRutaBackend.persistence.VendedorRepository;
import com.uy.enRutaBackend.persistence.ViajeRepository;
import com.uy.enRutaBackend.utils.UtilsClass;

@Service
public class ServiceOmnibus implements IServiceOmnibus{

    @Autowired
    private OmnibusRepository omnibusRepository;

    @Autowired
    private LocalidadRepository localidadRepository;
    
    @Autowired
    private AsientoRepository asientoRepository;
    
    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private HistoricoEstadoRepository historicoEstadoRepository;
    
    @Autowired
    private VendedorRepository vendedorRepository;
    
    @Autowired
    private UtilsClass utils;

    @Autowired
    private TareaProgramadaRepository tareaProgramadaRepository;
    
    @Autowired
    private DisAsientoViajeRepository disAsientosRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Value("${spring.jackson.time-zone}")
    private ZoneId zona;
    
    
    public ResultadoOperacion<DtOmnibus> registrarOmnibus(DtOmnibus dtOmnibus) {
        try {
        	// Verificar si existe registrado el num de coche
        	boolean existe = omnibusRepository.existsByNroCoche(dtOmnibus.getNro_coche());
           
        	if (existe) {
                return new ResultadoOperacion<>(false, "Ya existe un ómnibus con ese número de coche.", ErrorCode.YA_EXISTE);
            }
            
            // Convertir DTO a entidad y setear localidad
            Omnibus Omnibus = dtoToEntity(dtOmnibus);
            Omnibus.setLocalidad_actual(localidadRepository.findById(dtOmnibus.getId_localidad_actual()).orElseThrow());
            
            // Persistir omnibus
            Omnibus guardado = omnibusRepository.save(Omnibus);
            
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
            
            
            
            DtOmnibus respuesta = entityToDto(guardado);
            return new ResultadoOperacion<>(true, "Ómnibus registrado correctamente", respuesta);
        } catch (Exception e) {
            return new ResultadoOperacion<>(false, "Error al registrar ómnibus: " + e.getMessage(), ErrorCode.ERROR_DE_CREACION);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoOperacion<List<DtOmnibus>> listarOmnibus() {
        try {
        	Sort sort = Sort.by("nroCoche").ascending();
            List<Omnibus> lista = (List<Omnibus>) omnibusRepository.findAll(sort);

            if (lista.isEmpty()) {
                return new ResultadoOperacion<>(false, "No hay ómnibus registrados", ErrorCode.LISTA_VACIA);
            }

            List<DtOmnibus> dtLista = lista.stream()
                    .map(this::entityToDtoSinViajes)
                    .toList();

            return new ResultadoOperacion<>(true, "Ómnibus listados correctamente", dtLista);
        } catch (Exception e) {
            return new ResultadoOperacion<>(false, "Error al listar ómnibus: " + e.getMessage(), ErrorCode.ERROR_LISTADO);
        }
    }
    
    private Omnibus dtoToEntity(DtOmnibus dto) {
        Omnibus omnibus = new Omnibus();
        omnibus.setCapacidad(dto.getCapacidad());
        omnibus.setNro_coche(dto.getNro_coche());
        omnibus.setActivo(dto.isActivo());
        omnibus.setFecha_fin(dto.getFecha_fin());
        omnibus.setFechaCreacion(new Date());
        // Localidad se busca desde el repositorio
        return omnibus;
    }

    private DtOmnibus entityToDto(Omnibus omnibus) {
        DtOmnibus dto = new DtOmnibus();
        dto.setActivo(omnibus.isActivo());
        dto.setCapacidad(omnibus.getCapacidad());
        dto.setId_omnibus(omnibus.getId_omnibus());
        dto.setNro_coche(omnibus.getNro_coche());
        

        if (omnibus.getLocalidad_actual() != null) {
            dto.setId_localidad_actual(omnibus.getLocalidad_actual().getId_localidad());
        }

        // Viajes
        List<Viaje> viajes = viajeRepository.findByOmnibus(omnibus);
        if (viajes != null && !viajes.isEmpty()) {
            List<DtViaje> dtViajes = viajes.stream()
                .map(v -> {
                    DtViaje dt = new DtViaje();
                    dt.setFecha_partida(utils.dateToString(v.getFecha_partida()));
                    dt.setHora_partida(utils.timeToString(v.getHora_partida()));
                    dt.setFecha_llegada(utils.dateToString(v.getFecha_llegada()));
                    dt.setHora_llegada(utils.timeToString(v.getHora_llegada()));
                    dt.setPrecio_viaje(v.getPrecio_viaje());
                    dt.setEstado(v.getEstado().toString());
                    dt.setLocalidadOrigen(modelMapper.map(v.getLocalidadOrigen(), DtLocalidad.class));
                    dt.setLocalidadDestino(modelMapper.map(v.getLocalidadDestino(), DtLocalidad.class));
                    return dt; 
                }).toList();
            dto.setViajes(dtViajes);
        }

        // Histórico de estados
        List<Historico_estado> historicos = historicoEstadoRepository.findByOmnibus(omnibus);
        if (historicos != null && !historicos.isEmpty()) {
            List<DtHistoricoEstado> dtHistoricos = historicos.stream()
                .map(h -> {
                    DtHistoricoEstado dt = new DtHistoricoEstado();
                    dt.setId_his_estado(h.getId_his_estado());
                    dt.setFecha_inicio(h.getFechaInicio());
                    dt.setFecha_fin(h.getFechaFin());
                    dt.setActivo(h.isActivo());
                    dt.setId_omnibus(h.getOmnibus().getId_omnibus());
                    return dt;
                }).toList();
            dto.setHistorico_estado(dtHistoricos);
        }
       
        return dto;
    }

    
    public ResultadoOperacion<?> cambiarEstadoOmnibus(DtHistoricoEstado dto) {
        Optional<Omnibus> optOmnibus = omnibusRepository.findById(dto.getId_omnibus());
        if (optOmnibus.isEmpty()) {
            return new ResultadoOperacion<>(false, "Ómnibus no encontrado", ErrorCode.SIN_RESULTADOS);
        }
        Omnibus omnibus = optOmnibus.get();
        boolean estadoActual = omnibus.isActivo();
        boolean nuevoEstado = dto.isActivo();
        Date fechaInicio = dto.getFecha_inicio();
        Date fechaFin = dto.getFecha_fin();
        Vendedor vendedor = vendedorRepository.findById(dto.getVendedor()).orElse(null);

        // 🟢 ACTIVACIÓN INMEDIATA
        if (nuevoEstado) {
            if (estadoActual) {
                return new ResultadoOperacion<>(true, "El ómnibus ya está activo.", null);
            }

            omnibus.setActivo(true);
            omnibus.setFecha_fin(null);
            omnibusRepository.save(omnibus);

            tareaProgramadaRepository
                .findTopByOmnibusAndNuevoEstadoAndFechaEjecucionAfterOrderByFechaEjecucionAsc(
                    omnibus, true, new Timestamp(new Date().getTime()))
                .ifPresent(tareaProgramadaRepository::delete);
            
            historicoEstadoRepository
            .findTopByOmnibusAndActivoOrderByFechaInicioDesc(omnibus, false)
            .ifPresent(prev -> {
                prev.setFechaFin(new Date());
                historicoEstadoRepository.save(prev);
            });

            // Crear histórico de activación
            Historico_estado hist = new Historico_estado();
            hist.setOmnibus(omnibus);
            hist.setVendedor(vendedor);
            hist.setActivo(true);
            hist.setFechaInicio(new Date());
            hist.setFechaFin(null);
            historicoEstadoRepository.save(hist);

            return new ResultadoOperacion<>(true, "Ómnibus activado correctamente.", null);
        }

        // 🟡 DESACTIVACIÓN PROGRAMADA
        if (fechaInicio == null || fechaFin == null) {
            return new ResultadoOperacion<>(false, "Debe proporcionar fecha de inicio y fin.", ErrorCode.FECHAS_INVALIDAS);
        }

        Date ahora = new Date();
        if (fechaInicio.before(ahora)) {
            return new ResultadoOperacion<>(false, "La fecha de inicio no puede ser anterior al momento actual.", ErrorCode.FECHAS_INVALIDAS);
        }
        if (fechaFin.before(ahora)) {
            return new ResultadoOperacion<>(false, "La fecha de fin no puede ser anterior al momento actual.", ErrorCode.FECHAS_INVALIDAS);
        }
        if (fechaFin.before(fechaInicio)) {
            return new ResultadoOperacion<>(false, "La fecha de fin no puede ser menor a la fecha de inicio.", ErrorCode.FECHAS_INVALIDAS);
        }
        if (fechaFin.equals(fechaInicio)) {
            return new ResultadoOperacion<>(false, "La fecha y hora de inicio y fin no pueden ser iguales.", ErrorCode.FECHAS_INVALIDAS);
        }

        Timestamp tsInicio = new Timestamp(fechaInicio.getTime());
        Timestamp tsFin = new Timestamp(fechaFin.getTime());

        if (viajeRepository.existeViajeEntreFechas(omnibus.getId_omnibus(), tsInicio, tsFin)) {
            return new ResultadoOperacion<>(false, "Tiene viajes asignados en ese período.", ErrorCode.OPERACION_INVALIDA);
        }

        List<TareaProgramada> tareas = tareaProgramadaRepository.findByOmnibusAndFechaEjecucionBetween(
            omnibus, tsInicio, tsFin
        );
        if (!tareas.isEmpty()) {
            return new ResultadoOperacion<>(false, "Ya existe una tarea programada.", ErrorCode.OPERACION_INVALIDA);
        }

        // Crear tarea para DESACTIVAR
        TareaProgramada tareaDesactivar = new TareaProgramada();
        tareaDesactivar.setOmnibus(omnibus);
        tareaDesactivar.setNuevoEstado(false);
        tareaDesactivar.setFechaEjecucion(fechaInicio);
        tareaDesactivar.setDescripcion("Desactivar ómnibus automáticamente");
        tareaDesactivar.setVendedor(vendedor);
        tareaProgramadaRepository.save(tareaDesactivar);

        // Crear tarea para REACTIVAR
        TareaProgramada tareaReactivar = new TareaProgramada();
        tareaReactivar.setOmnibus(omnibus);
        tareaReactivar.setNuevoEstado(true);
        tareaReactivar.setFechaEjecucion(fechaFin);
        tareaReactivar.setDescripcion("Reactivar ómnibus automáticamente");
        tareaReactivar.setVendedor(vendedor);
        tareaProgramadaRepository.save(tareaReactivar);

        omnibusRepository.save(omnibus);

        return new ResultadoOperacion<>(true, "Tareas programadas correctamente.", null);
    }

	@Override
	public DtResultadoCargaMasiva procesarOmnibus(List<DtOmnibusCargaMasiva> leidosCsv) {
		DtResultadoCargaMasiva resultadoCargaMasiva = new DtResultadoCargaMasiva();
		for(DtOmnibusCargaMasiva omnibusLeido : leidosCsv) {
			resultadoCargaMasiva.setTotalLineasARegistrar(resultadoCargaMasiva.getTotalLineasARegistrar()+1);
			DtOmnibus omnibusDt;
			try {
				omnibusDt = crearDt(omnibusLeido);
				try {
					DtOmnibus omnibusRegistrado = crearOmnibus(omnibusDt, omnibusLeido);
					if(omnibusRegistrado != null) {
						utils.actualizarResultado(resultadoCargaMasiva, "ok", omnibusRegistrado);					
					} else {
						utils.actualizarResultado(resultadoCargaMasiva, "error", null);
						System.out.println("No se pudo crear el omnibus. " + omnibusDt.toString());
					}
				} catch (Exception e) {
					utils.actualizarResultado(resultadoCargaMasiva, "error", null);	
					System.out.println("No se pudo crear el omnibus. " + e.getMessage());
				}
			} catch (Exception e) {
				utils.actualizarResultado(resultadoCargaMasiva, "error", null);	
				System.out.println("No se pudo crear el omnibus. " + e.getMessage());
			}
			
		}
		
		return resultadoCargaMasiva;
	}

	private DtOmnibus crearDt(DtOmnibusCargaMasiva omnibusLeido) throws Exception {
		DtOmnibus busDt = new DtOmnibus();
		busDt.setCapacidad(omnibusLeido.getCapacidad());
		if(!omnibusRepository.existsByNroCoche(omnibusLeido.getNroCoche())) {
			busDt.setNro_coche(omnibusLeido.getNroCoche());
		} else {
			throw new Exception("Ya existe coche con ese numero.");
		}
		busDt.setActivo(omnibusLeido.isActivo());
		busDt.setFecha_fin(java.sql.Date.valueOf(omnibusLeido.getFechaFin()));
		return busDt;
	}

	private DtOmnibus crearOmnibus(DtOmnibus omnibusDt, DtOmnibusCargaMasiva omnibusLeido) throws Exception {
		Omnibus bus = dtoToEntity(omnibusDt);
		Localidad loc = localidadRepository.findByDepartamentoNombreAndNombre(omnibusLeido.getNombreDepartamento(), omnibusLeido.getNombreLocalidad());
		if(loc != null) {
			bus.setLocalidad_actual(loc);			
		} else {
			throw new Exception("La localidad indicada no existe.");
		}
		
		Omnibus guardado = omnibusRepository.save(bus);

        List<Asiento> asientos = new ArrayList<>();
        for (int i = 1; i <= bus.getCapacidad(); i++) {
            Asiento asiento = new Asiento();
            asiento.setNumeroAsiento(i);
            asiento.setOmnibus(bus);
            asientos.add(asiento);
        }

        asientoRepository.saveAll(asientos);
        
        bus.setAsientos(asientos);
        
        DtOmnibus creado = entityToDto(guardado);
		return creado;
	}

	@Override
	public ResultadoOperacion<?> buscarOmnibusDisponibles(int idViaje) {
		List<DtOmnibus> aMostrar = new ArrayList<DtOmnibus>();
		Viaje viajeEnviado = viajeRepository.findById(idViaje).get();
		if(viajeEnviado != null) {
			if(viajeEnviado.getEstado().equals(EstadoViaje.CERRADO)) {
				return new ResultadoOperacion<>(false, "No se puede reasignar un viaje cerrado", ErrorCode.REQUEST_INVALIDO);
			} else if (viajeEnviado.getEstado().equals(EstadoViaje.CANCELADO)) {
				return new ResultadoOperacion<>(false, "No se puede reasignar un viaje cancelado", ErrorCode.REQUEST_INVALIDO);
			}
			Localidad locOrigen = viajeEnviado.getLocalidadOrigen();
			java.sql.Date fechaPartida = viajeEnviado.getFecha_partida();
			Time horaPartida = viajeEnviado.getHora_partida();
			int cantidadPasajes = disAsientosRepository.countByViajeAndEstado(viajeEnviado, EstadoAsiento.OCUPADO);
			java.sql.Date fechaLlegada = viajeEnviado.getFecha_llegada();
			Time horaLlegada = viajeEnviado.getHora_llegada();
			List<Integer> idsAgregados = new ArrayList<Integer>();
			List<Omnibus> omnibusSinViajesEnRango = viajeRepository.omnibusSinViajes(fechaPartida, horaPartida, fechaLlegada, horaLlegada, locOrigen.getId_localidad());
			for(Omnibus oSinViajeRango : omnibusSinViajesEnRango) {
				if(!oSinViajeRango.equals(viajeEnviado.getOmnibus()) 
						&& oSinViajeRango.getCapacidad() >= cantidadPasajes
						&& oSinViajeRango.isActivo()) {
					DtOmnibus oDt = entityToDtoSinViajes(oSinViajeRango);
					aMostrar.add(oDt);
					idsAgregados.add(oDt.getId_omnibus());
				}
			}
			
			List<Omnibus> noSalieronAun = omnibusRepository.omnibusNoHanSalido(locOrigen.getId_localidad());
			if (!noSalieronAun.isEmpty()) {
				for (Omnibus oNoSalio : noSalieronAun) {
					if (!oNoSalio.equals(viajeEnviado.getOmnibus()) && oNoSalio.getCapacidad() >= cantidadPasajes) {
						DtOmnibus oDt = entityToDtoSinViajes(oNoSalio);
						if(!idAgregado(idsAgregados, oDt.getId_omnibus())) {
							aMostrar.add(oDt);
							idsAgregados.add(oDt.getId_omnibus());
						}
					}
				}
			}
			
			List<Omnibus> omnibusSinViajesAsignados = omnibusRepository.omnibusSinViajeAsignado(locOrigen);
			if(omnibusSinViajesAsignados != null && !omnibusSinViajesAsignados.isEmpty()) {
				for(Omnibus oNoAsignado : omnibusSinViajesAsignados) {
					if(!oNoAsignado.equals(viajeEnviado.getOmnibus()) 
							&& oNoAsignado.getCapacidad() >= cantidadPasajes
							&& oNoAsignado.isActivo()) {
						DtOmnibus oDt = entityToDtoSinViajes(oNoAsignado);
						if(!idAgregado(idsAgregados, oDt.getId_omnibus())) {
							aMostrar.add(oDt);
							idsAgregados.add(oDt.getId_omnibus());
						}
						
					}
				}
			}
				
			if(!aMostrar.isEmpty())
				return new ResultadoOperacion<>(true, "Ómnibus disponibles ", aMostrar);
			else
				return new ResultadoOperacion<>(false, "No hay ómnibus disponibles.", ErrorCode.LISTA_VACIA);
		} else {
			return new ResultadoOperacion<>(false, "No existe viaje con ese ID.", ErrorCode.REQUEST_INVALIDO);
		}
	}
	
	private boolean idAgregado(List<Integer> idsAgregados, int id_omnibus) {
		boolean agregado = false;
		for (int idBus : idsAgregados) {
			if (idBus == id_omnibus) {
				agregado = true;
				break;
			}
		}
		return agregado;
	}

	private DtOmnibus entityToDtoSinViajes(Omnibus omnibus) {
        DtOmnibus dto = new DtOmnibus();
        dto.setActivo(omnibus.isActivo());
        dto.setCapacidad(omnibus.getCapacidad());
        dto.setId_omnibus(omnibus.getId_omnibus());
        dto.setNro_coche(omnibus.getNro_coche());
        

        if (omnibus.getLocalidad_actual() != null) {
            dto.setId_localidad_actual(omnibus.getLocalidad_actual().getId_localidad());
        }
		return dto;
	}

	@Override
	public ResultadoOperacion<?> calcularPorcentajeAsignados() {
		int totalOmnibus = (int) omnibusRepository.count();
		int omnibusAsignados = 0;
		int omnibusSinAsignar = 0;
		List<Omnibus> omnibus = (List<Omnibus>) omnibusRepository.findAll();
		List<Omnibus> conViajes = viajeRepository.obtenerOmnibusAsignados();
							
		List<Omnibus> sinAsignar = omnibus.stream()
			    .filter(bus -> !conViajes.contains(bus))
			    .collect(Collectors.toList());
		
		if(conViajes.size()>0)
			omnibusAsignados = conViajes.size();
		if(sinAsignar.size() > 0)
			omnibusSinAsignar = sinAsignar.size();
		DtPorcentajeOmnibusAsignados dtResultado = crearDtOmnibusAsignados(totalOmnibus, omnibusAsignados, omnibusSinAsignar);
		if(dtResultado != null)
			return new ResultadoOperacion(true, "Estadistica obtenida correctamente", dtResultado);
		else 
			return new ResultadoOperacion(false, "No hay datos disponibles para esta estadística", null);
	}

	private DtPorcentajeOmnibusAsignados crearDtOmnibusAsignados(int totalOmnibus, int omnibusAsignados, int omnibusSinAsignar) {
		DtPorcentajeOmnibusAsignados dtAsignados = new DtPorcentajeOmnibusAsignados();
		dtAsignados.setAsignados(omnibusAsignados);
		dtAsignados.setNoAsignados(omnibusSinAsignar);
		dtAsignados.setTotalOmnibus(totalOmnibus);
		dtAsignados.setPorcentajeAsignados(calcularPorcentaje(totalOmnibus, omnibusAsignados));
		dtAsignados.setPorcentajeNoAsignados(100 - dtAsignados.getPorcentajeAsignados());
		return dtAsignados;
	}

	private int calcularPorcentaje(int totalOmnibus, int omnibusAsignados) {
		double porcentaje = 0;
		porcentaje = (omnibusAsignados * 100) / totalOmnibus;
		return (int) Math.round(porcentaje);
	}

	@Override
	public ResultadoOperacion<?> omnibusPorEstadoPorMes() {
		try {
			List<Historico_estado> estado = historicoEstadoRepository.findByActivo(false);
			List<Omnibus> omnibus = (List<Omnibus>) omnibusRepository.findAll();
			
			YearMonth hoy = YearMonth.now();
	        List<YearMonth> mesesDelAño = IntStream.rangeClosed(1, hoy.getMonthValue())
	            .mapToObj(m -> YearMonth.of(hoy.getYear(), m))
	            .collect(Collectors.toList());

	        Map<YearMonth, Long> creadosAlMes = mesesDelAño.stream()
	            .collect(Collectors.toMap(
	                ym -> ym,
	                ym -> omnibus.stream()
	                    .filter(r -> !r.getFechaCreacion().toInstant().atZone(zona).toLocalDate().isAfter(ym.atEndOfMonth()))
	                    .count(),
	                (a, b) -> b,
	                LinkedHashMap::new
	            ));

			
			Map<YearMonth, Long> cambioPorMes = estado.stream()
				    .collect(Collectors.groupingBy(
				        e -> YearMonth.from(e.getFechaInicio()
				                              .toInstant()
				                              .atZone(ZoneId.systemDefault())
				                              .toLocalDate()),
				        Collectors.counting()
				    ));
			
			Set<YearMonth> todosLosMeses = new TreeSet<>();
			todosLosMeses.addAll(creadosAlMes.keySet());
			todosLosMeses.addAll(cambioPorMes.keySet());
			
			List<CantidadesPorMes> cantPorMes = todosLosMeses.stream()
				    .map(mes -> new CantidadesPorMes(
				        mes,
				        creadosAlMes.getOrDefault(mes, 0L),
				        cambioPorMes.getOrDefault(mes, 0L)
				    ))
				    .collect(Collectors.toList());

			
			List<DtOmnibusPorEstadoPorMes> listadoPorMes = cantPorMes.stream()
					.map(est -> completarDt(est.mes(), est.cantidadActivos(), est.cantidadInactivos()))
					.collect(Collectors.toList());

			if(listadoPorMes.size() > 0) {
				return new ResultadoOperacion(true, "Estadistica obtenida correctamente", listadoPorMes);
			} else {
				return new ResultadoOperacion(false, "No hay datos disponibles para esta estadística", null);
			}
		} catch (Exception e) {
			return new ResultadoOperacion(false, e.getMessage(), ErrorCode.OPERACION_INVALIDA);
		}
	}

	private DtOmnibusPorEstadoPorMes completarDt(YearMonth mesAnio, Long cantidadActivos, Long cantidadInactivos) {
		DtOmnibusPorEstadoPorMes estadistica = new DtOmnibusPorEstadoPorMes();
		String[] separarAnioMes = mesAnio.toString().split("-");
		String anio = separarAnioMes[0];
		int anioActual = LocalDate.now().getYear();
		
		if(anio.equals(String.valueOf(anioActual))) {
			estadistica.setAnio(anio);
			estadistica.setMes(separarAnioMes[1]);
			estadistica.setCantidadInactivos(String.valueOf(cantidadInactivos));
			estadistica.setCantidadActivos(String.valueOf(cantidadActivos-cantidadInactivos));
		}
		return estadistica;
	}

	@Override
	public ResultadoOperacion<?> listarOmibusDisponiblesCreacion(DtViaje dtViaje) {
		List<Integer> idsAgregados = new ArrayList<Integer>();
		List<DtOmnibus> aMostrar = new ArrayList<DtOmnibus>();
		Localidad locOrigen = localidadRepository.findById(dtViaje.getLocalidadOrigen().getId_localidad()).get();
		java.sql.Date fechaPartida = java.sql.Date.valueOf(dtViaje.getFecha_partida());
		Time horaPartida = Time.valueOf(dtViaje.getHora_partida());
		java.sql.Date fechaLlegada = java.sql.Date.valueOf(dtViaje.getFecha_llegada());
		Time horaLlegada = Time.valueOf(dtViaje.getHora_llegada());
				
		List<Omnibus> omnibusSinViajesEnRango = viajeRepository.omnibusSinViajes(fechaPartida, horaPartida,
				fechaLlegada, horaLlegada, locOrigen.getId_localidad());
		for (Omnibus o : omnibusSinViajesEnRango) {
			DtOmnibus oDt = entityToDtoSinViajes(o);
			aMostrar.add(oDt);
			idsAgregados.add(oDt.getId_omnibus());
		}
		
		List<Omnibus> noSalieronAun = omnibusRepository.omnibusNoHanSalido(locOrigen.getId_localidad());
		if(!noSalieronAun.isEmpty()) {
			for(Omnibus oNoSalio : noSalieronAun) {
				DtOmnibus oDt = entityToDtoSinViajes(oNoSalio);
				if(!idAgregado(idsAgregados, oDt.getId_omnibus())) {
					aMostrar.add(oDt);
					idsAgregados.add(oDt.getId_omnibus());
				}
			}
		}
		
		List<Omnibus> omnibusSinViajesAsignados = omnibusRepository.omnibusSinViajeAsignado(locOrigen);
		if (omnibusSinViajesAsignados != null && !omnibusSinViajesAsignados.isEmpty()) {
			for(Omnibus oNoAsignado : omnibusSinViajesAsignados) {
				DtOmnibus oDt = entityToDtoSinViajes(oNoAsignado);
				if(!idAgregado(idsAgregados, oDt.getId_omnibus())) {
					aMostrar.add(oDt);
					idsAgregados.add(oDt.getId_omnibus());
				}
			}
		}
		
		if (!aMostrar.isEmpty())
			return new ResultadoOperacion<>(true, "Ómnibus disponibles ", aMostrar);
		else
			return new ResultadoOperacion<>(false, "No hay ómnibus disponibles.", ErrorCode.LISTA_VACIA);

	}
}
