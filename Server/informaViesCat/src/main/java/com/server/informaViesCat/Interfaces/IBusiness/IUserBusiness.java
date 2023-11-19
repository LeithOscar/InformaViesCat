
package com.server.informaViesCat.Interfaces.IBusiness;

import com.server.informaViesCat.Entities.User.User;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IUserBusiness  {

    public User Login(String UserName, String password);

    User Logout(int userId);

    boolean CreateNewUser(User user);

    List<User> GetAll();

    boolean Modify(User user);
    
    boolean Delete (int id);

}
