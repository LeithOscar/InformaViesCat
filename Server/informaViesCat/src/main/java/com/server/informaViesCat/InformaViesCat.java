package com.server.informaViesCat;

import com.server.informaViesCat.Business.ManageUsers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author leith
 */

@SpringBootApplication
public class InformaViesCat {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        // Inicia la aplicación Spring Boot
        SpringApplication.run(InformaViesCat.class, args);
        
        // init
        ManageUsers manageUsers = new ManageUsers();

    }

}
