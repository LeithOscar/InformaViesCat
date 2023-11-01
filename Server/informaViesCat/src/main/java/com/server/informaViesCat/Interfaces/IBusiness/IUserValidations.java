package com.server.informaViesCat.Interfaces.IBusiness;

/**
 *
 * @author leith
 */
public interface IUserValidations {

    public boolean IsAdmin(int rolId);

    public boolean IsTecnic(int rolId);

    public boolean IsUser(int rolId);
}
