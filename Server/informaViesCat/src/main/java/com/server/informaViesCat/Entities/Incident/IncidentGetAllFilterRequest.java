package com.server.informaViesCat.Entities.Incident;

import java.io.Serializable;

/**
 *
 * @author leith
 */
public class IncidentGetAllFilterRequest implements Serializable {

    public int userid;
    public String sessionid;
    public int rolid;
    public IncidentCriteria criteria;

    public IncidentGetAllFilterRequest(){}
    public  IncidentGetAllFilterRequest(int userid, String sessionid, int rolid, IncidentCriteria criteria) {

        this.userid = userid;
        this.sessionid = sessionid;
        this.rolid = rolid;

        this.criteria = criteria;
    }
}
