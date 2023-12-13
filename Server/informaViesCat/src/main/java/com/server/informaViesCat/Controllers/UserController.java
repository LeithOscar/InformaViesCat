package com.server.informaViesCat.Controllers;

import com.google.gson.JsonObject;
import com.server.informaViesCat.Business.UserBusiness;
import com.server.informaViesCat.Entities.AESEncryptionService;
import com.server.informaViesCat.Entities.UnauthorizedException;
import com.server.informaViesCat.Entities.User.User;
import com.server.informaViesCat.Entities.User.UserLoginRequest;
import com.server.informaViesCat.Entities.User.UserLogoutRequest;
import com.server.informaViesCat.Entities.User.UserResponse;
import com.server.informaViesCat.Interfaces.IRepository.ISessionRepository;
import com.server.informaViesCat.Repository.SessionRepository;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @param userLoginRequest
     * @return Retorna una entitat user amb el seu estat
     */
    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<String> login(@RequestBody String userLoginRequest) {

        User userObtained = null;

        //map from client
        JSONObject requestJson = AESEncryptionService.decryptToJSONObject(userLoginRequest);

        if (requestJson == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        UserLoginRequest request = new UserLoginRequest(requestJson.getString("username"), requestJson.getString("password"));

        userObtained = userBusiness.Login(request.username, request.password);
        if (userObtained != null) {

            String sessionId = UUID.randomUUID().toString();
            UserResponse userResponse = new UserResponse(userObtained, sessionId);
            this.sessionRepo.AddSession(sessionId, userObtained.getId());

            String encryptedObject = AESEncryptionService.encryptFromJSONObject(userResponse.convertObjectToJson());

            return new ResponseEntity<>(encryptedObject, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/logout")
    @CrossOrigin
    public ResponseEntity logout(@RequestBody String userLogoutRequest) {

        //map from client
        JSONObject requestJson = AESEncryptionService.decryptToJSONObject(userLogoutRequest);

        if (requestJson == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        UserLogoutRequest request = new UserLogoutRequest(requestJson.getInt("userid"), requestJson.getString("sessionid"));

        if (isSessionActive(request.sessionId)) {

            boolean logoutSucces = userBusiness.Logout(request.userId);
            if (logoutSucces) {
                ResponseEntity.ok(logoutSucces);
                boolean closed = this.sessionRepo.CloseSession(request.sessionId);
                return new ResponseEntity<>(closed, HttpStatus.OK);
            }
            return (ResponseEntity<Boolean>) ResponseEntity.noContent();

        } else {
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Obté tots els usuaris
     *
     * @param sessionId
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

    /**
     * Decrypta una entitat user (proves)s
     *
     * @param userEncrypted
     * @return usuari encriptat
     */
    @GetMapping("/decrypt")
    public ResponseEntity<UserResponse> decrypt(@RequestBody String userEncrypted) {

        UserResponse user = (UserResponse) AESEncryptionService.decryptObject(userEncrypted, UserResponse.class);
        return ResponseEntity.ok(user);

    }

    /**
     * Decrypta una entitat user (proves)
     *
     * @param req
     * @return text encriptat
     */
    @GetMapping("/UserLoginRequestEncript")
    public ResponseEntity<String> UserLoginRequestEncript(@RequestBody UserLoginRequest req) {

        String txt = AESEncryptionService.encryptObject(req);
        return ResponseEntity.ok(txt);

    }

    /**
     * Decrypta una entitat user (proves)
     *
     * @param req
     * @return text encriptat
     */
    @GetMapping("/UserLogoutRequestEncript")
    public ResponseEntity<String> UserLogoutRequestEncript(@RequestBody UserLogoutRequest req) {

        String txt = AESEncryptionService.encryptObject(req);
        return ResponseEntity.ok(txt);

    }

    /**
     * Decrypta una entitat user (proves)
     *
     * @param json
     * @return text encriptat
     */
    @PostMapping("/encrypFromJSONObject")
    public ResponseEntity<String> encrypFromJSONObject(@RequestBody JSONObject json) {

        String txt = AESEncryptionService.encryptFromJSONObject(json);
        return ResponseEntity.ok(txt);

    }

    /**
     * Decrypta una entitat user (proves)
     *
     * @param json
     * @return text encriptat
     */
    @PostMapping("/decrytFromJSONObject")
    public ResponseEntity<JSONObject> decrytFromJSONObject(@RequestBody String encryptedData) {

        JSONObject txt = AESEncryptionService.decryptToJSONObject(encryptedData);
        return ResponseEntity.ok(txt);

    }

    private boolean isSessionActive(String sessionId) {

        boolean isActive = sessionRepo.IsActive(sessionId);

        return isActive;
    }
}
