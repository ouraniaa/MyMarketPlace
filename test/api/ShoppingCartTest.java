package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;

    @Before
    public void setUp() {
        // Δημιουργία νέου αντικειμένου ShoppingCart για κάθε τεστ
        shoppingCart = new ShoppingCart();
    }

    @Test
    public void testAddProduct() {
        // Προσθήκη προϊόντος στο καλάθι
        shoppingCart.addProduct("Laptop", 1200.50, 1);
        shoppingCart.addProduct("Apples", 2.99, 2);

        // Έλεγχος αν τα προϊόντα προστέθηκαν σωστά
        assertEquals(2, shoppingCart.getCartDetails().size());

        // Έλεγχος αν η τιμή του Laptop υπολογίστηκε σωστά
        ShoppingCart.CartItem laptop = shoppingCart.getCartDetails().stream()
                .filter(item -> item.getProductName().equals("Laptop"))
                .findFirst()
                .orElse(null);

        assertNotNull(laptop);
        assertEquals(1200.50, laptop.getTotalCost(), 0.0001);

        // Έλεγχος αν η τιμή των Apples υπολογίστηκε σωστά
        ShoppingCart.CartItem apples = shoppingCart.getCartDetails().stream()
                .filter(item -> item.getProductName().equals("Apples"))
                .findFirst()
                .orElse(null);

        assertNotNull(apples);
        assertEquals(2 * 2.99, apples.getTotalCost(), 0.0001);
    }


    @Test
    public void testRemoveProduct() {
        // Προσθήκη προϊόντος στο καλάθι
        shoppingCart.addProduct("Laptop", 1200.50, 1);
        shoppingCart.addProduct("Apples", 2.99, 2);

        // Αφαίρεση προϊόντος από το καλάθι
        shoppingCart.removeProduct("Laptop");

        // Έλεγχος αν το προϊόν αφαιρέθηκε σωστά
        assertEquals(1, shoppingCart.getCartDetails().size());
        assertFalse(shoppingCart.getCartDetails().stream().anyMatch(item -> item.getProductName().equals("Laptop")));
    }

    @Test
    public void testUpdateProductQuantity() {
        // Προσθήκη προϊόντος στο καλάθι
        shoppingCart.addProduct("Laptop", 1200.50, 1);

        // Ενημέρωση ποσότητας προϊόντος
        shoppingCart.updateProductQuantity("Laptop", 2, 10);

        // Έλεγχος αν η ποσότητα και η τιμή ενημερώθηκαν σωστά
        assertEquals(2, shoppingCart.getCartDetails().get(0).getQuantity(), 0.0001);
        assertEquals(2401.00, shoppingCart.getCartDetails().get(0).getTotalCost(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProductQuantityExceedsStock() {
        // Προσθήκη προϊόντος στο καλάθι
        shoppingCart.addProduct("Laptop", 1200.50, 1);

        // Προσπάθεια ενημέρωσης της ποσότητας με ποσότητα μεγαλύτερη από το απόθεμα
        shoppingCart.updateProductQuantity("Laptop", 10, 5);
    }

    @Test
    public void testCompleteOrder() {
        // Προσθήκη προϊόντων στο καλάθι
        shoppingCart.addProduct("Laptop", 1200.50, 1);
        shoppingCart.addProduct("Apples", 2.99, 2);

        // Καταγραφή αρχικής τιμής
        double initialCost = shoppingCart.getTotalCost();

        // Υλοποίηση ολοκλήρωσης παραγγελίας (η οποία καθαρίζει το καλάθι)
        shoppingCart.completeOrder();

        // Έλεγχος ότι το καλάθι είναι άδειο μετά την ολοκλήρωση
        assertTrue(shoppingCart.getCartDetails().isEmpty());
        assertEquals(0.0, shoppingCart.getTotalCost(), 0.0001);
    }

    @Test(expected = IllegalStateException.class)
    public void testCompleteOrderWithEmptyCart() {
        // Προσπάθεια ολοκλήρωσης παραγγελίας με άδειο καλάθι
        shoppingCart.completeOrder();
    }

    @Test
    public void testAddProductWithInvalidValues() {
        // Προσπάθεια προσθήκης προϊόντος με λανθασμένες τιμές
        shoppingCart.addProduct("Laptop", -1200.50, 1);
        shoppingCart.addProduct("Apples", 2.99, -2);

        // Έλεγχος ότι το προϊόν δεν προστέθηκε
        assertTrue(shoppingCart.getCartDetails().isEmpty());
    }

    @Test
    public void testGetTotalCost() {
        // Προσθήκη προϊόντων στο καλάθι
        shoppingCart.addProduct("Laptop", 1200.50, 1);
        shoppingCart.addProduct("Apples", 2.99, 2);

        // Έλεγχος ότι το συνολικό κόστος είναι σωστό
        double expectedTotalCost = 1200.50 + (2 * 2.99);
        assertEquals(expectedTotalCost, shoppingCart.getTotalCost(), 0.0001);
    }
}
