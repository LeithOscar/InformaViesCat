
package com.server.informaViesCat.Entities.Incident;

import java.util.Date;

/**
 *
 * @author leith
 */
public class Incident {

    public int id;
    public int UserId;
    public int TecnicId;
    public int IncidentTypeId;
    public String RoadName;
    public String KM;
    public String Geo;
    public String Description;
    public String StartDate;
    public String EndDate;
    public boolean Urgent;

    public Incident()
    {}
    public Incident(int id, int userId, int tecnicId,int incidentTypeId, String roadName, String km, String geo, String description, String startDate, String endDate, boolean urgent) {
        this.id = id;
        this.UserId = userId;
        this.TecnicId = tecnicId;
        this.RoadName = roadName;
        this.KM = km;
        this.Geo = geo;
        this.Description = description;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.Urgent = urgent;
        this.IncidentTypeId= incidentTypeId;
    }

    // Getters para las propiedades
    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return UserId;
    }

    public int getTecnicId() {
        return TecnicId;
    }

    public int getIncidentTypeId() {
        return IncidentTypeId;
    }

    public String getRoadName() {
        return RoadName;
    }

    public String getKM() {
        return KM;
    }

    public String getGeo() {
        return Geo;
    }

    public String getDescription() {
        return Description;
    }

    public String getStartDate() {
        return this.StartDate;
    }

    public String getEndDate() {
        return this.EndDate;
    }

    public boolean isUrgent() {
        return Urgent;
    }
    
     @Override
    public String toString() {
        return "ObjetoDeCarretera{" +
                "carretera='" + this.getRoadName() + '\'' +
                ", longitud=" + this.getGeo() +
                '}';
    }
}
