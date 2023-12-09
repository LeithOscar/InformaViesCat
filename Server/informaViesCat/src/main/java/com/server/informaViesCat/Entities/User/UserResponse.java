
package com.server.informaViesCat.Entities.User;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author leith
 * Entitat que es retorna amb la peticio  Login
 * Retorna Entitat User i el ID de la sesió
 */
public class UserResponse implements Serializable {

    public User user;
    public String sessionId;

    public UserResponse(User user, String sessionId) {
        this.user = user;
        this.sessionId = sessionId;
    }

}
