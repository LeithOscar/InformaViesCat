
package com.server.informaViesCat.Entities.Incident;

/**
 *
 * @author leith
 */
public class IncidentCriteriaBuilder {
    
    public void IncidentCriteriaBuilder(){}
    
    public String buildConditions(IncidentCriteria criteria) {
        String condition = "";

        if (criteria != null) {

            if (criteria.userid!=0) {
                condition += "UserId ="+ criteria.userid + " " + criteria.operator + " "; 

            }
            if (criteria.tecnicid != 0) {
                condition += "TecnicId = "+ criteria.tecnicid + " " + criteria.operator + " "; 

            }
            if (criteria.incidenttypeid != 0) {
                condition += "IncidentTypeId = "+ criteria.incidenttypeid + " " +  criteria.operator + " "; 
            }
            if (!criteria.startdate.isEmpty()) {
                condition += "StartDate = '"+ criteria.startdate + "' " + criteria.operator + " "; 

            }
            if (!criteria.enddate.isEmpty()) {
                condition += "EndDate = '"+ criteria.enddate + "' " +  criteria.operator + " "; 

            }
            
            condition += " Urgent = "+ criteria.urgent  + ";";

        }

        return condition;
    }

}
