/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.server.informaViesCat.Business;

import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Repository.UserRepository;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserBusiness {

    private UserRepository repo = null;

    public UserBusiness() {

        this.repo = new UserRepository();

    }

    /**
     * login, activa la propiedad islogged a true
     *
     * @param UserName username del usuari
     * @param password Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User login(String UserName, String password) {

        if (!UserName.isEmpty() & !password.isBlank()) {
            try {
                User user = this.repo.GetByUsernameAndPassword(UserName, password);
                if (user != null) {

                    User UserUpdated = this.repo.UpdateIsLogged(user, true);
                    return UserUpdated;

                } else {
                    return new User(0,0, "", "", false, "", "", "");
                }

            } catch (SQLException ex) {
                Logger.getLogger(UserBusiness.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    /**
     * login, desactiva la propiedad islogged a false
     *
     * @param UserName username del usuari
     * @param password Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User Logout(String UserName, String password) {

        if (!UserName.isEmpty() & !password.isBlank()) {
            try {
                User user = this.repo.GetByUsernameAndPassword(UserName, password);
                if (user != null) {

                    User UserUpdated = this.repo.UpdateIsLogged(user, false);
                    return UserUpdated;

                } else {
                    return new User(0,0, "", "", false, "", "", "");
                }

            } catch (SQLException ex) {
                Logger.getLogger(UserBusiness.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;

    }

    /**
     * login, desactiva la propiedad islogged a false
     *
     * @param user username del usuari
     * @return RRetorna true si la operacio es exitosa o false en cas contrari
     */
    public boolean CreateNewUser(User user) {

        if (user != null) {

            var userEmail = this.repo.Exist(user.GetEmail());
            if (userEmail == 0) {

                this.repo.CreateNewUser(user);
                return true;

            } else {
                return false;
            }
        }
        
        return false;

    }

}
