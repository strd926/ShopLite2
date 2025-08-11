package com.darwinruiz.shoplite.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Requisito: bloquear todo lo no público si no existe una sesión con auth=true.
 */
public class AuthFilter implements Filter {
    private static final List<String> PUBLIC_URIS = Arrays.asList(
            "/index.jsp",
            "/login.jsp",
            "/auth/login",
            "/auth/logout",
            "/"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String relativeUri = uri.startsWith(contextPath) && contextPath.length() > 0
                ? uri.substring(contextPath.length()) : uri;

        boolean esPublica =
                uri.endsWith("/index.jsp") || uri.endsWith("/login.jsp") ||
                        uri.contains("/auth/login") || uri.endsWith("/");

        if (esPublica) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean isAuthenticated = session != null && Boolean.TRUE.equals(session.getAttribute("auth"));

        if (isAuthenticated) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(contextPath + "/login.jsp");
        }


    }
}