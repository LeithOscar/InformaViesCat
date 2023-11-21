
package ioc.informaviescat.Tests;


import ioc.informaviescat.Controller.IncidencesManagement;
import ioc.informaviescat.Entities.Incident;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Pau Cors Bardolet
 */
public class IncidentFunctionsTest {
    
    Incident incidentToTest = new Incident();
    Incident incidentToTest2 = new Incident();
    
    /**
     * Test de creació d'incidències
     * 
     */
    @Test
    public void testCreateIncident() {
        incidentToTest.setDescription("Test Description");
        incidentToTest.setStartDate("Test Date");
        incidentToTest.setRoadName("Test Road");
        incidentToTest.setTecnicId(2);
        incidentToTest.setUserId(1);
        incidentToTest.setIncidentTypeId(1);
        incidentToTest.setUrgent(true);
        incidentToTest.setKM("Test KM");
        incidentToTest.setGeo("Test Geo");
        
        IncidencesManagement.createIncident(incidentToTest);
        List<Incident> newList = new ArrayList();
        newList = IncidencesManagement.getIncidenceList();
        for(Incident incident: newList){
            if(incident.getDescription().equals("Test Description")){
                incidentToTest2 = incident;
            }
        }
        
        assertNotNull(incidentToTest2);
        assertTrue(incidentToTest2.isUrgent());
        assertEquals(incidentToTest2.getRoadName(), incidentToTest.getRoadName());
    }
    
    /**
     * Test de modificació d'incidències
     * 
     */
    @Test
    public void testModifyIncident() {
        
        incidentToTest.setKM("Modify Test");
        incidentToTest2.setKM("Modify Test");
        
        IncidencesManagement.modifyIncident(incidentToTest2);
        List<Incident> newList = new ArrayList();
        newList = IncidencesManagement.getIncidenceList();
        for(Incident incident: newList){
            if(incident.getDescription().equals("Test Description")){
                incidentToTest2 = incident;
            }
        }
        
        assertEquals(incidentToTest2.getKM(), incidentToTest.getKM());
    }
    
    /**
     * Test de obtenir urgència
     * 
     */
    @Test
    public void testGetUrgency() {
        
        assertEquals("No Urgent", IncidencesManagement.getUrgency(incidentToTest));
    }
}
