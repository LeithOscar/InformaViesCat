package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.IncidentBusiness;
import com.server.informaViesCat.Entities.AESEncryptionService;
import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentCriteriaBuilder;
import com.server.informaViesCat.Entities.Incident.IncidentGetAllRequest;
import com.server.informaViesCat.Entities.Incident.IncidentListResponse;
import com.server.informaViesCat.Entities.Incident.IncidentRequest;
import com.server.informaViesCat.Entities.User.UserListResponse;
import com.server.informaViesCat.Entities.User.UserLogoutRequest;
import com.server.informaViesCat.Interfaces.IRepository.ISessionRepository;
import com.server.informaViesCat.Repository.SessionRepository;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final IncidentCriteriaBuilder incidentCriteriaBuilder;

    //TODO només els Admin les modificacions 
    //TODO només els tecnic les modificacions el cambi d'estast
    private IncidentBusiness incientBusiness = null;

    public IncidentController() {

        this.incientBusiness = new IncidentBusiness();
        this.incidentCriteriaBuilder = new IncidentCriteriaBuilder();
        this.sessionRepo = new SessionRepository();

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
            var incidentList = incientBusiness.GetAll(request.sessionid, request.rolid);
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
    @GetMapping("/getAllCount")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<Integer> getAllCount() {

        int incidentListCount = incientBusiness.GetAllCount();

        return ResponseEntity.ok(incidentListCount);

    }

    /**
     * Crea el incident
     *
     * @param incident
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/create")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> create(@RequestBody Incident incident) {
        if (incientBusiness.CreateNewIncident(incident)) {
            return ResponseEntity.ok("Incidencia creada.");

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El recurs ja existeix");
        }
    }

    /**
     * Modifica el incident
     *
     * @param incident
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/modify")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> modify(@RequestBody Incident incident) {
        if (incientBusiness.Modify(incident)) {
            return ResponseEntity.ok("incidencia modificada.");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No es pot modificar");
        }
    }

    /**
     * Elimina el incident
     *
     * @param id id del incident
     * @return Retorna missagte si ha elimnat OK o un badrequest
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (incientBusiness.Delete(id)) {
            return ResponseEntity.ok("Incident eliminat.");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix");

        }
    }

    private String BuilderQuery(IncidentRequest incidentRequest) {

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

}
