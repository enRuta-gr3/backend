package com.uy.enRutaBackend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtEstadisticaActividadUsuarios;
import com.uy.enRutaBackend.datatypes.DtEstadisticaUsuarios;
import com.uy.enRutaBackend.datatypes.DtPromedioClienteDTO;
import com.uy.enRutaBackend.datatypes.DtUsuario;
import com.uy.enRutaBackend.entities.Administrador;
import com.uy.enRutaBackend.entities.DatosEliminados;
import com.uy.enRutaBackend.entities.Sesion;
import com.uy.enRutaBackend.entities.Usuario;
import com.uy.enRutaBackend.errors.ErrorCode;
import com.uy.enRutaBackend.errors.ResultadoOperacion;
import com.uy.enRutaBackend.icontrollers.IServiceAdmin;
import com.uy.enRutaBackend.icontrollers.IServiceSesion;
import com.uy.enRutaBackend.icontrollers.IServiceSupabase;
import com.uy.enRutaBackend.persistence.AdministradorRepository;
import com.uy.enRutaBackend.persistence.OmnibusRepository;
import com.uy.enRutaBackend.persistence.PasswordResetTokenRepository;
import com.uy.enRutaBackend.persistence.SesionRepository;
import com.uy.enRutaBackend.persistence.UsuarioRepository;
import com.uy.enRutaBackend.persistence.VentaCompraRepository;
import com.uy.enRutaBackend.security.jwt.JwtManager;

@Service
public class ServiceAdmin  implements IServiceAdmin{
	
	private final UsuarioRepository repository;
    private final IServiceSupabase iserviceSupabase;
    private final SesionRepository sesionRepository;
    private final VentaCompraRepository ventaCompraRepository;
        

	@Autowired
	public ServiceAdmin(UsuarioRepository repository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, JwtManager jwtManager, IServiceSesion sesionService, PasswordResetTokenRepository resetTokenRepository, EmailService emailService, IServiceSupabase iserviceSupabase, SesionRepository sesionRepository, VentaCompraRepository ventaCompraRepository) {
		this.repository = repository;
		this.iserviceSupabase = iserviceSupabase;
		this.sesionRepository = sesionRepository;
		this. ventaCompraRepository =  ventaCompraRepository;
	}
	
	
	@Override
	public ResultadoOperacion<?> eliminarUsuarioComoAdmin(String token, DtUsuario datos) {
	    Sesion sesion = sesionRepository.findByAccessToken(token);

	    if (sesion == null || !sesion.isActivo()) {
	        return new ResultadoOperacion<>(false, "Sesión inválida o expirada", ErrorCode.TOKEN_INVALIDO);
	    }

	    Usuario usuarioSesion = sesion.getUsuario();

	    // ✅ Verificar que sea un administrador
	    if (!(usuarioSesion instanceof Administrador)) {
	        return new ResultadoOperacion<>(false, "No tiene permisos para realizar esta acción", ErrorCode.NO_AUTORIZADO);
	    }

	    // ✅ Buscar el usuario a eliminar por email o cédula
	    String identificador = datos.getEmail() != null ? datos.getEmail() : datos.getCi();
	    if (identificador == null) {
	        return new ResultadoOperacion<>(false, "Debe especificar email o cédula para eliminar", ErrorCode.DATOS_INSUFICIENTES);
	    }

	    Usuario userAEliminar = identificador.contains("@")
	        ? repository.findByEmail(identificador)
	        : repository.findByCi(identificador);

	    if (userAEliminar == null) {
	        return new ResultadoOperacion<>(false, "Usuario no encontrado", ErrorCode.SIN_RESULTADOS);
	    }

	    if (userAEliminar.isEliminado()) {
	        return new ResultadoOperacion<>(false, "El usuario ya está eliminado", ErrorCode.USUARIO_YA_ELIMINADO);
	    }

	    if (usuarioSesion.getUuidAuth().equals(userAEliminar.getUuidAuth())) {
	        return new ResultadoOperacion<>(false, "No puede eliminar su propia cuenta", ErrorCode.NO_AUTORIZADO);
	    }

	    if (userAEliminar.getEmail() != null) {
	        try {
	            iserviceSupabase.eliminarUsuarioPorEmailSQL(userAEliminar.getEmail());
	        } catch (Exception e) {
	            return new ResultadoOperacion<>(false, "Error al eliminar el usuario de Supabase", e.getMessage());
	        }
	    }

	    // ✅ Guardar datos en la tabla DatosEliminados
	    DatosEliminados datosEliminados = new DatosEliminados();
	    datosEliminados.setCorreo(userAEliminar.getEmail());
	    datosEliminados.setCedula(userAEliminar.getCi());
	    datosEliminados.setUsuario(userAEliminar); 

	    userAEliminar.setDatosEliminados(datosEliminados); // Esto los vincula (si el cascade está bien configurado)

	    // ✅ Marcar como eliminado en la base
	    userAEliminar.setEliminado(true);
	    userAEliminar.setEmail(null);
	    userAEliminar.setCi(null);

	    repository.save(userAEliminar); // Gracias al cascade, también guarda DatosEliminados

	    return new ResultadoOperacion<>(true, "Usuario eliminado correctamente por administrador", null);
	}

	@Override
	public DtEstadisticaUsuarios obtenerEstadisticasUsuarios() {
	    long total = repository.countByEliminadoFalse();
	    long administradores = repository.countByTipoUsuarioAndEliminadoFalse("ADMINISTRADOR");
	    long vendedores = repository.countByTipoUsuarioAndEliminadoFalse("VENDEDOR");
	    long clientes = repository.countByTipoUsuarioAndEliminadoFalse("CLIENTE");

	    return new DtEstadisticaUsuarios(total, administradores, vendedores, clientes);
	}
	
	@Override
	public DtEstadisticaActividadUsuarios obtenerEstadisticasActividadUsuarios() {
	    // Calculamos fecha hace 30 días
	    LocalDate haceUnMes = LocalDate.now().minusDays(30);
	    Date fechaLimite = java.sql.Date.valueOf(haceUnMes);

	    long activos = repository.countActivos(fechaLimite);
	    long inactivos = repository.countInactivos(fechaLimite);

	    return new DtEstadisticaActividadUsuarios(activos, inactivos);
	}
	
	
	public List<DtPromedioClienteDTO> obtenerPromedioImportePorCliente() {
	    List<Object[]> resultados = ventaCompraRepository.obtenerPromedioImportePorClienteRaw();

	    return resultados.stream()
	        .map(obj -> new DtPromedioClienteDTO(
	            (UUID) obj[0],                         
	            (String) obj[1],                       
	            obj[2] != null ? ((Number) obj[2]).doubleValue() : 0.0
	        ))
	        .toList();
	}
}
