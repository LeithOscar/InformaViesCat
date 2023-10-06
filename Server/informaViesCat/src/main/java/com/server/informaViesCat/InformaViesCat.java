package com.server.informaViesCat;

import java.sql.SQLException;
import javax.crypto.AEADBadTagException;
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

        CConnection.conectar();


    }
    
   

}
