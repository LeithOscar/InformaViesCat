
package ioc.informaviescat.Tests;

import ioc.informaviescat.Controller.UsersManagement;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author Pau Cors Bardolet
 */
public class UsersManagementTest {
    
    /**
     * Test de la funció GetRole amb user Administrador
     * 
     * Aquest test comprova si la funció GetRole retorna correctament el Valor d'Administrador
     */
    @Test
    public void testGetRole_Administrator() {
        String expectedResult = "Administrador";

        String result = UsersManagement.getRole(1); //rol Administrador = 1

        assertNotNull(result);
        assertEquals(result, expectedResult);
    }
    
    /**
     * Test de la funció GetRole amb user Tècnic
     * 
     * Aquest test comprova si la funció GetRole retorna correctament el Valor de Tècnic
     */
    @Test
    public void testGetRole_Technician() {
        String expectedResult = "Tècnic";

        String result = UsersManagement.getRole(2); //rol Tècnic = 2

        assertNotNull(result);
        assertEquals(result, expectedResult);
    }
    
    /**
     * Test de la funció GetRole amb user Ciutadà
     * 
     * Aquest test comprova si la funció GetRole retorna correctament el Valor de usuari Ciutadà
     */
    @Test
    public void testGetRole_User() {
        String expectedResult = "Usuari comú";

        String result = UsersManagement.getRole(3); //rol ciutadà = 3

        assertNotNull(result);
        assertEquals(result, expectedResult);
    }
}
