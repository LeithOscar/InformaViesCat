package com.server.informaViesCat.Entities.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author leith Entitat que es retorna amb la peticio Login Retorna Entitat
 * User i el ID de la sesió
 */
public class UserListResponse implements Serializable {

    public List<User> user;
    public String sessionId;

    public UserListResponse(List<User> user, String sessionId) {
        this.user = user;
        this.sessionId = sessionId;
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
