package com.server.informaViesCat.Entities.User;

import java.io.Serializable;

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
