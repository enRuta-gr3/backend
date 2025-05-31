package PRUEBAS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class probarBD {
	
    public static void main(String[] args) {
        String url = "jdbc:postgresql://db.zvynuwmrfmktqwhdjpoe.supabase.co:5432/postgres";
        String user = "postgres";
        String password = "8P8WFmTzu4VuDlW0";

        System.out.println("🔌 Intentando conectar a Supabase...");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a Supabase!");
            } else {
                System.out.println("⚠️ Conexión fallida (sin excepción pero sin conexión).");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar con Supabase:");
            e.printStackTrace();
        }
    }
}
