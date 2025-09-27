package api;

import java.util.*;

public class ShoppingCart {
    private Map<String, CartItem> cartItems;
    private double totalCost;

    public ShoppingCart() {
        this.cartItems = new HashMap<>();
        this.totalCost = 0.0;
    }

    public void completeOrder() {
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Το καλάθι είναι άδειο.");
        }

        String filePath = "Files\\Products.txt"; // Το αρχείο των προϊόντων

        // Ενημέρωση αποθέματος προϊόντων
        fileEditor.updateProductStock(filePath, getCartDetails());

        // Καθαρισμός καλαθιού
        cartItems.clear();
        totalCost = 0.0;
    }

    public void addProduct(String productName, double pricePerUnit, double quantity) {
        if (quantity <= 0 || pricePerUnit <= 0) {
            System.out.println("Λανθασμένες τιμές ή ποσότητες. Δεν προστίθεται το προϊόν.");
            return; // Ακύρωση προσθήκης προϊόντος αν οι τιμές ή ποσότητες είναι μη έγκυρες
        }
        if (cartItems.containsKey(productName)) {
            CartItem item = cartItems.get(productName);
            item.setQuantity(item.getQuantity() + quantity);
            item.setTotalCost(item.getQuantity() * pricePerUnit);
        } else {
            cartItems.put(productName, new CartItem(productName, pricePerUnit, quantity));
        }
        updateTotalCost();
    }

    public void removeProduct(String productName) {
        Iterator<Map.Entry<String, CartItem>> iterator = cartItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CartItem> entry = iterator.next();
            if (entry.getKey().equals(productName)) {
                iterator.remove();  // Αφαίρεση του προϊόντος με τη χρήση του iterator
                updateTotalCost();
                System.out.println("Το προϊόν " + productName + " αφαιρέθηκε από το καλάθι.");
                return;
            }
        }
        System.out.println("Το προϊόν με το όνομα " + productName + " δεν βρέθηκε στο καλάθι.");
    }

    public void updateProductQuantity(String productName, double newQuantity, double availableStock) {
        if (cartItems.containsKey(productName)) {
            if (newQuantity > availableStock) {
                throw new IllegalArgumentException("Η νέα ποσότητα υπερβαίνει τη διαθέσιμη ποσότητα στο απόθεμα.");
            }
            CartItem item = cartItems.get(productName);
            item.setQuantity(newQuantity);
            item.setTotalCost(item.getPricePerUnit() * newQuantity);
            updateTotalCost();
        }
    }

    public double getTotalCost() {
        return totalCost;
    }

    public List<CartItem> getCartDetails() {
        return new ArrayList<>(cartItems.values());
    }

    private void updateTotalCost() {
        totalCost = cartItems.values().stream().mapToDouble(CartItem::getTotalCost).sum();
    }

    public static class CartItem {
        private String productName;
        private double pricePerUnit;
        private double quantity;
        private double totalCost;

        public CartItem(String productName, double pricePerUnit, double quantity) {
            this.productName = productName;
            this.pricePerUnit = pricePerUnit;
            this.quantity = quantity;
            this.totalCost = pricePerUnit * quantity;
        }

        public String getProductName() {
            return productName;
        }

        public double getPricePerUnit() {
            return pricePerUnit;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }
}