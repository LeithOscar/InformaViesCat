package com.server.informaViesCat.Entities.User;

import java.io.Serializable;

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

}
