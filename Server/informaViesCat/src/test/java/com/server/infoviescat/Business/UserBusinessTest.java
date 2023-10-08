/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.server.infoviescat.Business;

import com.server.informaViesCat.Business.UserBusiness;
import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author leith
 */
public class UserBusinessTest {
    /*
    private UserBusiness userBusiness;
    private UserRepository repo;

    public UserBusinessTest() {
       
        repo = new UserRepository();
        userBusiness = new UserBusiness();
    }

    @Test
    public void testLogin_SuccessfulLogin()  {
        // Arrange
        String userName = "testuser";
        String password = "testpassword";
        User mockUser = new User(1, userName, password, false, "", "", "");

        When(repo.GetByUsernameAndPassword(userName, password)).thenReturn(mockUser);
        when(repo.UpdateIsLogged(mockUser, true)).thenReturn(mockUser);

        // Act
        User result = userBusiness.login(userName, password);

        // Assert
        assertNotNull(result);
        assertTrue(result.isLogged());
        assertEquals(mockUser, result);
    }

    @Test
    public void testLogin_UnsuccessfulLogin() throws SQLException {
        // Arrange
        String userName = "testuser";
        String password = "testpassword";

        when(repo.GetByUsernameAndPassword(userName, password)).thenReturn(null);

        // Act
        User result = userBusiness.login(userName, password);

        // Assert
        assertNotNull(result);
        assertFalse(result.isLogged());
        assertEquals(0, result.getId());
    }

    @Test
    public void testLogin_EmptyUserNameAndPassword() {
        // Arrange
        String userName = "";
        String password = "";

        // Act
        User result = userBusiness.login(userName, password);

        // Assert
        assertNull(result);
    }

    @Test
    public void testLogin_NullUserNameAndPassword() {
        // Act
        User result = userBusiness.login(null, null);

        // Assert
        assertNull(result);
    }
     */

}
