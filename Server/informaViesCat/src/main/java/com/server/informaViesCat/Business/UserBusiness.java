
package com.server.informaViesCat.Business;

import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Interfaces.IBusiness.IUserBusiness;
import com.server.informaViesCat.Interfaces.IRepository.IUserRepository;
import com.server.informaViesCat.Repository.UserRepository;
import java.util.List;

public class UserBusiness implements IUserBusiness {

    private IUserRepository repo = null;

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

                User UserUpdated = this.repo.UpdateIsLogged(user, true);
                return UserUpdated;

            } else {
                return null;
            }

        }
        return null;
    }

    /**
     * login, desactiva la propiedad islogged a false
     *
     * @param UserName username del usuari
     * @param password Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public User Logout(String UserName, String password) {

        if (!UserName.isEmpty() & !password.isBlank()) {
            User user = this.repo.GetByUsernameAndPassword(UserName, password);
           if (user != null && user.isLogged()) {

                User UserUpdated = this.repo.UpdateIsLogged(user, false);
                return UserUpdated;

            } else {
                return null;
            }

        }
        return null;

    }

    /**
     * login, desactiva la propiedad islogged a false
     *
     * @param user username del usuari
     * @return Retorna true si la operacio es exitosa o false en cas contrari
     */
    public boolean CreateNewUser(User user) {

        if (user != null) {

            var userEmail = this.repo.Exist(user.GetEmail());
            if (userEmail == 0) {

                this.repo.CreateNewUser(user);
                return true;

            } else {
                return false;
            }
        }

        return false;

    }

      /**
     * Getall
     *
     * @return tota la llista d'usuaries
     */
    public List<User> GetAll() {

        return this.repo.GetAll();

    }

    public boolean Modify(User user) {

        if(user!= null){
             return this.repo.Modify(user);
        }
        return false;
    }

    public boolean Delete(int id) {
        
          return this.repo.Delete(id);
    }

}
