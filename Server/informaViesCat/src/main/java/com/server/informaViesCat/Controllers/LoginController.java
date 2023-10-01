package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.ManageUsers;
import com.server.informaViesCat.Entities.User;

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
     * @param name Nom del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("login/{name}/{pass}")
    public User login(@PathVariable String name, @PathVariable String pass) {

        User autenticarUsuario = ManageUsers.autenticarUsuario(name, pass);

        return autenticarUsuario;
    }

    /**
     * Desconecta el usuari
     *
     * @param name Nom del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("logout/{name}/{pass}")
    public User logout(@PathVariable String name, @PathVariable String pass) {

        User autenticarUsuario = ManageUsers.desconectarUsuario(name, pass);
        return autenticarUsuario;
    }

}
