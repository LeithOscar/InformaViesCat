
package ioc.informaviescat.Entities;

/**
 *
 * @author Pau Cors Bardolet
 */
public class Response {
    User user;
    String sessionId;
    
    public User getUser(){
        return user;
    }
    
    public String getSessionId(){
        return sessionId;
    }
}
