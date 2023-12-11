package com.server.informaViesCat.Configuration.Interceptors.SessionValidation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author leith Aquest interceptor gestiona la sesio del usuario, enc as de
 * estar autoritzat, podra fer ús de del programa.
 */
public class SessionValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
        String uri = request.getRequestURI();
        String sessionId = request.getParameter("sessionId");

        if ( uri.contains("/logout")) {
            
            
            ISessionRepository repo = new SessionRepository();
            boolean isActive = repo.IsActive(sessionId);

            if (!isActive) {

                String mensajeRechazo = "No autoritzat";
                throw new UnauthorizedException(mensajeRechazo);

            }
        } else {
            return true;

        }
         */
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

}
