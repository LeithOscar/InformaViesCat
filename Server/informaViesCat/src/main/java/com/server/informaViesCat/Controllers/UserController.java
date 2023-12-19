package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.UserBusiness;
import com.server.informaViesCat.Entities.AESEncryptionService;
import com.server.informaViesCat.Entities.User.User;
import com.server.informaViesCat.Entities.User.UserListResponse;
import com.server.informaViesCat.Entities.User.UserLoginRequest;
import com.server.informaViesCat.Entities.User.UserLogoutRequest;
import com.server.informaViesCat.Entities.User.UserRemoveRequest;
import com.server.informaViesCat.Entities.User.UserRequest;
import com.server.informaViesCat.Entities.User.UserResponse;
import com.server.informaViesCat.Interfaces.IRepository.ISessionRepository;
import com.server.informaViesCat.Repository.SessionRepository;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @return llistat dels usuarios
     */
    @PostMapping("/getall")
    public ResponseEntity<String> getAll(@RequestBody String sessionId) {

        //map from client
        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(sessionId);

        if (requestJSON == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (isSessionActive(requestJSON.getString("sessionid"))) {
            var userList = userBusiness.GetAll();
            if (userList != null) {

                UserListResponse response = new UserListResponse(userList, requestJSON.getString("sessionid"));

                return ResponseEntity.ok(AESEncryptionService.encryptFromJSONObject(response.convertObjectToJson()));
            }
            return (ResponseEntity<String>) ResponseEntity.noContent();
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Crea el usuari
     *
     * @param UserRequest
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/create")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> create(@RequestBody String UserRequest) {

        UserRequest request = this.parseUserRequest(UserRequest);

        if (request == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (userBusiness.CreateNewUser(request.user)) {

            return ResponseEntity.ok("");

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El recurs ja existeix");
        }

    }

    /**
     * Crea el usuari
     *
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/modify")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> modify(@RequestBody String UserRequest) {

        UserRequest request = this.parseUserRequest(UserRequest);

        if (request == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (isSessionActive(request.sessionId)) {
            if (userBusiness.Modify(request.user)) {
                return ResponseEntity.ok(" ");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No es pot modificar");
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Elimina el usuari
     *
     * @return Retorna missagte si ha elimnat OK o un badrequest
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody String userRemoveRequest) {

        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(userRemoveRequest);

        if (requestJSON == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        UserRemoveRequest request = new UserRemoveRequest(requestJSON.getString("sessionid"), requestJSON.getInt("userid"));

        if (isSessionActive(request.sessionId)) {
            if (userBusiness.Delete(request.userid)) {
                return ResponseEntity.ok("");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix");

            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
    /**
     * Elimina el usuari
     *
     * @return Retorna missagte si ha elimnat OK o un badrequest
     */
    @PostMapping("/delete")
    public ResponseEntity<String> deleteDesk(@RequestBody String userRemoveRequest) {

        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(userRemoveRequest);

        if (requestJSON == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        UserRemoveRequest request = new UserRemoveRequest(requestJSON.getString("sessionid"), requestJSON.getInt("userid"));

        if (isSessionActive(request.sessionId)) {
            if (userBusiness.Delete(request.userid)) {
                return ResponseEntity.ok("");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix");

            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }


    private boolean isSessionActive(String sessionId) {

        boolean isActive = sessionRepo.IsActive(sessionId);

        return isActive;
    }

    private UserRequest parseUserRequest(String userRequestString) {
        // Map from the client
        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(userRequestString);

        if (requestJSON == null) {
            return null;
        }

        // Extract the user object from the JSON
        JSONObject userObject = requestJSON.getJSONObject("user");

        // Convert the user JSON to a User object
        User user = User.convertJsonToObject(userObject.toString());

        // Create a UserRequest object
        UserRequest request = new UserRequest(requestJSON.getString("sessionid"), user);

        // Return request
        return request;
    }
}
