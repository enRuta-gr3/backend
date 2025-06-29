package com.uy.enRutaBackend;

//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.uy.enRutaBackend"})
public class EnRutaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnRutaBackendApplication.class, args);
    }
}
