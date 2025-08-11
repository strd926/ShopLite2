package com.darwinruiz.shoplite.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Requisito: permitir /admin solo a usuarios con rol "ADMIN"; los demás ven 403.jsp.
 */
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse p = (HttpServletResponse) res;

        HttpSession session = r.getSession(false);
        boolean isAuthenticated = session != null && Boolean.TRUE.equals(session.getAttribute("auth"));

        if (!isAuthenticated) {
            chain.doFilter(req, res);
            return;
        }

        String role = (String) session.getAttribute("role");
        if("ADMIN".equals(role)) {
            chain.doFilter(req, res);
        } else {
            r.getRequestDispatcher("/403.jsp").forward(req, res);
        }

        // Requisito: validar sesión existente y atributo "role" con valor "ADMIN".
        // Si no cumple, hacer forward a /403.jsp. Si cumple, continuar.
    }
}
