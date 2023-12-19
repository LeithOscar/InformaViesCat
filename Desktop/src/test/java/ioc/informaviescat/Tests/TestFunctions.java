
package ioc.informaviescat.Tests;

import ioc.informaviescat.Controller.Functions;
import ioc.informaviescat.Entities.Incident;
import ioc.informaviescat.Entities.Response;
import ioc.informaviescat.Entities.User;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Pau Cors Bardolet
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFunctions {
    
    
    /**
    * Test de la funció login
    */
    @Test
    @Order(1)
    public void testLogin(){
        
        Response response = Functions.login("paucors", "1234");
        User user = response.getUser();
        
        System.out.println(user.getName());
        
        assertEquals("paucors", response.getUser().getUserName());
        assertEquals("paucors@gmail.com", response.getUser().GetEmail());
    }
    
    /**
    * Test de la funció logout
    */
    @Test
    @Order(2)
    public void testLogout(){
        
        
        
        Boolean resultat = null;
        Response response = Functions.login("paucors", "1234");
        String resposta = Functions.logout(response.getUser().getId(), response.getUser().getSessionId());
        Response response2 = Functions.login("grego", "1234");
        
        List<User> llista = Functions.getAllUsers(response2.getUser().getSessionId());
        for(User usuari : llista){
            if(usuari.getUserName().equals("paucors")){
                resultat = usuari.isLogged();
            }
        }
        
        Functions.logout(response2.getUser().getId(), response2.getUser().getSessionId());
        assertFalse(resultat);
    }
    
    /**
    * Test de la funció create user
    */
    @Test
    @Order(3)
    public void testCreateUser(){
        
        Response resposta = Functions.login("paucors", "1234");
        User user = resposta.getUser();
        User usuariTest = new User();
        User usuariNou = new User();
        
        usuariNou.setEmail("testClient@mail.com");
        usuariNou.setLastName("testClientCognom");
        usuariNou.setName("testClientNom");
        usuariNou.setPassword("1234");
        usuariNou.setRolId(1);
        usuariNou.setUserName("testClient");
        
        Functions.createUser(usuariNou, user.getSessionId());
        
        List<User> llista = Functions.getAllUsers(user.getSessionId());
        
        for(User usuari : llista){
            if(usuari.getUserName().equals("testClient")){
                usuariTest = usuari;
            }
        }
        
        Functions.logout(user.getId(), user.getSessionId());
        
        assertEquals(usuariTest.getUserName(), usuariNou.getUserName());
        assertEquals(usuariTest.getRolId(), usuariNou.getRolId());
    }

    /**
    * Test de la funció delete user
    */
    @Test
    @Order(4)
    public void testDeleteUser(){
        
        Response resposta = Functions.login("paucors", "1234");
        User user = resposta.getUser();
        User usuariNou = new User();
        
        List<User> llista = Functions.getAllUsers(user.getSessionId());
        
        for(User usuari : llista){
            if(usuari.getUserName().equals("testClient")){
                usuariNou = usuari;
            }
        }
        
        Functions.deleteUser(usuariNou, user);
        
        llista = Functions.getAllUsers(user.getSessionId());
        
        int i = 0;
        for(User usuari : llista){
            if(usuari.getUserName().equals("testClient")){
                i ++;
            }
        }
        Functions.logout(user.getId(), user.getSessionId());
        assertEquals(0, i);
    }
    
    /**
    * Test de la funció get all users
    */
    @Test
    @Order(5)
    public void testGetAllUsers(){
        
        List<User> llistaTests = null;
        Response resposta = Functions.login("paucors", "1234");
        User user = resposta.getUser();
        
        llistaTests = Functions.getAllUsers(user.getSessionId());
        
        Functions.logout(user.getId(), user.getSessionId());
        
        assertNotNull(llistaTests);
    }
    
    /**
    * Test de la funció get all users
    */
    @Test
    @Order(6)
    public void testGetUserData(){
        
        String[][] llistaTests = null;
        Response resposta = Functions.login("paucors", "1234");
        User user = resposta.getUser();
        
        llistaTests = Functions.getUserData(user);
        
        Functions.logout(user.getId(), user.getSessionId());
        
        assertNotNull(llistaTests);
    }
    
    /**
    * Test de la funció get logat
    */
    @Test
    @Order(7)
    public void testGetLogat(){
        
        String resposta1 = Functions.getLogat(true);
        String resposta2 = Functions.getLogat(false);
        
        assertEquals("Logat", resposta1);
        assertEquals("no Logat", resposta2);
    }
    
    /**
    * Test de la funció get all incidents
    */
    @Test
    @Order(8)
    public void testGetAllIncidents(){
        
        List<Incident> llistaTests = null;
        Response resposta = Functions.login("paucors", "1234");
        User user = resposta.getUser();
        
        llistaTests = Functions.getAllIncidents(user);
        
        Functions.logout(user.getId(), user.getSessionId());
        
        assertNotNull(llistaTests);
    }
    
    /**
    * Test de la funció create incident
    */
    @Test
    @Order(9)
    public void testCreateIncident(){
        
        
        Response resposta = Functions.login("paucors", "1234");
        User user = resposta.getUser();
        Incident incidentTest = new Incident();
        Incident incidentNou = new Incident();
        
        incidentNou.setDescription("testDescription");
        incidentNou.setEndDate("-");
        incidentNou.setGeo("-");
        incidentNou.setIncidentTypeId(1);
        incidentNou.setKM("-");
        incidentNou.setRoadName("-");
        incidentNou.setStartDate("-");
        incidentNou.setTecnicId(2);
        incidentNou.setUrgent(false);
        incidentNou.setUserId(user.getId());
        
        Functions.createIncident(user, incidentNou);
        
        List<Incident> llista = Functions.getAllIncidents(user);
        
        for(Incident incident : llista){
            if(incident.getDescription().equals("testDescription")){
                incidentTest = incident;
            }
        }
        
        Functions.logout(user.getId(), user.getSessionId());
        
        assertEquals(incidentNou.getDescription(), incidentTest.getDescription());
        assertEquals(incidentNou.getKM(), incidentTest.getKM());
    }
    
    /**
    * Test de la funció create incident
    */
    @Test
    @Order(10)
    public void testDeleteIncident(){
        
        
        Response resposta = Functions.login("paucors", "1234");
        User user = resposta.getUser();
        Incident incidentTest = new Incident();
        Incident incidentNou = new Incident();
        
        List<Incident> llista = Functions.getAllIncidents(user);
        
        for(Incident incident : llista){
            if(incident.getDescription().equals("testDescription")){
                incidentNou = incident;
            }
        }
        
        Functions.deleteIncident(user, incidentNou);
        
        llista = Functions.getAllIncidents(user);
        
        int i = 0;
        for(Incident incident : llista){
            if(incident.getDescription().equals("testDescription")){
                i ++;
            }
        }
        
        Functions.logout(user.getId(), user.getSessionId());
        
        assertEquals(0, i);
    }
    
    /**
    * Test de la funció get role
    */
    @Test
    @Order(11)
    public void testGetRole(){
        String resposta1 = Functions.getRole(1);
        String resposta2 = Functions.getRole(2);
        String resposta3 = Functions.getRole(3);
        
        assertEquals("Administrador", resposta1);
        assertEquals("Tècnic", resposta2);
        assertEquals("Usuari comú", resposta3);
    }
    
    /**
    * Test de la funció get incident type
    */
    @Test
    @Order(12)
    public void testGetIncidentType(){
        
        Incident incidentNou = new Incident();
        incidentNou.setDescription("testDescription");
        incidentNou.setEndDate("-");
        incidentNou.setGeo("-");
        incidentNou.setIncidentTypeId(2);
        incidentNou.setKM("-");
        incidentNou.setRoadName("-");
        incidentNou.setStartDate("-");
        incidentNou.setTecnicId(2);
        incidentNou.setUrgent(false);
        
        String resposta1 = Functions.getIncidentType(incidentNou);
        
        incidentNou.setIncidentTypeId(5);
        
        String resposta2 = Functions.getIncidentType(incidentNou);
        
        assertEquals("Validada", resposta1);
        assertEquals("Tancada", resposta2);
    }
    
    /**
    * Test de la funció get urgency
    */
    @Test
    @Order(13)
    public void testGetUrgency(){
        
        Incident incidentNou = new Incident();
        incidentNou.setDescription("testDescription");
        incidentNou.setEndDate("-");
        incidentNou.setGeo("-");
        incidentNou.setIncidentTypeId(1);
        incidentNou.setKM("-");
        incidentNou.setRoadName("-");
        incidentNou.setStartDate("-");
        incidentNou.setTecnicId(2);
        incidentNou.setUrgent(false);
        
        String resposta1 = Functions.getUrgency(incidentNou);
        
        incidentNou.setUrgent(true);
        
        String resposta2 = Functions.getUrgency(incidentNou);
        
        assertEquals("No Urgent", resposta1);
        assertEquals("Urgent", resposta2);
    }
    
    /**
    * Test de la funció get urgency
    */
    @Test
    @Order(14)
    public void testSaveAndRestoreCredentials(){
        
        Functions.saveCredentials("test", "credencials");
        String[] resposta = Functions.restoreCredentials();
        
        assertEquals("test", resposta[0]);
        assertEquals("credencials", resposta[1]);
    }
}
