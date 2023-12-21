package com.server.informaViesCat.Business;

import com.server.informaViesCat.Entities.Coordenada;
import com.server.informaViesCat.Entities.CoordenadasUtils;
import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Entities.Incident.IncidentGetAllFilterRequest;
import com.server.informaViesCat.Entities.User.UserValidations;
import com.server.informaViesCat.Interfaces.IBusiness.IIncidentsBusiness;
import com.server.informaViesCat.Interfaces.IRepository.IIncidentRepository;
import com.server.informaViesCat.Repository.IncidentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Coordenada GetAllNearMe(float latitud, float longitud) {
        var all = this.repo.GetAll();
        Map<Incident, Coordenada> diccionario = new HashMap<>();

        for (Incident incident : all) {
            var spliter = incident.geo.split(";");
            if (spliter.length >= 2) {
                var coordenadas = this.buildCoordenadas(spliter[0], spliter[1]);
                diccionario.put(incident, coordenadas);
            } else {
                // Manejar el caso donde no hay suficientes elementos después de la división
                System.out.println("Coordenades no válides:  " + incident.geo);
            }
        }

        // covertir els valor en una llista.
        List<Coordenada> listaDeValores = new ArrayList<>(diccionario.values());

        var nearme = CoordenadasUtils.FindNearMe(latitud, longitud, listaDeValores);

        return nearme;
    }

    public List<Incident> GetAll(IncidentGetAllFilterRequest incidentGetAllFilterRequest) {

        return this.repo.GetAll(incidentGetAllFilterRequest.criteria);

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

    private Coordenada buildCoordenadas(String latitud, String longitud) {

        Coordenada newCordenada = new Coordenada(Float.parseFloat(latitud), Float.parseFloat(longitud));

        return newCordenada;
    }

}
