
package com.server.informaViesCat.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 *
 * @author leith
 * configuració per habilitar el acces o restringir.
 * Configuració Spring framworks
 */
@Configuration
@EnableWebSecurity
public class SpringFrameWorkConfiguration extends WebSecurityConfigurerAdapter {
 
   
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                
            .authorizeRequests()
                .antMatchers("/**").permitAll()// Dona acces total
               //.antMatchers("/api/**").hasRole("USER")

            .and()
                
                //and()
            //.httpBasic()
            //    .and()
            .csrf().disable();
    }
    
    /*
   
    
      @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
    */
    
  
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
}

