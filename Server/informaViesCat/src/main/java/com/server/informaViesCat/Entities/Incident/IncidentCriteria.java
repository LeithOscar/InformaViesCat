package com.server.informaViesCat.Entities.Incident;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author leith
 */
public class IncidentCriteria  implements Serializable{

    public String operator;
    public int userid;
    public int tecnicid;
    public int incidenttypeid;
    public String startdate;
    public String enddate;
    public boolean urgent;

    public IncidentCriteria(){}
    
    public  IncidentCriteria(String operator, int userid, int tecnicid, int incidenttypeid, String startdate, String enddate, boolean urgent) {
        this.operator = operator;

        this.userid = userid;
        this.tecnicid = tecnicid;
        this.incidenttypeid = incidenttypeid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.urgent = urgent;
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

    public static IncidentCriteria convertJsonToObject(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, IncidentCriteria.class);
        } catch (IOException e) {
            e.printStackTrace(); // Maneja la excepción según tus necesidades
            return null;
        }
    }
}
