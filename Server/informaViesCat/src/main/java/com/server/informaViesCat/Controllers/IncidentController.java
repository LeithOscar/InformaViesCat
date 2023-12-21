package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.IncidentBusiness;
import com.server.informaViesCat.Entities.AESEncryptionService;
import com.server.informaViesCat.Entities.Incident.GetAllDescriptionContainsRequest;
import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentCriteria;
import com.server.informaViesCat.Entities.Incident.IncidentCriteriaBuilder;
import com.server.informaViesCat.Entities.Incident.IncidentGetAllCount;
import com.server.informaViesCat.Entities.Incident.IncidentGetAllCountResponse;
import com.server.informaViesCat.Entities.Incident.IncidentGetAllRequest;
import com.server.informaViesCat.Entities.Incident.IncidentListResponse;
import com.server.informaViesCat.Entities.Incident.IncidentCriteriaRequest;
import com.server.informaViesCat.Entities.Incident.IncidentEntityRequest;
import com.server.informaViesCat.Entities.Incident.IncidentGetAllFilterRequest;
import com.server.informaViesCat.Entities.Incident.IncidentRemoveRequest;
import com.server.informaViesCat.Interfaces.IRepository.IIncidentRepository;
import com.server.informaViesCat.Interfaces.IRepository.ISessionRepository;
import com.server.informaViesCat.Repository.IncidentRepository;
import com.server.informaViesCat.Repository.SessionRepository;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author leith Controlador d'incidencies
 */
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private ISessionRepository sessionRepo = null;
    private IIncidentRepository incidentRepo = null;

    private final IncidentCriteriaBuilder incidentCriteriaBuilder;

    //TODO només els Admin les modificacions 
    //TODO només els tecnic les modificacions el cambi d'estast
    private IncidentBusiness incientBusiness = null;

    public IncidentController() {

        this.incientBusiness = new IncidentBusiness();
        this.incidentCriteriaBuilder = new IncidentCriteriaBuilder();
        this.sessionRepo = new SessionRepository();
        this.incidentRepo = new IncidentRepository();

    }

    /**
     * Obté tots els incidents
     *
     * @return llistat dels Incidenciesss
     */
    @PostMapping("/getall")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> getAll(@RequestBody String incidentGetAllRequest) {

        //map from client
        JSONObject requestJson = AESEncryptionService.decryptToJSONObject(incidentGetAllRequest);

        if (requestJson == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        IncidentGetAllRequest request = new IncidentGetAllRequest(requestJson.getInt("userid"), requestJson.getInt("rolid"), requestJson.getString("sessionid"));

        if (isSessionActive(request.sessionid)) {
            var incidentList = incientBusiness.GetAll(request.userid, request.rolid);
            if (incidentList != null) {

                IncidentListResponse response = new IncidentListResponse(incidentList, requestJson.getString("sessionid"));

                return ResponseEntity.ok(AESEncryptionService.encryptFromJSONObject(response.convertObjectToJson()));
            }

            return (ResponseEntity<String>) ResponseEntity.noContent();
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/getallFiltered")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> getallFiltered(@RequestBody String request) {

        //map from client
        JSONObject requestJson = AESEncryptionService.decryptToJSONObject(request);

        if (requestJson == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server

        // Extract the user object from the JSON
        JSONObject criteriaObject = requestJson.getJSONObject("criteria");

        // Convert the user JSON to a User object
        IncidentCriteria criteria = IncidentCriteria.convertJsonToObject(criteriaObject.toString());

        IncidentGetAllFilterRequest filter = new IncidentGetAllFilterRequest(requestJson.getInt("userid"), requestJson.getString("sessionid"), requestJson.getInt("rolid"), criteria);

        if (isSessionActive(filter.sessionid)) {
            var incidentList = incientBusiness.GetAll(filter);
            if (incidentList != null) {

                IncidentListResponse response = new IncidentListResponse(incidentList, requestJson.getString("sessionid"));

                return ResponseEntity.ok(AESEncryptionService.encryptFromJSONObject(response.convertObjectToJson()));
            }

            return (ResponseEntity<String>) ResponseEntity.noContent();
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Obté tots els incidents
     *
     * @return llistat dels Incidenciesss
     */
    @PostMapping("/getAllCount")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> getAllCount(@RequestBody String incidentGetAllCount) {

        //map from client
        JSONObject requestJson = AESEncryptionService.decryptToJSONObject(incidentGetAllCount);

        if (requestJson == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        IncidentGetAllCount request = new IncidentGetAllCount(requestJson.getString("sessionid"));

        if (isSessionActive(request.sessionid)) {
            int incidentListCount = incientBusiness.GetAllCount();

            IncidentGetAllCountResponse response = new IncidentGetAllCountResponse(incidentListCount);

            return ResponseEntity.ok(AESEncryptionService.encryptFromJSONObject(response.convertObjectToJson()));
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Obté tots els incidents per tipus indicat
     *"validating"
     *"validated"
     *"Assign"
     *"Repaired"
     *"close"
     * @return llistat dels Incidenciess per tipus indicat
     */
    @PostMapping("/getAllByType")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> getAllByType(@RequestBody String incidentrequest) {

        IncidentEntityRequest request = this.parseIncidentRequest(incidentrequest);

        if (isSessionActive(request.sessionid)) {
            var incidentList = incidentRepo.GetAllByType(request.incident.getIncidenttypeid());
            if (incidentList != null) {

                IncidentListResponse response = new IncidentListResponse(incidentList, request.sessionid);

                return ResponseEntity.ok(AESEncryptionService.encryptFromJSONObject(response.convertObjectToJson()));
            }

            return (ResponseEntity<String>) ResponseEntity.noContent();
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Obté tots els incidents on la descripcio conte
     *
     * @return llistat dels Incidenciess on la descripcio conte
     */
    @PostMapping("/getAllByDescriptionContains")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> getAllByDescriptionContains(@RequestBody String getAllDescriptionContainsRequest) {

        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(getAllDescriptionContainsRequest);

        if (requestJSON == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        GetAllDescriptionContainsRequest request = new GetAllDescriptionContainsRequest(requestJSON.getString("sessionid"), requestJSON.getString("contains"));

        if (isSessionActive(request.sessionid)) {
            var incidentList = incidentRepo.GetAllByDescriptionContains(request.contains);
            if (incidentList != null) {

                IncidentListResponse response = new IncidentListResponse(incidentList, request.sessionid);

                return ResponseEntity.ok(AESEncryptionService.encryptFromJSONObject(response.convertObjectToJson()));
            }

            return (ResponseEntity<String>) ResponseEntity.noContent();
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }

    }

    
    /**
     * Obté tots els incidents
     *
     * @return llistat dels Incidenciesss
     */
    @PostMapping("/getAllNearMe")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> getAllNearMe(@RequestBody String incidentGetAllRequest) {

        //map from client
        JSONObject requestJson = AESEncryptionService.decryptToJSONObject(incidentGetAllRequest);

        if (requestJson == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        
        if (isSessionActive(requestJson.getString("sessionid"))) {
            var cordenadaMasPropera = incientBusiness.GetAllNearMe(requestJson.getFloat("latitud"), requestJson.getFloat("longitud"));
            if (cordenadaMasPropera != null) {

                

                return ResponseEntity.ok(AESEncryptionService.encryptFromJSONObject(cordenadaMasPropera.convertObjectToJson()));
            }

            return (ResponseEntity<String>) ResponseEntity.noContent();
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }

    }

    
    /**
     * Crea el incident
     *
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/create")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> create(@RequestBody String incidentrequest) {

        IncidentEntityRequest request = this.parseIncidentRequest(incidentrequest);

        if (request == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (isSessionActive(request.sessionid)) {

            if (incientBusiness.CreateNewIncident(request.incident)) {
                return ResponseEntity.ok("Incidencia creada.");

            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El recurs ja existeix");
            }
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Modifica el incident
     *
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/modify")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> modify(@RequestBody String incidentrequest) {

        IncidentEntityRequest request = this.parseIncidentRequest(incidentrequest);

        if (request == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (request == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (isSessionActive(request.sessionid)) {

            if (incientBusiness.Modify(request.incident)) {
                return ResponseEntity.ok("incidencia modificada.");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No es pot modificar");
            }
        } else {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Elimina el incident
     *
     * @return Retorna missagte si ha elimnat OK o un badrequest
     */
    @PostMapping("/delete")
    public ResponseEntity<String> deleteDesk(@RequestBody String incidentRemoveRequest) {

        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(incidentRemoveRequest);

        if (requestJSON == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        IncidentRemoveRequest request = new IncidentRemoveRequest(requestJSON.getString("sessionid"), requestJSON.getInt("incidentid"));

        if (isSessionActive(request.sessionId)) {
            if (incientBusiness.Delete(request.incidentid)) {
                return ResponseEntity.ok("");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix");

            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * Elimina el incident
     *
     * @return Retorna missagte si ha elimnat OK o un badrequest
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody String incidentRemoveRequest) {

        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(incidentRemoveRequest);

        if (requestJSON == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        //Build new Object server
        IncidentRemoveRequest request = new IncidentRemoveRequest(requestJSON.getString("sessionid"), requestJSON.getInt("incidentid"));

        if (isSessionActive(request.sessionId)) {
            if (incientBusiness.Delete(request.incidentid)) {
                return ResponseEntity.ok("");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix");

            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    private String BuilderQuery(IncidentCriteriaRequest incidentRequest) {

        // Construir la consulta SQL base
        StringBuilder query = new StringBuilder("SELECT * FROM Incidents WHERE ");

        // Añadir condiciones según los criterios proporcionados
        query.append(this.incidentCriteriaBuilder.buildConditions(incidentRequest.Criteria));

        return "";
    }

    private boolean isSessionActive(String sessionId) {

        boolean isActive = sessionRepo.IsActive(sessionId);

        return isActive;
    }

    private IncidentEntityRequest parseIncidentRequest(String incidentRequestString) {
        // Map from the client
        JSONObject requestJSON = AESEncryptionService.decryptToJSONObject(incidentRequestString);

        if (requestJSON == null) {
            return null;
        }

        // Extract the user object from the JSON
        JSONObject incidentObject = requestJSON.getJSONObject("incident");

        // Convert the user JSON to a User object
        Incident incident = Incident.convertJsonToObject(incidentObject.toString());

        // Create a UserRequest object
        IncidentEntityRequest request = new IncidentEntityRequest(requestJSON.getString("sessionid"), incident);

        // Return request
        return request;
    }
}
