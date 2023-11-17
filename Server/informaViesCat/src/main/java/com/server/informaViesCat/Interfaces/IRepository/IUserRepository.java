
package com.server.informaViesCat.Interfaces.IRepository;

import com.server.informaViesCat.Entities.User.User;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IUserRepository {

    User GetByUsernameAndPassword(String userName, String password);

    User UpdateIsLogged(User user, boolean state);

    void CreateNewUser(User user);

    boolean Exist(String email);

    boolean Exist(int id);

    List<User> GetAll(int parentId);

    boolean Modify(User user);

    boolean Delete(int id);

    public User GetByUsername(String username);

    public User GetById(int userId);

}
