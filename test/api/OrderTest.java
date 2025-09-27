package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderTest {

    private Order order;
    private Date orderDate;
    private List<ShoppingCart.CartItem> items;
    private double totalCost;

    @Before
    public void setUp() {
        // Δημιουργία ψεύτικων δεδομένων για δοκιμή
        orderDate = new Date();

        ShoppingCart.CartItem item1 = new ShoppingCart.CartItem("Πορτοκάλια 1kg", 1200.50, 1);
        ShoppingCart.CartItem item2 = new ShoppingCart.CartItem("Γάλα Πλήρες 1lt", 2.99, 2.0);

        items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        totalCost = item1.getTotalCost() + item2.getTotalCost();
        order = new Order(orderDate, items, totalCost);
    }

    @Test
    public void testConstructorAndGetters() {
        // Έλεγχος τιμών που αποθηκεύονται από τον constructor
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(items, order.getItems());
        assertEquals(totalCost, order.getTotalCost(), 0.0001);
    }

    @Test
    public void testOrderItems() {
        List<ShoppingCart.CartItem> orderItems = order.getItems();

        // Έλεγχος πλήθους αντικειμένων
        assertEquals(2, orderItems.size());

        // Έλεγχος πρώτου προϊόντος
        ShoppingCart.CartItem item1 = orderItems.get(0);
        assertEquals("Πορτοκάλια 1kg", item1.getProductName());
        assertEquals(1200.50, item1.getPricePerUnit(), 0.0001);
        assertEquals(1, item1.getQuantity(), 0.0001);
        assertEquals(1200.50, item1.getTotalCost(), 0.0001);

        // Έλεγχος δεύτερου προϊόντος
        ShoppingCart.CartItem item2 = orderItems.get(1);
        assertEquals("Γάλα Πλήρες 1lt", item2.getProductName());
        assertEquals(2.99, item2.getPricePerUnit(), 0.0001);
        assertEquals(2.0, item2.getQuantity(), 0.0001);
        assertEquals(2.99 * 2.0, item2.getTotalCost(), 0.0001);
    }

    @Test
    public void testTotalCostCalculation() {
        // Έλεγχος συνολικού κόστους
        double expectedTotalCost = items.stream().mapToDouble(ShoppingCart.CartItem::getTotalCost).sum();
        assertEquals(expectedTotalCost, order.getTotalCost(), 0.0001);
    }
}
