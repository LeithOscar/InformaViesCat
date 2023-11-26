
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

            if (!criteria.UserId.isEmpty()) {
                condition += "UserId ="+ criteria.UserId + " " + criteria.Operator + " "; 

            }
            if (criteria.TecnicId != 0) {
                condition += "TecnicId = "+ criteria.TecnicId + " " + criteria.Operator + " "; 

            }
            if (criteria.IncidentTypeId != 0) {
                condition += "IncidentTypeId = "+ criteria.IncidentTypeId + " " +  criteria.Operator + " "; 
            }
            if (!criteria.StartDate.isEmpty()) {
                condition += "StartDate = "+ criteria.StartDate + " " + criteria.Operator + " "; 

            }
            if (!criteria.EndDate.isEmpty()) {
                condition += "EndDate = "+ criteria.EndDate + criteria.Operator + " "; 

            }
            
            condition += "Urgent = "+ criteria.Urgent + criteria.Operator; 

        }

        return condition;
    }

}
