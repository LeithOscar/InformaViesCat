package com.server.informaViesCat;

import com.server.informaViesCat.Entities.User;
import com.server.informaViesCat.Interfaces.IRepository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


public class CustomUserDetailsService implements UserDetailsService {

    // Debes inyectar una fuente de datos, como un repositorio de usuarios o una conexión a una base de datos
    private IUserRepository userRepository;

    public void CustomUserDetailsService(){}
    
    public CustomUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implementa la lógica para cargar los detalles del usuario desde tu fuente de datos
        // Por ejemplo, busca el usuario en la base de datos por su nombre de usuario
        User user = userRepository.GetByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // Crea un objeto UserDetails con los detalles del usuario y sus roles
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .build();
    }
}
