package com.server.informaViesCat.Interfaces.IRepository;

import com.server.informaViesCat.Entities.Incident;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IIncidentRepository {

    void CreateIncident(Incident incident);

    boolean Exist(int id);

    List<Incident> GetAll();

    boolean Modify(Incident incident);

    boolean Delete(int id);
}
