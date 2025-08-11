package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.repositories.ProductRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Requisito (POST): validar y crear un nuevo producto en memoria y redirigir a /home.
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final ProductRepository repo = new ProductRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.getRequestDispatcher("/admin.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");
        String contextPath = req.getContextPath();

        if (name == null || name.isEmpty() || priceStr == null || priceStr.isEmpty()) {
            resp.sendRedirect(contextPath + "/admin.jsp?err=1");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price <= 0) {
                resp.sendRedirect(contextPath + "/admin.jsp?err=1");
                return;
            }
                long id = repo.nextId();
                Product product = new Product(id, name.trim(), price);
                repo.add(product);

                resp.sendRedirect(contextPath + "/home");
            } catch(NumberFormatException e){
                resp.sendRedirect(contextPath + "/admin.jsp?err=1");
            }


            // Requisito:
            //  - Leer name y price del formulario.
            //  - Validar: nombre no vacío, precio > 0.
            //  - Generar id con repo.nextId(), crear y guardar en repo.
            //  - Redirigir a /home si es válido; si no, regresar a /admin?err=1.
        }
    }

