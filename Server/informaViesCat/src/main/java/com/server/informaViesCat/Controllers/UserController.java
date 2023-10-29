package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.UserBusiness;
import com.server.informaViesCat.Entities.User;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author leith Controlador d'accès per el login i el logout
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserBusiness userBusiness = null;

    public UserController() {

        this.userBusiness = new UserBusiness();
    }

    /**
     * Conecta el usuari
     *
     * @param username username del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("/login/{username}/{pass}")
    public ResponseEntity<User> login(@PathVariable String username, @PathVariable String pass) {

        User userObtained = null;

        userObtained = userBusiness.Login(username, pass);
        if (userObtained != null) {
            return ResponseEntity.ok(userObtained);
        }

        return (ResponseEntity<User>) ResponseEntity.badRequest();

    }

    /**
     * Desconecta el usuari
     *
     * @param username username del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("/logout/{username}/{pass}")
    public ResponseEntity<User> logout(@PathVariable String username, @PathVariable String pass) {

        User userObtained = null;

        userObtained = userBusiness.Logout(username, pass);
        if (!userObtained.isLogged()) {
            return ResponseEntity.ok(userObtained);
        }

        return (ResponseEntity<User>) ResponseEntity.noContent();
    }

    /**
     * Obté tots els usuaris
     *
     * @return llistat dels usuarios
     */
    @GetMapping("/getall")
    public ResponseEntity<List<User>> getAll() {

        var userList = userBusiness.GetAll();
        if (userList != null) {
            return ResponseEntity.ok(userList);
        }
        return (ResponseEntity<List<User>>) ResponseEntity.noContent();

    }

    /**
     * Crea el usuari
     *
     * @param user username del usuari
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/create")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> create(@RequestBody User user) {
        if (userBusiness.CreateNewUser(user)) {
            return ResponseEntity.ok("Usuari creat.");

        } else {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("El recurs ja existeix");
        }
    }

    /**
     * Crea el usuari
     *
     * @param user username del usuari
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    
    //TODO només els admins o tecnic o podem fer els cambis
    @PutMapping("/modify")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> modify(@RequestBody User user) {
        if (userBusiness.Modify(user)) {
            return ResponseEntity.ok("Usuari modificat.");

        } else {
           return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No es pot modificar");
        }
    }

    /**
     * Elimina el usuari
     *
     * @param id id del usuari
     * @return Retorna missagte si ha elimnat OK o un badrequest
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (userBusiness.Delete(id)) {
            return ResponseEntity.ok("Usuari eliminat.");

        } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix");

        }
    }
}
