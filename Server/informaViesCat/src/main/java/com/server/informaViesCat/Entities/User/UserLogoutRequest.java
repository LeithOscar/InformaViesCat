package com.server.informaViesCat.Entities.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author leith
 * 
 */
public class UserLogoutRequest implements Serializable {

    public String  sessionId;
    public int userId;

    public UserLogoutRequest(){}
    public UserLogoutRequest(int userId, String sessionId) {
        this.sessionId = sessionId;
        this.userId = userId;
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
