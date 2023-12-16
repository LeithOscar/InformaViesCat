package com.server.informaViesCat.Repository;

import com.server.informaViesCat.Configuration.ConnectionBD;
import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentCriteriaRequest;
import com.server.informaViesCat.Interfaces.IRepository.IIncidentRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
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

            pstmt.setInt(1, incident.getUserid());
            pstmt.setInt(2, incident.getTecnicid());
            pstmt.setString(3, incident.getRaodname());
            pstmt.setString(4, incident.getKm());
            pstmt.setString(5, incident.getgeo());
            pstmt.setString(6, incident.getdescription());
            pstmt.setString(7, incident.getStartdate());
            pstmt.setString(8, incident.getEnddate());
            pstmt.setBoolean(9, incident.isurgent());
            pstmt.setInt(10, incident.getIncidenttypeid());

            pstmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public boolean Modify(Incident Incident) {

        try {
            String consultaSQL = "UPDATE Incidents\n"
                    + "SET Carretera = ?, Km = ?, Geo = ?, Description = ?, StartDate = ?, EndDate = ?, Urgent = ?, incidentTypeId = ?\n"
                    + "WHERE Id = ?;";

            PreparedStatement pstmt = bdConnection.prepareStatement(consultaSQL);

            pstmt.setString(1, Incident.getRaodname());
            pstmt.setString(2, Incident.getKm());
            pstmt.setString(3, Incident.getgeo());
            pstmt.setString(4, Incident.getdescription());
            pstmt.setString(5, Incident.getStartdate());
            pstmt.setString(6, Incident.getEnddate());
            pstmt.setBoolean(7, Incident.isurgent());
            pstmt.setInt(8, Incident.getIncidenttypeid());
            pstmt.setInt(9, Incident.getId());

            pstmt.executeUpdate();
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean Delete(int id) {

        try {

            if (this.Exist(id)) {

                String consultaSQL = "DELETE FROM Incidents WHERE id=" + id;

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

    public boolean Exist(int id) {
        try {
            return this.GetIncident(id) != null;
        } catch (SQLException ex) {
            Logger.getLogger(IncidentRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Incident> GetAll(String userId) {

        List<Incident> incidents = new ArrayList<>();
        String consultaSQL = "SELECT * FROM incidents WHERE userId = " + userId + ";";
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

        //Apliquem el filtres a la consulta obtinguda
        //Comparator<Incident> comparadorPorCarretera = GetFieldComparator(incidentRequest);
        //Ordenació
        //Collections.sort(incidents, comparadorPorCarretera);
        return incidents;
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

        //Apliquem el filtres a la consulta obtinguda
        //Comparator<Incident> comparadorPorCarretera = GetFieldComparator(incidentRequest);
        //Ordenació
        //Collections.sort(incidents, comparadorPorCarretera);
        return incidents;
    }

    @Override
    public int GetAllCount() {
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

        return incidents.size();
    }

    private Comparator<Incident> GetFieldComparator(IncidentCriteriaRequest incidentRequest) {
        /* switch (field) {
            case "UserId":
                return Comparator.comparing(Incident::getUserId);
            case "RoadNames":
                return Comparator.comparing(Incident::getRoadName);
            case "StartDate":
                return Comparator.comparing(Incident::getStartDate);
            default:
                throw new IllegalArgumentException("no válido: " + field);
        }*/
        // return Comparator.comparing(Incident::getStartDate);
        return null;
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
            String startDate = result.getString("StartDate");
            String endDate = result.getString("EndDate");
            boolean urgent = result.getBoolean("Urgent");

            incident = new Incident(_id, userId, tecnicId, IncidentTypeId, roadName, km, geo, description, startDate, endDate, urgent);

        } catch (SQLException ex) {
            Logger.getLogger(IncidentRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return incident;
    }

}
