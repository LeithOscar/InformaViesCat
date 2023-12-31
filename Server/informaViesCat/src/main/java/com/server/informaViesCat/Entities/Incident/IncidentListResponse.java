package com.server.informaViesCat.Entities.Incident;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author leith Entitat que es retorna amb la peticio Login Retorna Entitat
 * incidents i el ID de la sesió
 */
public class IncidentListResponse implements Serializable {

    public List<Incident> incidents;
    public String sessionid;

    public IncidentListResponse(List<Incident> incidents, String sessionId) {
        this.incidents = incidents;
        this.sessionid = sessionId;
    }

    public JSONObject convertObjectToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(this);
            return new JSONObject(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
            return null;
        }
    }

}
