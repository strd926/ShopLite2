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
import java.math.BigDecimal;
import java.util.logging.Logger;

@WebServlet({"/admin/edit-product", "/admin/delete-product"})
public class AdminProductServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminProductServlet.class.getName());
    private final IProductRepository repo = new ProductRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        String path = req.getServletPath();

        if ("/admin/edit-product".equals(path)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                BigDecimal price = new BigDecimal(req.getParameter("price"));
                int stock = Integer.parseInt(req.getParameter("stock"));

                if (name == null || name.isEmpty() || price.compareTo(BigDecimal.ZERO) <= 0 || stock < 0) {
                    LOGGER.warning("Entrada Invalida: id=" + id);
                    resp.sendRedirect(contextPath + "/home?err=Invalid input");
                    return;
                }

                Product product = new Product(id, name, price, stock);
                repo.update(product);
                LOGGER.info("Producto Actualizado: id=" + id);
                resp.sendRedirect(contextPath + "/home?msg=Producto actualizado");
            } catch (NumberFormatException e) {
                LOGGER.warning("Formato de numero invalido: " + e.getMessage());
                resp.sendRedirect(contextPath + "/home?err=Formato de numero invalido");
            } catch (Exception e) {
                LOGGER.severe("Error al actualizar producto: " + e.getMessage());
                resp.sendRedirect(contextPath + "/home?err=Error updating product: " + e.getMessage());
            }
        } else if ("/admin/delete-product".equals(path)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                repo.deleteById(id);
                LOGGER.info("Producto Eliminado: id=" + id);
                resp.sendRedirect(contextPath + "/home?msg=Producto eliminado");
            } catch (NumberFormatException e) {
                LOGGER.warning("Formato Invalido: " + e.getMessage());
                resp.sendRedirect(contextPath + "/home?err=Formato Invalido");
            } catch (Exception e) {
                LOGGER.severe("Falla al eliminar Producto: " + e.getMessage());
                resp.sendRedirect(contextPath + "/home?err=Error deleting product: " + e.getMessage());
            }
        }
    }
}