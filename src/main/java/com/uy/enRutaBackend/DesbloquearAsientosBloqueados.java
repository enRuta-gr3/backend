package com.uy.enRutaBackend;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.uy.enRutaBackend.icontrollers.IServiceAsiento;

import jakarta.annotation.PostConstruct;

@Component
public class DesbloquearAsientosBloqueados {

	private final IServiceAsiento serviceAsiento;
	private final ScheduledExecutorService scheduler;
	@Value("${intervalo.ejecucion.programada}")
	private long intervaloEjecucion;
	
	@Autowired
	public DesbloquearAsientosBloqueados(@Lazy IServiceAsiento serviceAsiento, ScheduledExecutorService scheduler) {
		this.serviceAsiento = serviceAsiento;
		this.scheduler = scheduler;
	}

	@PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(this::validarDescargarAsientos, 1, intervaloEjecucion, TimeUnit.MILLISECONDS);
    }
	
	//@Scheduled(fixedRateString = "${intervalo.ejecucion.programada}")
	public void validarDescargarAsientos() {
		System.out.println("Se ejecuta tarea programada para desbloquear asientos");
		serviceAsiento.desbloquearPorTiempo();
	}
}
