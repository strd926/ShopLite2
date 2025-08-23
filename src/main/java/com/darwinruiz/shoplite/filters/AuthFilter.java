package com.darwinruiz.shoplite.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthFilter implements Filter {
    private static final List<String> PUBLIC_URIS = Arrays.asList(
            "/index.jsp",
            "/login.jsp",
            "/register.jsp",
            "/auth/login",
            "/auth/logout",
            "/register",
            "/"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String relativeUri = uri.startsWith(contextPath) && !contextPath.isEmpty()
                ? uri.substring(contextPath.length()) : uri;


        if (PUBLIC_URIS.contains(relativeUri)) {
            chain.doFilter(req, res);
            return;
        }

        // Verifica la autenticación
        HttpSession session = request.getSession(false);
        boolean isAuthenticated = session != null && Boolean.TRUE.equals(session.getAttribute("auth"));

        if (isAuthenticated) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(contextPath + "/login.jsp");
        }
    }
}