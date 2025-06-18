package com.uy.enRutaBackend;

import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import PRUEBAS.MOSTRARDATOS;

@SpringBootApplication
@ComponentScan(basePackages = {"com.uy.enRutaBackend"})
public class EnRutaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnRutaBackendApplication.class, args);
    }

  /*
   @Bean
   public CommandLineRunner run(MOSTRARDATOS mostrardatos) {
        return args -> mostrardatos.ejecutar();
    }
  */
}
