package org.example.productsservlet;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/productApp")
public class ProductServlet extends HttpServlet {
    private static final Gson GSON = new Gson();
    private ProductRepository productRepository = new ProductRepository();

    @Override
    public void init() {
        productRepository = new ProductRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Product> products = productRepository.getProducts();
        String json = GSON.toJson(products);

        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("name");
            String priceParameter = req.getParameter("price");
            if (name == null || name.isBlank() || priceParameter == null || priceParameter.isBlank()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Name and price should be provided");
                return;
            }
            double price = Double.parseDouble(priceParameter);
            Product newProduct = new Product(name, price);
            productRepository.addProduct(newProduct);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            // return all products
            doGet(req, resp);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Price should be a number");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null || name.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Name should be provided");
            return;
        }
        try {
            productRepository.removeProduct(name);
            resp.setStatus(HttpServletResponse.SC_OK);
            doGet(req, resp);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String priceParameter = req.getParameter("price");
        if (name == null || name.isBlank() || priceParameter == null || priceParameter.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Name and price should be provided");
            return;
        }
        try {
            double price = Double.parseDouble(priceParameter);
            productRepository.setProductPrice(name, price);
            resp.setStatus(HttpServletResponse.SC_OK);
            doGet(req, resp);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Price should be a number");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}
