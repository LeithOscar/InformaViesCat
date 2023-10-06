package com.server.informaViesCat.Entities;

/**
 *
 * @author leith
 *
 * Clase user, defineix les propietat que te un usuari y si esta o no conectat
 */
public class User {

    private final int id;
    private final String name;
    private final String userName;
    private final String lastName;
    private final String email;
    private final String password;
    private boolean logged;

    public User(int id, String name, String password, Boolean connected, String userName, String lastName, String email) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.logged = connected;
    }

    public String getName() {
        return name;
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

    public boolean Exist() {
        return (!this.getName().isEmpty() && !this.getPassword().isBlank()) == true;
    }

    public void Disconnect() {
        logged = false;
    }

}
