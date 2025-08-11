package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.User;
import com.darwinruiz.shoplite.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

/**
 * Requisito: autenticar, regenerar sesión y guardar auth, userEmail, role, TTL.
 */
@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private final UserRepository users = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");
        String contextPath = req.getContextPath();

        if (email == null || pass == null || email.isEmpty() || pass.isEmpty()) {
            resp.sendRedirect(contextPath + "/login.jsp?err=1");
            return;
        }

        Optional<User> userOpt = users.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(pass)) {
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null){
                oldSession.invalidate();
            }

            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("auth", true);
            newSession.setAttribute("userEmail", userOpt.get().getEmail());
            newSession.setAttribute("role", userOpt.get().getRole());
            newSession.setMaxInactiveInterval(30 * 60);

            resp.sendRedirect(contextPath + "/home");
        } else {
            resp.sendRedirect(contextPath + "/login.jsp?err=1");
        }


        // Requisito:
        //  - Si credenciales inválidas → redirect a login.jsp?err=1
        //  - Si válidas → invalidar sesión previa, crear nueva y setear:
        //      auth=true, userEmail, role, maxInactiveInterval (p.ej. 30 min)
        //  - Redirigir a /home

//        resp.sendRedirect(req.getContextPath() + "/home"); // temporal para compilar
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
