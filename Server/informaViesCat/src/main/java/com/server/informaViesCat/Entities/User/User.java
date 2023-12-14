package com.server.informaViesCat.Entities.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author leith
 *
 * Clase user, defineix les propietat que te un usuari y si esta o no conectat
 */
public class User implements Serializable {

    private int id = 0;
    private int parentId = 0;
    private String name = null;
    private String userName = null;
    private String lastName = null;
    public String email = null;
    private String password = null;
    private boolean logged;
    private Integer rolId;

    public User() {
    }

    public User(int id, int rolId, String name, String password, Boolean connected, String userName,
            String lastName, String email, int parentId) {
        this.id = id;
        this.rolId = rolId;
        this.name = name;
        this.userName = userName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.logged = connected;
        this.parentId = parentId;

    }

    public int getParentId() {
        return parentId;
    }

    public int getId() {
        return id;
    }

    public int getRolId() {
        return rolId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLogged() {
        return logged;
    }

    public void Connect() {
        logged = true;
    }

    public String GetEmail() {
        return email;
    }

    public void Disconnect() {
        logged = false;
    }

    public void SetStatusLogging(boolean state) {
        this.logged = state;
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

    public static User convertJsonToObject(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, User.class);
        } catch (IOException e) {
            e.printStackTrace(); // Maneja la excepción según tus necesidades
            return null;
        }
    }

}
