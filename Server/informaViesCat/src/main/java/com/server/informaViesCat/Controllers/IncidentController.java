package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.IncidentBusiness;
import com.server.informaViesCat.Entities.Incident.Incident;
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

    //TODO només els Admin les modificacions 
    //TODO només els tecnic les modificacions el cambi d'estast
    private IncidentBusiness incientBusiness = null;

    public IncidentController() {
        
        this.incientBusiness = new IncidentBusiness();
    }

        /**
     * Obté tots els incidents
     *
     * @return llistat dels Incidenciesss
     */
    @GetMapping("/getall/{filter}")
    public ResponseEntity<List<Incident>> getAll(@PathVariable String filter) {

        var incidentList = incientBusiness.GetAll(filter);
        if (incidentList != null) {
            return ResponseEntity.ok(incidentList);
        }
        return (ResponseEntity<List<Incident>>) ResponseEntity.noContent();

    
    }
    
     /**
     * Crea el usuari
     *
     * @param incident
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    @PutMapping("/create")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> create(@RequestBody Incident incident) {
        if (incientBusiness.CreateNewIncident(incident)) {
            return ResponseEntity.ok("Usuari creat.");

        } else {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("El recurs ja existeix");
        }
    }
    
     /**
     * Crea el usuari
     *
     * @param incident
     * @return Retorna missagte si ha creat OK o un badrequest
     */
    
    @PutMapping("/modify")
    @Consumes("MediaType.APPLICATION_JSON")
    @Produces("MediaType.APPLICATION_JSON")
    public ResponseEntity<String> modify(@RequestBody Incident incident) {
        if (incientBusiness.Modify(incident)) {
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
        if (incientBusiness.Delete(id)) {
            return ResponseEntity.ok("Incident eliminat.");

        } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existeix");

        }
    }


}
