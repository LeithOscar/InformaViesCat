/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.server.informaViesCat.Interfaces.IBusiness;

import com.server.informaViesCat.Entities.User.User;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IUserBusiness {

    public User Login(String UserName, String password);

    User Logout(String UserName, String password);

    boolean CreateNewUser(User user);

    List<User> GetAll();

    boolean Modify(User user);
    
    boolean Delete (int id);

}
