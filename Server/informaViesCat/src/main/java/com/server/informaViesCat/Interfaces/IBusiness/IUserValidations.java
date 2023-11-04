package com.server.informaViesCat.Interfaces.IBusiness;

/**
 *
 * @author leith
 */
public interface IUserValidations {

    public boolean IsAdmin(int rolId);

    public boolean isTechnician(int rolId);

    public boolean IsUser(int rolId);
}
