package com.uy.enRutaBackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.icontrollers.IServiceSupabase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
public class ServiceSupabase implements IServiceSupabase {

    @Autowired
    private DataSource dataSource;

    @Override
    public void eliminarUsuarioPorEmailSQL(String email) {
        String sqlDeleteIdentities = """
            DELETE FROM auth.identities 
            WHERE user_id = (SELECT id FROM auth.users WHERE email = ?)
        """;

        String sqlDeleteUser = """
            DELETE FROM auth.users 
            WHERE email = ?
        """;

        try (Connection conn = dataSource.getConnection()) {
            // 1. Eliminar de auth.identities
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteIdentities)) {
                stmt.setString(1, email);
                stmt.executeUpdate();
            }

            // 2. Eliminar de auth.users
            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteUser)) {
                stmt.setString(1, email);
                stmt.executeUpdate();
            }

            System.out.println("✅ Usuario eliminado completamente de Supabase por email");

        } catch (Exception e) {
            throw new RuntimeException("❌ Error eliminando usuario por email: " + e.getMessage(), e);
        }
    }
}
