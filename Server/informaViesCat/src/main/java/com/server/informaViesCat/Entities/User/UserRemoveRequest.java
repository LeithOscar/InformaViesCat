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
public class UserRemoveRequest implements Serializable {

    public String sessionId;
    public int userId;

    public UserRemoveRequest() {
    }

    public UserRemoveRequest(String sessionId, int userId) {

        this.sessionId = sessionId;
        this.userId = userId;
    }

   
}
