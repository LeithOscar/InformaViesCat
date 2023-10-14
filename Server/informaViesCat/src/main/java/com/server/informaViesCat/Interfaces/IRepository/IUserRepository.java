/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.server.informaViesCat.Interfaces.IRepository;

import com.server.informaViesCat.Entities.User;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IUserRepository {

    User GetByUsernameAndPassword(String userName, String password);

    User UpdateIsLogged(User user, boolean state);

    void CreateNewUser(User user);

    int Exist(String email);

    List<User> GetAll();

}
