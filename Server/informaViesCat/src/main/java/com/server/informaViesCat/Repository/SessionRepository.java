package com.server.informaViesCat.Repository;

import com.server.informaViesCat.Configuration.ConnectionBD;
import com.server.informaViesCat.Interfaces.IRepository.ISessionRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leith
 *
 * Repositori per el acces a les taula session
 */
public class SessionRepository implements ISessionRepository {

    private Connection bdConnection;

    public SessionRepository() {

        bdConnection = ConnectionBD.conectar();
    }

    @Override
    public boolean AddSession(String sessionId, int userId) {
        try {
            String consultaSQL = "INSERT INTO session (SessionId, lastConnection,userId) VALUES (?,?,?)";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            pstmt.setString(1, sessionId.toString());
            Date utilDate = new java.util.Date();

            Date now = new Date(utilDate.getTime());
            pstmt.setString(2, now.toString());
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public boolean CloseSession(String sessionId) {

        try {
            String consultaSQL = "DELETE FROM Session WHERE SessionId = ?";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            pstmt.setString(1, sessionId);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public boolean IsActive(String sessionId) {
        
        boolean isActive = false;
       
        try {
            String consultaSQL = "SELECT * FROM session WHERE sessionId = '" + sessionId + "';";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            ResultSet result = pstmt.executeQuery();
            while (result.next()) {
                    
                return isActive = true;
            }
            return isActive;

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

}
