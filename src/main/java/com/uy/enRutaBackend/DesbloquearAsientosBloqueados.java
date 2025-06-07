package com.uy.enRutaBackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.uy.enRutaBackend.icontrollers.IServiceAsiento;

@Component
public class DesbloquearAsientosBloqueados {

	private final IServiceAsiento serviceAsiento;
	@Value("${intervalo.ejecucion.programada}")
	private long intervaloEjecucion;
	
	public DesbloquearAsientosBloqueados(IServiceAsiento serviceAsiento) {
		this.serviceAsiento = serviceAsiento;
	}
	
//	@Scheduled(fixedRate = 120000)
	@Scheduled(fixedRateString = "${intervalo.ejecucion.programada}")
	public void validarDescargarAsientos() {
		serviceAsiento.desbloquearPorTiempo();
	}
}
