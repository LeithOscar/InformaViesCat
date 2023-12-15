package com.server.informaViesCat.Configuration;

import com.server.informaViesCat.Entities.User.User;
import com.server.informaViesCat.Interfaces.IRepository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomUserDetailsService implements UserDetailsService {

    // Debes inyectar una fuente de datos, como un repositorio de usuarios o una conexión a una base de datos
    private IUserRepository userRepository;

    public void CustomUserDetailsService(){}
    
    public CustomUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.GetByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // Crea un objeto UserDetails amb els detalls de l'usuari
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
