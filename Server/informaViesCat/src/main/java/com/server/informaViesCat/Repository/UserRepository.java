/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.server.informaViesCat.Repository;

import com.server.informaViesCat.CConnection;
import com.server.informaViesCat.Entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author leith
 */
public class UserRepository {

    private Connection conexion;

    public UserRepository() {

        conexion = CConnection.conectar();

    }

    public User getByUsernameAndPassword(String username, String password) throws SQLException {

        String consultaSQL = "SELECT u.*, r.RolName\n"
                + "	FROM Users u\n"
                + "	JOIN Rol r ON u.RolId = r.id\n"
                + "	WHERE u.UserName ='" + username + "' AND u.Password = '" + password + "';";

        PreparedStatement pstmt = conexion.prepareStatement(consultaSQL);

        ResultSet result = pstmt.executeQuery();

        User user = null;

        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("Name");
            String lastName = result.getString("LastName");
            String userName = result.getString("UserName");
            String email = result.getString("Email");
            String pass = result.getString("Password");

            user = new User(id, name, pass, true, userName, lastName, email);
        }

        if (user != null) {
            return user;
        } else {
            return new User(0, "", "", false, "", "", "");
        }

    }
}
