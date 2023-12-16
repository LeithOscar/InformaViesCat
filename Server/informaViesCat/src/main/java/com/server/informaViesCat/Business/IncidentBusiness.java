package com.server.informaViesCat.Business;

import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.User.UserValidations;
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
    private UserValidations userValidations = new UserValidations();

    public IncidentBusiness() {

        this.repo = new IncidentRepository();
    }

    public IncidentBusiness(IIncidentRepository repoMock) {
        this.repo = repoMock;
    }

    public boolean CreateNewIncident(Incident incident) {

        return this.repo.CreateIncident(incident);

    }

    public List<Incident> GetAll(int userId, int rolId) {
        // si es admin o tecnic..getall
        // si el userId esta informat filtrar.

        if (this.userValidations.IsAdmin(rolId) || this.userValidations.isTechnician(rolId)) {
            return this.repo.GetAll();
        } else {
            return this.repo.GetAll(userId);
        }

    }

    @Override
    public boolean Modify(Incident incident) {

        if (incident != null) {
            return repo.Modify(incident);
        }
        return false;
    }

    @Override
    public boolean Delete(int id) {
        return this.repo.Delete(id);
    }

    public int GetAllCount() {
        return this.repo.GetAllCount();
    }

}
