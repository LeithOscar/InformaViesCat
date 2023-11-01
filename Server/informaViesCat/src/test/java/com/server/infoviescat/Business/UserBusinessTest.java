package com.server.infoviescat.Business;

import com.server.informaViesCat.Business.UserBusiness;
import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Entities.UserValidations;
import com.server.informaViesCat.Interfaces.IBusiness.IUserBusiness;
import com.server.informaViesCat.Interfaces.IBusiness.IUserValidations;
import com.server.informaViesCat.Interfaces.IRepository.IUserRepository;
import com.server.informaViesCat.Repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author leith Clase de test unitaris de la clase user Busines, testeja tota
 * la logica del negoci referent amb l'usuari
 *
 * frameWork JUnit Metodologia Assert
 */
public class UserBusinessTest {

    private IUserBusiness userBusiness;
    private IUserRepository repoMock;
    private IUserValidations userValidationsMock;

    public UserBusinessTest() {

        repoMock = mock(UserRepository.class);
        userValidationsMock = mock(IUserValidations.class);

        userBusiness = new UserBusiness(repoMock);
    }

    @Test
    public void testLogin_SuccessfulLogin() {
        // Arrange
        String userName = "testuser";
        String password = "testpassword";
        User mockUser = new User(1, 1, userName, password, false, "", "", "");

        when(repoMock.GetByUsernameAndPassword(userName, password)).thenReturn(mockUser);
        when(repoMock.UpdateIsLogged(mockUser, true)).thenReturn(mockUser);

        // Act
        User result = userBusiness.Login(userName, password);

        // Assert
        verify(repoMock).UpdateIsLogged(mockUser, true);

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
        User result = userBusiness.Login(userName, password);

        // Assert
        assertNull(result);

    }

    @Test
    public void testLogin_EmptyUserNameAndPassword() {
        // Arrange
        String userName = "";
        String password = "";

        // Act
        User result = userBusiness.Login(userName, password);

        // Assert
        assertNull(result);
    }

    @Test
    public void testLogoutWithValidUser() {
        String userName = "validUser";
        String password = "validPassword";

        User mockUser = new User(1, 1, userName, password, true, "", "", "");

        when(repoMock.GetByUsernameAndPassword(userName, password)).thenReturn(mockUser);

        // Act
        User result = userBusiness.Logout(userName, password);

        // Assert
        verify(repoMock).UpdateIsLogged(mockUser, false);

    }

    @Test
    public void testLogoutWithInvalidUser() {
        String userName = "";
        String password = "";

        // Act
        User result = userBusiness.Logout(userName, password);
        // Assert
        assertNull(result);
    }

    @Test
    public void testCreateNewUserWithValidUser() {
        String userName = "validUser";
        String password = "validPassword";

        User mockUser = new User(1, 1, userName, password, true, "", "", "");

        when(repoMock.Exist(mockUser.GetEmail())).thenReturn(false);
        // Act
        boolean result = userBusiness.CreateNewUser(mockUser);

        // Assert
        verify(repoMock).CreateNewUser(mockUser);

    }

    @Test
    public void testDeleteUserWithValidId() {
        int userId = 12;

        // Act
        boolean result = userBusiness.Delete(userId);

        // Assert
        verify(repoMock).Delete(userId);

    }

    @Test
    public void testModifyUserWithInValidRolUser() {
        String userName = "validUser";
        String password = "validPassword";

        User mockUser = new User(1, 3, userName, password, true, "", "", "");

        when(userValidationsMock.IsUser(mockUser.getRolId())).thenReturn(true);
        // Act
        boolean result = userBusiness.Modify(mockUser);

        // Assert
        assertFalse(result);

    }

    @Test
    public void testModifyUserWithValidAdminUser() {
        String userName = "validUser";
        String password = "validPassword";

        User mockUser = new User(1, 1, userName, password, true, "", "", "");

        when(userValidationsMock.IsAdmin(mockUser.getRolId())).thenReturn(true);
        // Act
        boolean result = userBusiness.Modify(mockUser);

        // Assert
        verify(repoMock).Modify(mockUser);

    }

    @Test
    public void testModifyUserWithValidTecnicUser() {
        String userName = "validUser";
        String password = "validPassword";

        User mockUser = new User(1, 1, userName, password, true, "", "", "");

        when(userValidationsMock.IsTecnic(mockUser.getRolId())).thenReturn(true);
        // Act
        boolean result = userBusiness.Modify(mockUser);

        // Assert
        verify(repoMock).Modify(mockUser);

    }

}
