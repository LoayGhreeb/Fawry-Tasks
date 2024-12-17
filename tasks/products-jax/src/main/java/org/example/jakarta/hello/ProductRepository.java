package org.example.jakarta.hello;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static final List<Product> PRODUCTS = new ArrayList<>();

    static {
        PRODUCTS.add(new Product("Apple", 10));
        PRODUCTS.add(new Product("Banana", 20));
        PRODUCTS.add(new Product("Orange", 15));
    }

    public static List<Product> getProducts() {
        return new ArrayList<>(PRODUCTS);
    }

    public static void addProduct(Product newProduct) {
        String productName = newProduct.getName();
        validateProductName(productName);

        for (Product product : PRODUCTS) {
            if (product.getName().equalsIgnoreCase(productName)) {
                throw new IllegalArgumentException("Product already exists: " + productName);
            }
        }
        PRODUCTS.add(newProduct);
    }

    public static void removeProduct(String name) {
        PRODUCTS.removeIf(product -> product.getName().equalsIgnoreCase(name));
    }

    public static void setProductPrice(String name, double price) {
        for (Product product : PRODUCTS) {
            if (product.getName().equalsIgnoreCase(name)) {
                product.setPrice(price);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found: " + name);
    }

    private static void validateProductName(String name) {
        if (name.length() > 100) {
            throw new IllegalArgumentException("Product name should be less than 100 characters");
        }
    }
}