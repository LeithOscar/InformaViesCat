/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.server.informaViesCat.Entities.User;

import com.server.informaViesCat.Entities.Incident.RolTypes;
import com.server.informaViesCat.Interfaces.IBusiness.IUserValidations;

/**
 *
 * @author leith
 */
public class UserValidations implements IUserValidations{

    public  boolean IsAdmin(int rolId) {
        return RolTypes.ADMIN.getValue() == rolId;
    }

    public  boolean isTechnician(int rolId) {
        return RolTypes.TECNIC.getValue() == rolId;
    }

    public  boolean IsUser(int rolId) {
        return RolTypes.USER.getValue() == rolId;
    }
}
