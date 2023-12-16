package com.server.informaViesCat.Entities.Incident;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

/**
 *
 * @author leith
 */
public class IncidentGetAllCountResponse {


    public int incidentscount;

    public IncidentGetAllCountResponse() {
    }

    public IncidentGetAllCountResponse(int incidentscount) {
        this.incidentscount = incidentscount;
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
