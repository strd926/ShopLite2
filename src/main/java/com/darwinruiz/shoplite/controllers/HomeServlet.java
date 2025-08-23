package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.repositories.IProductRepository;
import com.darwinruiz.shoplite.repositories.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final IProductRepository repo = new ProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int defaultPage = 1;
        int defaultSize = 6;

        int page;
        try {
            page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : defaultPage;
            if (page < 1) page = defaultPage;
        } catch (NumberFormatException e) {
            page = defaultPage;
        }

        int size;
        try {
            size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : defaultSize;
            if (size < 1) size = defaultSize;
        } catch (NumberFormatException e) {
            size = defaultSize;
        }

        int offset = (page - 1) * size;

        List<Product> items;
        int total;
        int totalPages;
        try {
            items = repo.findAll(offset, size);
            total = repo.countAll();
            totalPages = (total + size - 1) / size;
        } catch (Exception e) {
            items = Collections.emptyList();
            total = 0;
            totalPages = 0;
            req.setAttribute("error", "Error al cargar productos: " + e.getMessage());
        }

        req.setAttribute("items", items);
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("total", total);

        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }
}