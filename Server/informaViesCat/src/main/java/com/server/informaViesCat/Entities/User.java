package com.server.informaViesCat.Entities;

/**
 *
 * @author leith
 * 
 * Clase user, defineix les propietat que te un usuari y si esta o no conectat
 */
public class User {

    private final String name;
    private final String credential;
    private boolean logged;
    

    public User(String name, String credencial,Boolean connected) {
        this.name = name;
        this.credential = credencial;
        this.logged = connected;
    }

    public String getName() {
        return name;
    }

    public String getCredential() {
        return credential;
    }

    public boolean isLogged() {
        return logged;
    }

    public void Connect() {
        logged = true;
    }

    public void Disconnect() {
        logged = false;
    }

}
