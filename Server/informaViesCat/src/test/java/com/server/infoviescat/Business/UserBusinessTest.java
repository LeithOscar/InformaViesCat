/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.server.infoviescat.Business;

import com.server.informaViesCat.Business.UserBusiness;
import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Interfaces.IBusiness.IUserBusiness;
import com.server.informaViesCat.Interfaces.IRepository.IUserRepository;
import com.server.informaViesCat.Repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author leith
 */
public class UserBusinessTest {

    private IUserBusiness userBusiness;
    private IUserRepository repoMock;

    public UserBusinessTest() {

        repoMock = mock(UserRepository.class);

        userBusiness = new UserBusiness(repoMock);
    }

    @Test
    public void testLogin_SuccessfulLogin() {
        // Arrange
        String userName = "testuser";
        String password = "testpassword";
        User mockUser = new User(1, 1, userName, password, true, "", "", "");

        when(repoMock.GetByUsernameAndPassword(userName, password)).thenReturn(mockUser);
        when(repoMock.UpdateIsLogged(mockUser, true)).thenReturn(mockUser);

        // Act
        User result = userBusiness.login(userName, password);

        // Assert
        assertNotNull(result);
        assertTrue(result.isLogged());
        //assertEquals(mockUser, result);
    }

    @Test
    public void testLogin_UnsuccessfulLogin() {
        // Arrange
        String userName = "testuser";
        String password = "testpassword";
        User mockUser = new User(1, 1, userName, password, true, "", "", "");

        when(repoMock.GetByUsernameAndPassword(userName, password)).thenReturn(null);
        when(repoMock.UpdateIsLogged(mockUser, true)).thenReturn(null);

        // Act
        User result = userBusiness.login(userName, password);

        // Assert
        assertNull(result);
       
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



}
