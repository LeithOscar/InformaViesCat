package com.server.informaViesCat.Business;

import com.server.informaViesCat.Entities.User.User;
import com.server.informaViesCat.Entities.User.UserValidations;
import com.server.informaViesCat.Interfaces.IBusiness.IUserBusiness;
import com.server.informaViesCat.Interfaces.IRepository.IUserRepository;
import com.server.informaViesCat.Repository.UserRepository;
import java.util.List;

public class UserBusiness implements IUserBusiness {

    private IUserRepository repo = null;
    private UserValidations userValidations = new UserValidations();

    public UserBusiness() {

        this.repo = new UserRepository();

    }

    public UserBusiness(IUserRepository repoMock) {
        this.repo = repoMock;
    }

    /**
     * login, activa la propiedad islogged a true
     *
     * @param UserName username del usuari
     * @param password Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User Login(String UserName, String password) {

        if (!UserName.isEmpty() & !password.isEmpty()) {
            User user = this.repo.GetByUsernameAndPassword(UserName, password);
            if (user != null) {

                user.SetStatusLogging(this.repo.UpdateIsLogged(user.getId(), true));

                return user;

            } else {
                return null;
            }

        }
        return null;
    }

    /**
     * login, desactiva la propiedad islogged a false
     *
     * @param userId id del usuari
     * @return Retorna una entitat user amb el seu estat
     */
    public boolean Logout(int userId) {

        User user = repo.GetById(userId);

        if (user != null && !user.isLogged()) {
            return repo.UpdateIsLogged(user.getId(), false);
        }

        return false;
    }

    /**
     * login, desactiva la propiedad islogged a false
     *
     * @param user username del usuari
     * @return Retorna true si la operacio es exitosa o false en cas contrari
     */
    public boolean CreateNewUser(User user) {

        if (!repo.Exist(user.GetEmail())) {
            repo.CreateNewUser(user);
            return true;
        }
        return false;

    }

    /**
     * Getall
     *
     * @param parentId
     * @return tota la llista d'usuaries
     */
    public List<User> GetAll() {

        return this.repo.GetAll();

    }

    public boolean Modify(User user) {

        if (user != null) {

            if ((userValidations.IsAdmin(user.getRolId()) || userValidations.isTechnician(user.getRolId()))) {
                return repo.Modify(user);
            } else {
                return repo.ModifyCitizen(user);
            }
        }

        return false;
    }

    public boolean Delete(int id) {

        return this.repo.Delete(id);
    }

}
