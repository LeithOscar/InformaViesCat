package com.server.informaViesCat.Entities.Incident;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

/**
 *
 * @author leith
 */
public class GetAllDescriptionContainsRequest {

    public String contains;
    public String sessionid;

    public GetAllDescriptionContainsRequest() {
    }

    public GetAllDescriptionContainsRequest(String sessionid,String contains) {
        this.sessionid = sessionid;
        this.contains = contains;
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
