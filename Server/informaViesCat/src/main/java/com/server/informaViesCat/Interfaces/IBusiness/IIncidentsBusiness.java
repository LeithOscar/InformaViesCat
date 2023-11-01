
package com.server.informaViesCat.Interfaces.IBusiness;

import com.server.informaViesCat.Entities.Incident;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IIncidentsBusiness {
    
       boolean CreateNewIncident(Incident incident);

    List<Incident> GetAll();

    boolean Modify(Incident incident);
    
    boolean Delete (int id);
}
