
package com.server.informaViesCat.Entities.User;

import java.util.UUID;

/**
 *
 * @author leith
 * Entitat que es retorna amb la peticio  Login
 * Retorna Entitat User i el ID de la sesió
 */
public class UserResponse {

    public User user;
    public UUID sessionId;

    public UserResponse(User user, UUID sessionId) {
        this.user = user;
        this.sessionId = sessionId;
    }

}
