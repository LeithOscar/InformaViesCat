package com.server.informaViesCat.Repository;

import com.server.informaViesCat.Configuration.ConnectionBD;
import com.server.informaViesCat.Entities.User.User;
import com.server.informaViesCat.Interfaces.IRepository.IUserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserRepository
 *
 * Aquesta clase centralitza tots els accesos a la base de dades de la entitat
 * User
 */
public class UserRepository implements IUserRepository {

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
    public User GetByUsernameAndPassword(String userName, String password) {

        try {
            return this.GetUser(userName, password);
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Actualitza islogged al argument state indicat
     *
     * @param user username del usuari
     * @param state Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User UpdateIsLogged(User user, boolean state) {

        if (user != null) {

            try {
                String updateUser = "UPDATE Users SET isLogged = " + state + " WHERE Id =" + user.getId();
                PreparedStatement updateStmt = bdConnection.prepareStatement(updateUser);
                updateStmt.executeUpdate();

                User userUpdated = this.GetUser(user.getUserName(), user.getPassword());

                return userUpdated;

            } catch (SQLException ex) {
                Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return user;

    }

    public void CreateNewUser(User user) {

        try {
            String consultaSQL = "INSERT INTO users (rolId, name, lastName, userName, password,email, islogged, parentId ) VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            pstmt.setInt(1, user.getRolId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getUserName());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.GetEmail());
            pstmt.setBoolean(7, user.isLogged());
            pstmt.setInt(8, user.getParentId());

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean Exist(String email) {

        User user = null;
        String consultaSQL = "SELECT * FROM Users WHERE Email = ?";

        PreparedStatement pstmt;
        try {
            pstmt = bdConnection.prepareStatement(consultaSQL);

            pstmt.setString(1, email);

            ResultSet result = pstmt.executeQuery();
            while (result.next()) {

                user = this.ExtractUserFromResult(result);
            }
            return user != null;

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    public boolean Exist(int id) {
        String consultaSQL = "SELECT Id"
                + "	FROM Users \n"
                + "	WHERE Id ='" + id + "';";

        PreparedStatement pstmt;
        try {
            pstmt = bdConnection.prepareStatement(consultaSQL);
            pstmt.executeQuery();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<User> GetAll(int parentId) {
        List<User> users = new ArrayList<>();
        String consultaSQL = "SELECT * FROM users WHERE parentId =" + parentId + " ;";
        PreparedStatement pstmt;
        try {
            pstmt = bdConnection.prepareStatement(consultaSQL);
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {

                users.add(ExtractUserFromResult(result));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return users;
    }

    public boolean Modify(User user) {

        try {
            String consultaSQL = "UPDATE users\n"
                    + "SET rolId = ?, name = ?, lastName = ?, userName = ?, password = ?, email = ?, islogged = ?\n"
                    + "WHERE id = ?;";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            pstmt.setInt(1, user.getRolId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getUserName());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.GetEmail());
            pstmt.setBoolean(7, user.isLogged());
            pstmt.setInt(8, user.getId());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean Delete(int id) {

        try {

            if (this.Exist(id)) {

                String consultaSQL = "DELETE FROM Users WHERE id=" + id;

                PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

                pstmt.execute();
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    public User GetByUsername(String username) {

        try {
            String consultaSQL = "SELECT u.*"
                    + "	FROM Users u\n"
                    + "	WHERE u.UserName ='" + username + "';";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            ResultSet result = pstmt.executeQuery();

            User user = null;

            while (result.next()) {
                user = ExtractUserFromResult(result);
            }

            return user;
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private User ExtractUserFromResult(ResultSet result) {

        User user = null;
        try {
            int _id = result.getInt("id");
            int parentId = result.getInt("parentId");
            String _name = result.getString("Name");
            String _lastName = result.getString("LastName");
            String _userName = result.getString("UserName");
            String _email = result.getString("Email");
            String _pass = result.getString("Password");
            int _rolId = result.getInt("rolId");
            boolean _isLogged = result.getBoolean("isLogged");

            user = new User(_id, _rolId, _name, _pass, _isLogged, _userName, _lastName, _email, parentId);

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
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
            user = ExtractUserFromResult(result);
        }

        return user;

    }

    public User GetById(int userId) {
        User user = null;

        try {
            String consultaSQL = "SELECT u.*\n"
                    + "	FROM Users u\n"
                    + "	WHERE u.id ='" + userId + "';";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                user = ExtractUserFromResult(result);
            }

            return user;
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

}
