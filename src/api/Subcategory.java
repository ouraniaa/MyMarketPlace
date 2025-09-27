package api;

import java.util.ArrayList;

//Η κλάση Subcategory αναπαριστά μία υποκατηγορία προϊόντων, η οποία περιέχει το όνομα της υποκατηγορίας και τη λίστα των προϊόντων.

public class Subcategory {
    private String name;
    private ArrayList<Product> products;

    public Subcategory(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Subcategory: " + name + ", Products: " + products.size() + " items";
    }
}
