package com.server.informaViesCat.Repository;

import com.server.informaViesCat.Configuration.ConnectionBD;
import com.server.informaViesCat.Entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        User user = this.GetUser(userName, password);

        if (user != null) {

            User UserUpdated = this.UpdateIsLogged(user, true, bdConnection);
            return UserUpdated;

        } else {
            return new User(0, "", "", false, "", "", "");
        }

    }

    /**
     * GetByUsernameAndPassword
     *
     * @param userName username del usuari
     * @param password Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User Logout(String userName, String password) throws SQLException {

        User user = this.GetUser(userName, password);
        User UserUpdated = this.UpdateIsLogged(user, false, bdConnection);
        return UserUpdated;

    }

    private User UpdateIsLogged(User user, boolean state, Connection connection) throws SQLException {

        if (user != null) {

            String updateUser = "UPDATE Users SET isLogged = " + state + " WHERE Id =" + user.getId();
            PreparedStatement updateStmt = connection.prepareStatement(updateUser);
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
            boolean _isLogged = result.getBoolean("isLogged");

            user = new User(_id, _name, _pass, _isLogged, userName, _lastName, _email);
        }

        return user;

    }
}
