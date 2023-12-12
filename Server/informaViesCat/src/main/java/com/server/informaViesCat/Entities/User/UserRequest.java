package com.server.informaViesCat.Entities.User;

import java.io.Serializable;

/**
 *
 * @author leith
 *
 */
public class UserRequest implements Serializable {

    public String sessionId;
    public User user;

    public UserRequest() {
    }

    public UserRequest(String sessionId, User user) {

        this.sessionId = sessionId;
        this.user=user;
    }

}
