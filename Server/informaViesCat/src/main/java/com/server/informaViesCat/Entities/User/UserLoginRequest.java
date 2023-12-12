package com.server.informaViesCat.Entities.User;

import java.io.Serializable;

/**
 *
 * @author leith
 * 
 */
public class UserLoginRequest implements Serializable {

    public String  username;
    public String password;

    public UserLoginRequest(){}
    public UserLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
