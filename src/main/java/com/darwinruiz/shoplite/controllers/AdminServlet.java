package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.repositories.IProductRepository;
import com.darwinruiz.shoplite.repositories.ProductRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final IProductRepository repo = new ProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setAttribute("products", repo.findAll(0, 10));
            req.getRequestDispatcher("/admin.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stock");
        String contextPath = req.getContextPath();

        // Validation
        if (name == null || name.isEmpty() || priceStr == null || priceStr.isEmpty() || stockStr == null || stockStr.isEmpty()) {
            resp.sendRedirect(contextPath + "/admin.jsp?err=1");
            return;
        }

        try {
            BigDecimal price = new BigDecimal(priceStr);
            int stock = Integer.parseInt(stockStr);
            if (price.compareTo(BigDecimal.ZERO) <= 0 || stock < 0) {
                resp.sendRedirect(contextPath + "/admin.jsp?err=1");
                return;
            }

            Product product = new Product(name.trim(), price, stock);
            repo.create(product);

            resp.sendRedirect(contextPath + "/home");
        } catch (NumberFormatException e) {
            resp.sendRedirect(contextPath + "/admin.jsp?err=1");
        }
    }
}