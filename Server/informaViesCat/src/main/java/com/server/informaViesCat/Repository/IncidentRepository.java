package com.server.informaViesCat.Repository;

import com.server.informaViesCat.Configuration.ConnectionBD;
import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Interfaces.IRepository.IIncidentRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public boolean CreateIncident(Incident incident) {
        try {
            String insertSQL = "INSERT INTO Incidents (UserId, tecnicId, Carretera, Km, Geo, Description, StartDate, "
                    + "EndDate, Urgent, incidentTypeId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = bdConnection.prepareStatement(insertSQL);

            pstmt.setInt(1, incident.getUserId());
            pstmt.setInt(2, incident.getTecnicId());
            pstmt.setString(3, incident.getRoadName());
            pstmt.setString(4, incident.getKM());
            pstmt.setString(5, incident.getGeo());
            pstmt.setString(6, incident.getDescription());
            
            pstmt.setDate(7, (java.sql.Date) new Date(incident.getStartDate().getTime()));
            pstmt.setDate(8, (java.sql.Date) new Date(incident.getEndDate().getTime()));

            pstmt.setBoolean(9, incident.isUrgent());
            pstmt.setInt(10, incident.getIncidentTypeId());

            pstmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public boolean Modify(Incident Incident) {

        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    public boolean Delete(int id) {

        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    public boolean Exist(int id) {
        try {
            return this.GetIncident(id) != null;
        } catch (SQLException ex) {
            Logger.getLogger(IncidentRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Incident> GetAll(String filterCriteria) {
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

        Comparator<Incident> comparadorPorCarretera = GetFieldComparator(filterCriteria);

        Collections.sort(incidents, comparadorPorCarretera);

        return incidents;
    }

    private Comparator<Incident> GetFieldComparator(String field) {
        switch (field) {
            case "UserId":
                return Comparator.comparing(Incident::getUserId);
            case "RoadNames":
                return Comparator.comparing(Incident::getRoadName);
            case "StartDate":
                return Comparator.comparing(Incident::getStartDate);
            default:
                throw new IllegalArgumentException("no válido: " + field);
        }
    }

    private Incident GetIncident(int id) throws SQLException {

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
