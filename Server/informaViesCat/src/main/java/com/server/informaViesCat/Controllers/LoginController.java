package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Repository.UserRepository;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author leith Controlador d'accès per el login i el logout
 */
@RestController
public class LoginController {

    private UserRepository userRepository = null;

    public LoginController() {

        this.userRepository = new UserRepository();
    }

    /**
     * Conecta el usuari
     *
     * @param username username del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("login/{username}/{pass}")
    public User login(@PathVariable String username, @PathVariable String pass) {

        User userObtained = null;
        try {
            userObtained = userRepository.GetByUsernameAndPassword(username, pass);

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userObtained;
    }

    /**
     * Desconecta el usuari
     *
     * @param username username del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("logout/{username}/{pass}")
    public User logout(@PathVariable String username, @PathVariable String pass) {

        User userObtained = null;
        try {
            userObtained = userRepository.Logout(username, pass);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userObtained;
    }
    

}
