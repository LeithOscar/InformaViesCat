package com.server.informaViesCat.Entities.Incident;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

/**
 *
 * @author leith
 */
public class IncidentGetAllRequest {

    public int rolid;
    public int userid;
    public String sessionid;

    public IncidentGetAllRequest() {
    }

    public IncidentGetAllRequest(int userid, int rolid, String sessionid) {
        this.sessionid = sessionid;
        this.userid = userid;
        this.rolid = rolid;
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
}
