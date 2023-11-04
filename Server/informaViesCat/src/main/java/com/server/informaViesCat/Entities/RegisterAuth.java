
package com.server.informaViesCat.Entities;

/**
 *
 * @author leith
 */
public class RegisterAuth {
     private int id;
    private boolean activo;

    public RegisterAuth(int id, boolean activo) {
        this.id = id;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public boolean isActivo() {
        return activo;
    }
}
