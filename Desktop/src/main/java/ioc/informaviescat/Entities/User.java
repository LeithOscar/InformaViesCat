package ioc.informaviescat.Entities;

/**
 *
 * @author leith
 *
 * Clase user, defineix les propietat que te un usuari y si esta o no conectat
 */
public class User {

    public int id = 0;
    public String name = null;
    public String userName = null;
    public String lastName = null;
    public String email = null;
    public String password = null;
    public boolean logged;
    public Integer rolId;

    public User() {
    }

    public User(int id, int rolId, String name, String password, Boolean connected, String userName,
            String lastName, String email) {
        this.id = id;
        this.rolId = rolId;
        this.name = name;
        this.userName = userName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.logged = connected;
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
    
}
