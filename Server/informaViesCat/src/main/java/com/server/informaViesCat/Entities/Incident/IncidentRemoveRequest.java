
package com.server.informaViesCat.Entities.Incident;

/**
 *
 * @author leith
 */
public class IncidentRemoveRequest {
     public String sessionId;
    public int incidentid;

    public IncidentRemoveRequest() {
    }

    public IncidentRemoveRequest(String sessionid, int incidentid) {

        this.sessionId = sessionid;
        this.incidentid = incidentid;
    }
}
