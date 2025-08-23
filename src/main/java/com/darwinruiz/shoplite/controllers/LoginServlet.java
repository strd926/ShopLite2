package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.User;
import com.darwinruiz.shoplite.repositories.IUserRepository;
import com.darwinruiz.shoplite.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {
    private final IUserRepository users = new UserRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String contextPath = req.getContextPath();

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            resp.sendRedirect(contextPath + "/login.jsp?err=1");
            return;
        }

        Optional<User> userOpt = users.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password) && userOpt.get().isActive()) {
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("auth", true);
            newSession.setAttribute("username", userOpt.get().getUsername());
            newSession.setAttribute("role", userOpt.get().getRole());
            newSession.setMaxInactiveInterval(30 * 60);

            resp.sendRedirect(contextPath + "/home");
        } else {
            resp.sendRedirect(contextPath + "/login.jsp?err=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}