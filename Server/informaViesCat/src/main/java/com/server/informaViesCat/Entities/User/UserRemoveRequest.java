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
    public int userid;

    public UserRemoveRequest() {
    }

    public UserRemoveRequest(String sessionid, int userid) {

        this.sessionId = sessionid;
        this.userid = userid;
    }

   
}
