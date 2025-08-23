package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.User;
import com.darwinruiz.shoplite.repositories.IUserRepository;
import com.darwinruiz.shoplite.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final IUserRepository users = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String contextPath = req.getContextPath();


        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Usuario y contraseña son requeridos");
            resp.sendRedirect(contextPath + "/register.jsp?err=1");
            return;
        }

        if (role == null || (!role.equals("USER") && !role.equals("ADMIN"))) {
            role = "USER";
        }

        User newUser = new User(0, username, password, role, true);

        try {
            users.create(newUser);
            resp.sendRedirect(contextPath + "/login.jsp?msg=Registro exitoso");
        } catch (RuntimeException e) {
            String errorMsg = e.getMessage().contains("unique constraint")
                    ? "El usuario ya existe"
                    : "Error en el registro: " + e.getMessage();
            req.setAttribute("error", errorMsg);
            resp.sendRedirect(contextPath + "/register.jsp?err=1");
        }
    }
}