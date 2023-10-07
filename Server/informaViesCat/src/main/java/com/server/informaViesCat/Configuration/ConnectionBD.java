
package com.server.informaViesCat.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    /**
     * Conecta el usuari
     *
     * Clase que es conecta a la base de dades, retorna la conexió 
     */
public class ConnectionBD {
     public static Connection conectar() {
        String url = "jdbc:postgresql://localhost:5432/informaViesCat";
        String usuario = "postgres";
        String contraseña = "1234";
        Connection conexion = null;

        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conexion;
    }
}
