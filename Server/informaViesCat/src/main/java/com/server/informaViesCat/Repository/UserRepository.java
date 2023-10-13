package com.server.informaViesCat.Repository;

import com.server.informaViesCat.Configuration.ConnectionBD;
import com.server.informaViesCat.Entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserRepository
 *
 * Aquesta clase centralitza tots els accesos a la base de dades de la entitat
 * User
 */
public class UserRepository {

    private Connection bdConnection;

    public UserRepository() {

        bdConnection = ConnectionBD.conectar();
    }

    /**
     * GetByUsernameAndPassword
     *
     * @param userName username del usuari
     * @param password Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User GetByUsernameAndPassword(String userName, String password) throws SQLException {

        return this.GetUser(userName, password);
    }

    /**
     * Actualitza islogged al argument state indicat
     *
     * @param user username del usuari
     * @param state Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User UpdateIsLogged(User user, boolean state) throws SQLException {

        if (user != null) {

            String updateUser = "UPDATE Users SET isLogged = " + state + " WHERE Id =" + user.getId();
            PreparedStatement updateStmt = bdConnection.prepareStatement(updateUser);
            updateStmt.executeUpdate();

            User userUpdated = this.GetUser(user.getUserName(), user.getPassword());

            return userUpdated;
        }

        return user;

    }

    private User GetUser(String userName, String password) throws SQLException {

        String consultaSQL = "SELECT u.*, r.RolName\n"
                + "	FROM Users u\n"
                + "	JOIN Rol r ON u.RolId = r.id\n"
                + "	WHERE u.UserName ='" + userName + "' AND u.Password = '" + password + "';";

        PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

        ResultSet result = pstmt.executeQuery();

        User user = null;

        while (result.next()) {
            int _id = result.getInt("id");
            String _name = result.getString("Name");
            String _lastName = result.getString("LastName");
            String _userName = result.getString("UserName");
            String _email = result.getString("Email");
            String _pass = result.getString("Password");
            int _rolId = result.getInt("rolId");
            boolean _isLogged = result.getBoolean("isLogged");

            user = new User(_id, _rolId, _name, _pass, _isLogged, userName, _lastName, _email);
        }

        return user;

    }

    public void CreateNewUser(User user) {

        try {
            String consultaSQL = "INSERT INTO users (rolId, name, lastName, userName, password,email, islogged ) VALUES (?,?,?,?,?,?,?)";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            pstmt.setInt(1, user.getRolId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getUserName());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.GetEmail());
            pstmt.setBoolean(7, user.isLogged());

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int Exist(String email) {
        String consultaSQL = "SELECT Email"
                + "	FROM Users \n"
                + "	WHERE Email ='" + email + "';";

        PreparedStatement pstmt;
        try {
            pstmt = bdConnection.prepareStatement(consultaSQL);
            pstmt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }
}