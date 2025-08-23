package com.darwinruiz.shoplite.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        boolean isAuthenticated = session != null && Boolean.TRUE.equals(session.getAttribute("auth"));

        // Si no hay sesion, redirige a login
        if (!isAuthenticated) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // verifica si el user tienen ADMIN role
        String role = (String) session.getAttribute("role");
        if ("ADMIN".equals(role)) {
            chain.doFilter(req, res);
        } else {
            request.getRequestDispatcher("/403.jsp").forward(req, res);
        }
    }
}