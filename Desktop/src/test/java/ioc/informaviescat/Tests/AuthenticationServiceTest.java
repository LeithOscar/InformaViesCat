
package ioc.informaviescat.Tests;

import ioc.informaviescat.Controller.UsersManagement;
import ioc.informaviescat.Entities.User;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
/**
 *
 * @author Pau Cors Bardolet
 */
public class AuthenticationServiceTest {
    
    /**
     * Test de login de usuari Administrador
     * 
     * Aquest test comprova si un usuari Administrador es loggeja de forma correcta
     */
    User usuariTest = new User();
    
    @Test
    public void testLoginAdministrator_Success() {
        //Es comprova login per un usuari Administrador
        String username = "analopez";
        String password = "1234";

        usuariTest = UsersManagement.login(username, password);

        assertNotNull(usuariTest);
        assertTrue(usuariTest.isLogged());
        assertEquals(username, usuariTest.getUserName());
    }
    
    /**
     * Test de logout de usuari Administrador
     * 
     * Aquest test comprova si un usuari Administrador es desloggeja de forma correcta
     */
    @Test
    public void testLogoutAdministrator_Success() {
        //Es comprova logout per un usuari Administrador

        UsersManagement.logout(usuariTest.getSessionId(), usuariTest.getId());
    }
    
    /**
     * Test de login de usuari Tècnic
     * 
     * Aquest test comprova si un usuari Tècnic es loggeja de forma correcta
     */
    @Test
    public void testLoginTechnician_Success() {
        //Es comprova login per un usuari Tècnic
        String username = "luismartinez";
        String password = "1234";

         usuariTest = UsersManagement.login(username, password);

        assertNotNull(usuariTest);
        assertTrue(usuariTest.isLogged());
        assertEquals(username, usuariTest.getUserName());
    }
    
    /**
     * Test de logout de usuari Tècnic
     * 
     * Aquest test comprova si un usuari Tècnic es desloggeja de forma correcta
     */
    @Test
    public void testLogoutTechnician_Success() {
        //Es comprova logout per un usuari Tècnic

        UsersManagement.logout(usuariTest.getSessionId(), usuariTest.getId());
    }
    
    /**
     * Test de login de usuari Ciutadà
     * 
     * Aquest test comprova si un usuari Ciutadà es loggeja de forma correcta
     */
    @Test
    public void testLoginUser_Success() {
        //Es comprova login per un usuari Ciutadà
        String username = "pedrosanchez";
        String password = "1234";

         usuariTest = UsersManagement.login(username, password);

        assertNotNull(usuariTest);
        assertTrue(usuariTest.isLogged());
        assertEquals(username, usuariTest.getUserName());
    }
    
    /**
     * Test de logout de usuari Ciutadà
     * 
     * Aquest test comprova si un usuari Ciutadà es desloggeja de forma correcta
     */
    @Test
    public void testLogoutUser_Success() {
        //Es comprova logout per un usuari Ciutadà

        UsersManagement.logout(usuariTest.getSessionId(), usuariTest.getId());
    }

    /**
     * Test de login de usuari fals
     * 
     * Aquest test comprova si un usuari fals no es pot loggejar i la resposta del servidor
     */
    @Test
    public void testLogin_Failure() {
        //Es comprova el login de un usuari fals
        String username = "testUser";
        String password = "wrongPassword";

        User resultUser = UsersManagement.login(username, password);

        assertNull(resultUser);
    }

}

