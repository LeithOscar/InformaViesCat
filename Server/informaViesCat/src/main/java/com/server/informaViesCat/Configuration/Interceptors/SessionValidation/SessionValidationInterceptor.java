package com.server.informaViesCat.Configuration.Interceptors.SessionValidation;

import com.server.informaViesCat.Interfaces.IRepository.ISessionRepository;
import com.server.informaViesCat.Repository.SessionRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author leith Aquest interceptor gestiona la sesio del usuario, enc as de estar autoritzat, podra 
 * fer ús de del programa.
 */
public class SessionValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        
        if(uri.contains("/login/") || uri.contains("/logout/"))
        {
            return true;
        }

        String id = extractIdFromString(uri);
        ISessionRepository repo = new SessionRepository();
        boolean isActive = repo.IsActive(id);

        if (!isActive) {

            String mensajeRechazo = "No autoritzat";
            throw new UnauthorizedException(mensajeRechazo);

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public static String extractIdFromString(String inputString) {
        int lastSlashIndex = inputString.lastIndexOf("/");

        String idWithSlash = inputString.substring(lastSlashIndex);

        String id = idWithSlash.substring(1);

        return id;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public class UnauthorizedException extends RuntimeException {

        public UnauthorizedException(String mensaje) {
            super(mensaje);
        }
    }
}
