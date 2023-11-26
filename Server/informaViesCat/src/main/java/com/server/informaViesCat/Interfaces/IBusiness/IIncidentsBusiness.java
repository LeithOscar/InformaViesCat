package com.server.informaViesCat.Interfaces.IBusiness;

import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentRequest;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IIncidentsBusiness {

    boolean CreateNewIncident(Incident incident);

    List<Incident> GetAll(IncidentRequest incidentRequest);

    boolean Modify(Incident incident);

    boolean Delete(int id);
    
    int GetAllCount();
}
