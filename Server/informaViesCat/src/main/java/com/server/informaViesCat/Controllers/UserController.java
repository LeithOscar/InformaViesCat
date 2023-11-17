package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.UserBusiness;
import com.server.informaViesCat.Entities.User.User;
import com.server.informaViesCat.Entities.User.UserResponse;
import com.server.informaViesCat.Interfaces.IRepository.ISessionRepository;
import com.server.informaViesCat.Repository.SessionRepository;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ISessionRepository sessionRepo = null;

    public UserController() {

        this.userBusiness = new UserBusiness();
        this.sessionRepo = new SessionRepository();
    }

    /**
     * Conecta el usuari
     *
     * @param username username del usuari
     * @param pass Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("/login/{username}/{pass}")
    public ResponseEntity<UserResponse> login(@PathVariable String username, @PathVariable String pass) {

        User userObtained = null;

        userObtained = userBusiness.Login(username, pass);
        if (userObtained != null) {

            String sessionId = UUID.randomUUID().toString();
            UserResponse userResponse = new UserResponse(userObtained, UUID.randomUUID());
            this.sessionRepo.AddSession(sessionId, userObtained.getId());
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    /**
     * Desconecta el usuari
     *
     * @param sessionId id de la sessio a tancar
     * @param userId id del usuario.
     * @return Retorna una entitat user amb el seu estat
     */
    @GetMapping("/logout/{sessionId}/{userId}")
    public ResponseEntity<Boolean> logout(@PathVariable String sessionId, @PathVariable int userId) {

        User userObtained = null;

        userObtained = userBusiness.Logout(userId);
        if (!userObtained.isLogged()) {
            ResponseEntity.ok(userObtained);
            boolean closed = this.sessionRepo.CloseSession(sessionId);
            return new ResponseEntity<>(closed, HttpStatus.OK);
        }

        return (ResponseEntity<Boolean>) ResponseEntity.noContent();
    }

    /**
     * Obté tots els usuaris
     * @param parentId
     * @return llistat dels usuarios
     */
    @GetMapping("/getall/{parentId}")
    public ResponseEntity<List<User>> getAll(@PathVariable int parentId) {

        var userList = userBusiness.GetAll(parentId);
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
