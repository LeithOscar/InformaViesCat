
package com.server.informaViesCat.Configuration.Interceptors;

import com.server.informaViesCat.Configuration.Interceptors.Crypto.EncryptionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author leith
 * Clase que registra cada Interceptor creat. es una extension de SpringFrameWork.
 * S'ha possat per separar la configuració.
 */
@Configuration
public class WebInterceptorsConfiguration implements WebMvcConfigurer {
   
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new EncryptionInterceptor());
    }
}
