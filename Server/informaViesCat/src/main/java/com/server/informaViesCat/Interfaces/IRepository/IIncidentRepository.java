package com.server.informaViesCat.Interfaces.IRepository;

import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentRequest;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IIncidentRepository {

    boolean CreateIncident(Incident incident);

    boolean Exist(int id);

    List<Incident> GetAll(IncidentRequest incidentRequest);

    boolean Modify(Incident incident);

    boolean Delete(int id);
}
