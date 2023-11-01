package com.server.informaViesCat.Repository;

import com.server.informaViesCat.Configuration.ConnectionBD;
import com.server.informaViesCat.Entities.Incident;
import com.server.informaViesCat.Interfaces.IRepository.IIncidentRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IncidentRepository
 *
 * Aquesta clase centralitza tots els accesos a la base de dades de la entitat
 * Incident
 */
public class IncidentRepository implements IIncidentRepository {

    private Connection bdConnection;

    public IncidentRepository() {

        bdConnection = ConnectionBD.conectar();
    }

    public void CreateIncident(Incident incident) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean Modify(Incident Incident) {

        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    public boolean Delete(int id) {

        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    public boolean Exist(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Incident> GetAll() {
        List<Incident> incidents = new ArrayList<>();
        String consultaSQL = "SELECT * FROM incidents";
        PreparedStatement pstmt;
        try {
            pstmt = bdConnection.prepareStatement(consultaSQL);
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {

                incidents.add(ExtractIncidentFromResult(result));
            }
        } catch (SQLException ex) {
            Logger.getLogger(IncidentRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return incidents;
    }

    private Incident GeIncident(int id) throws SQLException {

        String consultaSQL = "SELECT i.*\n"
                + "	FROM Incidents i\n"
                + "	WHERE i.Id ='" + id + "';";

        PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

        ResultSet result = pstmt.executeQuery();

        Incident incident = null;

        while (result.next()) {
            incident = ExtractIncidentFromResult(result);
        }

        return incident;

    }

    private Incident ExtractIncidentFromResult(ResultSet result) {

        Incident incident = null;
        try {
            int _id = result.getInt("id");
            int userId = result.getInt("UserId");
            int tecnicId = result.getInt("TecnicId");
            int IncidentTypeId = result.getInt("IncidentTypeId");

            String roadName = result.getString("Carretera");
            String km = result.getString("Km");
            String geo = result.getString("Geo");
            String description = result.getString("Description");
            Date startDate = result.getDate("StartDate");
            Date endDate = result.getDate("EndDate");
            boolean urgent = result.getBoolean("Urgent");

            incident = new Incident(_id, userId, tecnicId, IncidentTypeId, roadName, km, geo, description, startDate, endDate, urgent);

        } catch (SQLException ex) {
            Logger.getLogger(IncidentRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return incident;
    }

}
