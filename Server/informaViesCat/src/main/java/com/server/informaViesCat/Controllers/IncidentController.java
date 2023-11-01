package com.server.informaViesCat.Controllers;

import com.server.informaViesCat.Business.IncidentBusiness;
import com.server.informaViesCat.Entities.Incident;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
     * @return llistat dels Incidentss
     */
    @GetMapping("/getall")
    public ResponseEntity<List<Incident>> getAll() {

        var incidentList = incientBusiness.GetAll();
        if (incidentList != null) {
            return ResponseEntity.ok(incidentList);
        }
        return (ResponseEntity<List<Incident>>) ResponseEntity.noContent();

    
    }

}
