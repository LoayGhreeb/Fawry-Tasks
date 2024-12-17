package org.example.jakarta.hello;

import java.util.List;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/productApp")
public class ProductsServer {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProducts() {
        return ProductRepository.getProducts();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> addProduct(Product newProduct) {
        ProductRepository.addProduct(newProduct);
        return ProductRepository.getProducts();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> removeProduct(String name) {
        ProductRepository.removeProduct(name);
        return ProductRepository.getProducts();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> setProductPrice(String name, double price) {
        ProductRepository.setProductPrice(name, price);
        return ProductRepository.getProducts();
    }
}
