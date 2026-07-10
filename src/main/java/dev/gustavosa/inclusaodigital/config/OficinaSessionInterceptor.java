package dev.gustavosa.inclusaodigital.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Garante que as etapas da oficina posteriores ao cadastro só sejam
 * acessadas com um participantId válido na sessão. Centraliza aqui a
 * checagem que, sem isso, teria que ser repetida em cada novo controller
 * (questionário, materiais, quiz).
 */
@Component
public class OficinaSessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("participantId") == null) {
            response.sendRedirect("/oficina/cadastro");
            return false;
        }
        return true;
    }
}
