package com.server.informaViesCat;

import com.server.informaViesCat.Configuration.ConnectionBD;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author leith
 */
@SpringBootApplication
//@ComponentScan(basePackages = "com.server.informaViesCat") // 
public class InformaViesCat {

    public static void main(String[] args) {
        System.out.println("Hello to InfoviesCat!");

        // Inicia la aplicación Spring Boot
        // aplicar configuració springframework
        
        SpringApplication.run(InformaViesCat.class, args);

        ConnectionBD.conectar();

    }
    
   

}
