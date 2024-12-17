package org.example.productsservlet;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    public ProductRepository() {
        products.add(new Product("Apple", 10));
        products.add(new Product("Banana", 20));
        products.add(new Product("Orange", 15));
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void addProduct(Product newProduct) {
        String productName = newProduct.getName();
        validateProductName(productName);

        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                throw new IllegalArgumentException("Product already exists: " + productName);
            }
        }
        products.add(newProduct);
    }

    public void removeProduct(String name) {
        products.removeIf(product -> product.getName().equalsIgnoreCase(name));
    }

    public void setProductPrice(String name, double price) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                product.setPrice(price);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found: " + name);
    }

    private void validateProductName(String name) {
        if (name.length() > 100) {
            throw new IllegalArgumentException("Product name should be less than 100 characters");
        }
    }
}
