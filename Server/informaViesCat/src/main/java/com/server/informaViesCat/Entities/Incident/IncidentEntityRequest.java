
package com.server.informaViesCat.Entities.Incident;

/**
 *
 * @author leith
 */
public class IncidentEntityRequest {

    public String sessionid;
    public Incident incident;

    public IncidentEntityRequest() {
    }

    public IncidentEntityRequest(String sessionid, Incident incident) {

        this.sessionid = sessionid;
        this.incident = incident;
    }
}
