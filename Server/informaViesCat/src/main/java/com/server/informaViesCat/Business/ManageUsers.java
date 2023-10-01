
package com.server.informaViesCat.Business;

import com.server.informaViesCat.Entities.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leith 
 * Aquesta és una classe que representa un usuari.
 * Conté informació sobre el nom, la credencial i l'estat de connexió de l'usuari.
 */


/* test */
public class ManageUsers {

    private static List<User> llistaUsuaris;
    private static List<User> llistaUsuarisConectats;

    public ManageUsers() {
        llistaUsuaris = new ArrayList<>();
        llistaUsuarisConectats = new ArrayList<>();
        
        llistaUsuaris.add(new User("Administrador", "admin1234", false));
        llistaUsuaris.add(new User("Tecnico", "tecnic1234", false));
        llistaUsuaris.add(new User("Usuario", "usuario1234", false));
    }

    /**
     * Crea una instància de la classe User amb el nom i la credencial
     * especificats.
     *
     * @param name El nom de l'usuari.
     * @param credencial La credencial de l'usuari.
     * @return Object user amb informació i el seu estat.
     */
    public static User autenticarUsuario(String name, String credencial) {
        for (User usuario : llistaUsuaris) {
            if (usuario.getName().equals(name) && usuario.getCredential().equals(credencial)) {
                if (!llistaUsuarisConectats.contains(usuario)) {
                    usuario.Connect();
                    llistaUsuarisConectats.add(usuario);
                }

                return new User(usuario.getName(), usuario.getCredential(), true);
            }
        }
        return new User(name, credencial, false);
    }

    /**
     * Elimina un usuari a la llista de conectats, y retornar User object amb
     * informació si esta o conectat
     *
     * @param name Nom de usuario a autenticar.
     * @param credencial Credencial del usuari.
     * @return Un objecte Usuari amb el nom, la credencial i l'estat de
     * connexió. 
     *
     */
    public static User desconectarUsuario(String name, String credencial) {
        User usuarioDesconectado = null;
        for (User usuario : llistaUsuarisConectats) {
            if (usuario.getName().equals(name) && usuario.getCredential().equals(credencial)) {
                usuario.Disconnect();
                llistaUsuarisConectats.remove(usuario);
                usuarioDesconectado = usuario;
                break;
            }
        }
        if (usuarioDesconectado != null) {
            return new User(usuarioDesconectado.getName(), usuarioDesconectado.getCredential(), false);
        } else {
            return new User(name, credencial, false);
        }
    }
}
