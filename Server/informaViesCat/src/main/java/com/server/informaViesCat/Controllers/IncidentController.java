package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.IncidentBusiness;
import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentCriteriaBuilder;
import com.server.informaViesCat.Entities.Incident.IncidentRequest;
import java.util.List;
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
 * @author leith Controlador d'incidencies
 */
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentCriteriaBuilder incidentCriteriaBuilder;

    //TODO només els Admin les modificacions 
    //TODO només els tecnic les modificacions el cambi d'estast
    private IncidentBusiness incientBusiness = null;

    public IncidentController() {

        this.incientBusiness = new IncidentBusiness();
        this.incidentCriteriaBuilder = new IncidentCriteriaBuilder();
    }

    /**
     * Obté tots els incidents
     *
     * @param userId
     * @param rolId
     * @return llistat dels Incidenciesss
     */
    @GetMapping("/getall/{userId}/{rolId}")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    //public ResponseEntity<List<Incident>> getAll(@RequestBody IncidentRequest incidentRequest) { //pendent filtro
    public ResponseEntity<List<Incident>> getAll(@PathVariable String userId,@PathVariable  int rolId ) {

        IncidentRequest incidentRequest = new IncidentRequest();
        var incidentList = incientBusiness.GetAll(userId,rolId);
        if (incidentList != null) {
            return ResponseEntity.ok(incidentList);
        }
        return (ResponseEntity<List<Incident>>) ResponseEntity.noContent();

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

}
