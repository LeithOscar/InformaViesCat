package com.server.informaViesCat.Entities.Incident;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.informaViesCat.Entities.User.User;
import java.io.IOException;
import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author leith
 */
public class Incident {

    public int id;
    public int userid;
    public int tecnicid;
    public int incidenttypeid;
    public String raodname;
    public String km;
    public String geo;
    public String description;
    public String startdate;
    public String enddate;
    public boolean urgent;

    public Incident() {
    }

    public Incident(int id, int userId, int tecnicId, int incidentTypeId, String roadName, String km, String geo, String description, String startDate, String endDate, boolean urgent) {
        this.id = id;
        this.userid = userId;
        this.tecnicid = tecnicId;
        this.raodname = roadName;
        this.km = km;
        this.geo = geo;
        this.description = description;
        this.startdate = startDate;
        this.enddate = endDate;
        this.urgent = urgent;
        this.incidenttypeid = incidentTypeId;
    }

    // Getters para las propiedades
    public int getId() {
        return this.id;
    }

    public int getUserid() {
        return userid;
    }

    public int getTecnicid() {
        return tecnicid;
    }

    public int getIncidenttypeid() {
        return incidenttypeid;
    }

    public String getRaodname() {
        return raodname;
    }

    public String getKm() {
        return km;
    }

    public String getgeo() {
        return geo;
    }

    public String getdescription() {
        return description;
    }

    public String getStartdate() {
        return this.startdate;
    }

    public String getEnddate() {
        return this.enddate;
    }

    public boolean isurgent() {
        return urgent;
    }

    @Override
    public String toString() {
        return "ObjetoDeCarretera{"
                + "carretera='" + this.getRaodname() + '\''
                + ", longitud=" + this.getgeo()
                + '}';
    }

    public JSONObject convertObjectToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(this);
            return new JSONObject(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Maneja la excepción según tus necesidades
            return null;
        }
    }

    public static Incident convertJsonToObject(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, Incident.class);
        } catch (IOException e) {
            e.printStackTrace(); // Maneja la excepción según tus necesidades
            return null;
        }
    }
}
