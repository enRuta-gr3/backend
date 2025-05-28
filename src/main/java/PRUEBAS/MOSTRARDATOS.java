package PRUEBAS;

import org.springframework.stereotype.Component;

import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Asiento;
import com.uy.enRutaBackend.entities.DisAsiento_Viaje;
import com.uy.enRutaBackend.entities.EstadoViaje;
import com.uy.enRutaBackend.entities.Localidad;
import com.uy.enRutaBackend.entities.Omnibus;
import com.uy.enRutaBackend.entities.Pasaje;
import com.uy.enRutaBackend.entities.Viaje;
import com.uy.enRutaBackend.icontrollers.IServiceAsiento;
import com.uy.enRutaBackend.icontrollers.IServiceLocalidad;
import com.uy.enRutaBackend.icontrollers.IServiceOmnibus;
import com.uy.enRutaBackend.icontrollers.IServicePasaje;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;
import com.uy.enRutaBackend.icontrollers.IServiceViaje;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

@Component
public class MOSTRARDATOS {

    private final IServiceUsuario servicioUsuario;
    private final IServicePasaje servicioPasaje;
    private final IServiceAsiento servicioAsiento;
    private final IServiceLocalidad servicioLocalidad;
    private final IServiceOmnibus servicioOmnibus;
    private final IServiceViaje servicioViaje;

    // Inyección de dependencias a través del constructor
    public MOSTRARDATOS(IServiceUsuario controladorUsuario, 
                        IServiceAsiento controladorAsiento,
                        IServiceLocalidad controladorLocalidad,
                        IServiceOmnibus controladorOmnibus,
                        IServicePasaje controladorPasaje,
                        IServiceViaje controladorViaje) {
        this.servicioUsuario = controladorUsuario;
        this.servicioAsiento = controladorAsiento;
        this.servicioLocalidad = controladorLocalidad;
        this.servicioOmnibus = controladorOmnibus;
        this.servicioPasaje = controladorPasaje;
        this.servicioViaje = controladorViaje;
    }

    public void ejecutar() {
        DtUsuario c1 = new DtUsuario("CLIENTE", "5.251.766-1", "Franco Rodrigo", "Pirotto Perez","francorro02@gmail.com", "123456", null, false, null, null, false, false, false);
        try {
			servicioUsuario.registrarUsuario(c1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        cargardatos(c1);
    }


    public void cargardatos(DtUsuario c1) {


        // Registrar Localidades
        Localidad loc1 = new Localidad("Córdoba");
        Localidad loc2 = new Localidad("Rosario");
        Localidad loc3 = new Localidad("Santa Fe");
        Localidad loc4 = new Localidad("Mendoza");

        servicioLocalidad.RegistrarLocalidad(loc1);
        servicioLocalidad.RegistrarLocalidad(loc2);
        servicioLocalidad.RegistrarLocalidad(loc3);
        servicioLocalidad.RegistrarLocalidad(loc4);

        // Registrar Omnibus (con localidad_actual y fecha_fin)
        Omnibus bus1 = new Omnibus(40, 1001, true, Date.valueOf("2025-12-31"), loc1);
        Omnibus bus2 = new Omnibus(30, 1002, true, Date.valueOf("2025-11-30"), loc2);

        servicioOmnibus.RegistrarOmnibus(bus1);
        servicioOmnibus.RegistrarOmnibus(bus2);

     // Registrar Asientos
        for (int i = 1; i <= 5; i++) {
            List<Pasaje> pasajes = new ArrayList<>();
            List<DisAsiento_Viaje> disAsientoViajes = new ArrayList<>();
            Omnibus omnibusAsignado = i <= 3 ? bus1 : bus2;

            Asiento asiento = new Asiento(i, omnibusAsignado, pasajes, disAsientoViajes);
            servicioAsiento.RegistrarAsiento(asiento);
        }

        // Registrar Viajes (ajustados al nuevo constructor)
        Viaje viaje1 = new Viaje(
                Date.valueOf("2025-06-10"), Time.valueOf("08:00:00"),
                Date.valueOf("2025-06-10"), Time.valueOf("13:30:00"),
                2500.0, EstadoViaje.ABIERTO, bus1, loc1, loc2);

        Viaje viaje2 = new Viaje(
                Date.valueOf("2025-06-11"), Time.valueOf("10:00:00"),
                Date.valueOf("2025-06-11"), Time.valueOf("13:15:00"),
                1800.0, EstadoViaje.ABIERTO, bus2, loc2, loc3);

        Viaje viaje3 = new Viaje(
                Date.valueOf("2025-06-12"), Time.valueOf("14:00:00"),
                Date.valueOf("2025-06-12"), Time.valueOf("20:00:00"),
                3200.0, EstadoViaje.ABIERTO, bus1, loc3, loc4);

//        servicioViaje.RegistrarViaje(viaje1);
//        servicioViaje.RegistrarViaje(viaje2);
//        servicioViaje.RegistrarViaje(viaje3);
    }



}
