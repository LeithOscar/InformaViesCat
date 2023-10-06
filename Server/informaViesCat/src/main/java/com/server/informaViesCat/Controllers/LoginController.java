package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Repository.UserRepository;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author leith
 */
@RestController
public class LoginController {

    /**
     * Conecta el usuari
     *
     * @param username username del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("login/{username}/{pass}")
    public User login(@PathVariable String username, @PathVariable String pass) throws SQLException {

       // User autenticarUsuario = ManageUsers.autenticarUsuario(username, pass);
        
        UserRepository userRepository = new UserRepository();
        User userObtained = userRepository.getByUsernameAndPassword(username, pass);

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

        //User autenticarUsuario = ManageUsers.desconectarUsuario(username, pass);
        //return autenticarUsuario;
        return null;
    }

}
