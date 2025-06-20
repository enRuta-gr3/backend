package com.uy.enRutaBackend.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class TareasProgramadasConfig {

	@Bean
	ScheduledExecutorService scheduledExecutorService() {
		return Executors.newScheduledThreadPool(2);
	}

}
