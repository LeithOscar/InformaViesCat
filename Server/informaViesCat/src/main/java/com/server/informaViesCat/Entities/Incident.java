
package com.server.informaViesCat.Entities;

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
    public Date StartDate;
    public Date EndDate;
    public boolean Urgent;

    public Incident(int id, int userId, int tecnicId,int incidentTypeId, String roadName, String km, String geo, String description, Date startDate, Date endDate, boolean urgent) {
        this.id = id;
        UserId = userId;
        TecnicId = tecnicId;
        RoadName = roadName;
        KM = km;
        Geo = geo;
        Description = description;
        StartDate = startDate;
        EndDate = endDate;
        Urgent = urgent;
        IncidentTypeId= incidentTypeId;
    }

    // Getters para las propiedades
    public int getId() {
        return id;
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

    public Date getStartDate() {
        return StartDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public boolean isUrgent() {
        return Urgent;
    }
}
