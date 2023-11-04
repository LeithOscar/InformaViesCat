package com.server.informaViesCat.Business;

import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Interfaces.IBusiness.IIncidentsBusiness;
import com.server.informaViesCat.Interfaces.IRepository.IIncidentRepository;
import com.server.informaViesCat.Repository.IncidentRepository;
import java.util.List;

/**
 *
 * @author leith
 */
public class IncidentBusiness implements IIncidentsBusiness {

    IIncidentRepository repo = null;

    public IncidentBusiness() {

        this.repo = new IncidentRepository();
    }

    public boolean CreateNewIncident(Incident incident) {

        if (!repo.Exist(incident.getId())) {
            this.repo.CreateIncident(incident);
            return true;
        }
        return false;
    }

    public List<Incident> GetAll(String filterCriteria) {
        return this.repo.GetAll(filterCriteria);
    }

    @Override
    public boolean Modify(Incident incident) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean Delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
