package com.server.informaViesCat;

import com.server.informaViesCat.Configuration.ConnectionBD;
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
        System.out.println("Hello to InfoviesCat!");

        // Inicia la aplicación Spring Boot
        SpringApplication.run(InformaViesCat.class, args);

        ConnectionBD.conectar();

    }
    
   

}
