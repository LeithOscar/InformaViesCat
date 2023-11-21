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
    public String sessionId;

    public User() {
    }

    public User(int id, int rolId, String name, String password, Boolean connected, String userName,
            String lastName, String email, String sessionId) {
        this.id = id;
        this.rolId = rolId;
        this.name = name;
        this.userName = userName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.logged = connected;
        this.sessionId = sessionId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId(){
        return sessionId;
    }
    
    public void setSessionID(String sessionID){
        this.sessionId = sessionID;
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
