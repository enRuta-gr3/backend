package com.uy.enRutaBackend.services;

import com.uy.enRutaBackend.datatypes.usuarioDTO;
import com.uy.enRutaBackend.icontrollers.IServiceUsuario;
import com.uy.enRutaBackend.persistence.persistence;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ServiceUsuario implements IServiceUsuario {

    private final persistence persistence;
    private static final String SUPABASE_URL = "https://zvynuwmrfmktqwhdjpoe.supabase.co";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2eW51d21yZm1rdHF3aGRqcG9lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQzMTM3OTMsImV4cCI6MjA1OTg4OTc5M30.T7zfUyRGDl7lctJyJ98TWrp1crjzlkx5VmX-r_x_t4c";

    public ServiceUsuario(persistence persistence) {
        this.persistence = persistence;
    }
    
      public UUID registrarUsuario(usuarioDTO usuario) {
    	  

    	//Print momentaneo para ver que esta enviando el colo desde el front
        System.out.println("Email recibido: " + usuario.getEmail());
        System.out.println("Contraseña recibida: " + usuario.getContraseña());
        System.out.println("Tipo_usuario recibida: " + usuario.getTipo_usuario());
        System.out.println("Cedula recibida: " + usuario.getCi());
        
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject data = new JSONObject()
            	    .put("nombres", usuario.getNombres())
            	    .put("apellidos",usuario.getApellidos())
            		.put("tipo_usuario", usuario.getTipo_usuario())
            		.put("fecha_nacimiento", usuario.getFecha_nacimiento())
            		.put("eliminado", usuario.isEliminado())
            		.put("ultimo_inicio_sesion", usuario.getUltimo_inicio_sesion())
            		.put("fecha_creacion", usuario.getFecha_creacion())
            		.put("estado_descuento", usuario.isEstado_descuento())            		
            		.put("cedula", usuario.getCi());

            	JSONObject body = new JSONObject()
            	    .put("email", usuario.getEmail())
            	    .put("password", usuario.getContraseña())
            	    .put("data", data);


            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUPABASE_URL + "/auth/v1/signup"))
                .header("Content-Type", "application/json")
                .header("apikey", API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println("🔎 Respuesta Supabase: " + responseBody);

            JSONObject json = new JSONObject(responseBody);

            if (json.has("user")) {
                JSONObject user = json.getJSONObject("user");
                return UUID.fromString(user.getString("id"));
            } else if (json.has("msg") && json.getString("msg").contains("already registered")) {
                System.out.println("⚠️ Usuario ya registrado. Obteniendo UUID...");

                UUID idExistente = buscarUUIDPorEmail(usuario.getEmail());
                if (idExistente != null) {
                    return idExistente;
                } else {
                    System.out.println("❌ Usuario ya existe pero no se pudo obtener su UUID.");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error registrando usuario: " + e.getMessage());
        }
        return null;
    }
      
      public UUID buscarUUIDPorEmail(String email) {
          try {
              HttpClient client = HttpClient.newHttpClient();

              HttpRequest request = HttpRequest.newBuilder()
                  .uri(URI.create(SUPABASE_URL + "/auth/v1/admin/users?email=" + email))
                  .header("Authorization", "Bearer " + API_KEY)
                  .header("apikey", API_KEY)
                  .GET()
                  .build();

              HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

              if (response.statusCode() == 200) {
                  JSONArray data = new JSONArray(response.body());
                  if (!data.isEmpty()) {
                      JSONObject user = data.getJSONObject(0);
                      return UUID.fromString(user.getString("id"));
                  }
              } else {
                  System.out.println("❌ Error buscando UUID: " + response.body());
              }

          } catch (Exception e) {
              e.printStackTrace();
          }
          return null;
      }
          
      public JSONObject login(String email, String password) {
      	
      	//Print momentaneo para ver que esta enviando el colo desde el front
          System.out.println("Email recibido: " + email);
          System.out.println("Contraseña recibida: " + password);
          
          
          try {
              HttpClient client = HttpClient.newHttpClient();

              JSONObject body = new JSONObject()
                      .put("email", email)
                      .put("password", password);

              HttpRequest request = HttpRequest.newBuilder()
                      .uri(URI.create(SUPABASE_URL + "/auth/v1/token?grant_type=password"))
                      .header("Content-Type", "application/json")
                      .header("apikey", API_KEY)
                      .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                      .build();

              HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
              JSONObject json = new JSONObject(response.body());

              if (response.statusCode() == 200) {
                  return json;
              } else {
                  System.out.println("❌ Error en login: " + json.toString());
              }

          } catch (Exception e) {
              e.printStackTrace();
          }
          return null;
      }

      
      /*
    @Override
    public void RegistrarUsuarioVerificado(usuarioDTO usuarioD) {
        if (usuarioD != null && usuarioD.getEmail() != null && !usuarioD.getEmail().isEmpty()) {
            Usuario usuario = switch (usuarioD.getTipo_usuario()) {
                case "CLIENTE" -> new Cliente(usuarioD.getNombre(), usuarioD.getEmail(), usuarioD.getContraseña(), usuarioD.getCedula());
                case "VENDEDOR" -> new Vendedor(usuarioD.getNombre(), usuarioD.getEmail(), usuarioD.getContraseña(), usuarioD.getCedula());
                case "ADMINISTRADOR" -> new Administrador(usuarioD.getNombre(), usuarioD.getEmail(), usuarioD.getContraseña(), usuarioD.getCedula());
                default -> throw new IllegalArgumentException("Tipo de usuario no soportado: " + usuarioD.getTipo_usuario());
            };

            boolean isSaved = persistence.saveUser(usuario);

            if (isSaved) {
                System.out.println("✅ Usuario registrado y persistido correctamente.");
            } else {
                System.out.println("❌ Error al registrar usuario.");
            }

        } else {
            System.out.println("❌ Datos de usuario inválidos.");
        }
    }


      
    public Usuario buscarPorEmail(String email) {
        return persistence.findByEmail(email);
    }
    
       */
}
