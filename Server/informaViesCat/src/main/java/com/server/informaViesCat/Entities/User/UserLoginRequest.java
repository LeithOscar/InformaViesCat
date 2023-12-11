package com.server.informaViesCat.Entities.User;

import java.io.Serializable;

/**
 *
 * @author leith
 * 
 */
public class UserLoginRequest implements Serializable {

    public String  UserName;
    public String Password;

    public UserLoginRequest(){}
    public UserLoginRequest(String UserName, String password) {
        this.UserName = UserName;
        this.Password = password;
    }

}
