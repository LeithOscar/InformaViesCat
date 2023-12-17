package com.server.informaViesCat.Interfaces.IRepository;

import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentCriteria;
import com.server.informaViesCat.Entities.Incident.IncidentCriteriaRequest;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IIncidentRepository {

    boolean CreateIncident(Incident incident);

    boolean Exist(int id);

    List<Incident> GetAll(int userId);

    List<Incident> GetAll();
    
     List<Incident> GetAll(IncidentCriteria criteria);

    boolean Modify(Incident incident);

    boolean Delete(int id);

    int GetAllCount();
}
