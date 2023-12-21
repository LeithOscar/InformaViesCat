
package com.server.informaViesCat.Interfaces.IRepository;

import com.server.informaViesCat.Entities.User.User;
import java.util.List;

/**
 *
 * @author leith
 */
public interface IUserRepository {

    User GetByUsernameAndPassword(String userName, String password);

    Boolean UpdateIsLogged(int userId, boolean state);

    void CreateNewUser(User user);

    boolean Exist(String email);

    boolean Exist(int id);

    List<User> GetAll();
    
    List<User> GetAllByRol(int rolId);
    
    List<User> GetAllWithoutIncidents() ;

    boolean Modify(User user);

    boolean Delete(int id);

    public User GetByUsername(String username);

    public User GetById(int userId);

    boolean ModifyCitizen(User user);

}
