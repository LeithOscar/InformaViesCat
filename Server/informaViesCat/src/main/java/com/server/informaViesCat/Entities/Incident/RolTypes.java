
package com.server.informaViesCat.Entities.Incident;

/**
 *
 * @author leith
 */
public enum RolTypes {
    ADMIN(1), TECNIC(2), USER(3);

    private final int valorEntero;

    RolTypes(int valorEntero) {
        this.valorEntero = valorEntero;
    }

    public int getValue() {
        return valorEntero;
    }
}