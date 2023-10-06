/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.server.informaViesCat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author leith
 */
public class CConnection {
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
