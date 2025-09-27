package api;

import gui.guiLogin;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class OrderHistoryTest {

    private OrderHistory orderHistory;

    @Before
    public void setUp() throws IOException {
        // Mock ενεργού χρήστη
        guiLogin.activeUser = "testUser";

        // Δημιουργία test αρχείου για τον χρήστη
        File testFile = new File("Files\\CompletedOrders\\testUserOrders.txt");
        if (testFile.exists()) {
            testFile.delete();
        }
        testFile.getParentFile().mkdirs();
        testFile.createNewFile();

        // Δημιουργία instance της κλάσης OrderHistory
        orderHistory = new OrderHistory();
    }

    @Test
    public void testAddOrderAndGetOrders() {
        // Δημιουργία ψεύτικων δεδομένων παραγγελίας
        List<ShoppingCart.CartItem> items = new ArrayList<>();
        items.add(new ShoppingCart.CartItem("Γάλα Πλήρες 1lt", 1200.50, 1));
        items.add(new ShoppingCart.CartItem("Βούτυρο Αγελαδινό 250g", 2.99, 2.0));
        double totalCost = items.stream().mapToDouble(ShoppingCart.CartItem::getTotalCost).sum();

        // Προσθήκη παραγγελίας
        orderHistory.addOrder(items, totalCost);

        // Έλεγχος αν η παραγγελία προστέθηκε σωστά
        List<Order> orders = orderHistory.getOrders();
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals(totalCost, order.getTotalCost(), 0.0001);
        assertEquals(items.size(), order.getItems().size());
    }

    @Test
    public void testDuplicateOrderNotAdded() {
        // Δημιουργία ψεύτικων δεδομένων παραγγελίας
        List<ShoppingCart.CartItem> items = new ArrayList<>();
        items.add(new ShoppingCart.CartItem("Βούτυρο Αγελαδινό 250g", 1200.50, 1));
        items.add(new ShoppingCart.CartItem("Γάλα Πλήρες 1lt", 2.99, 2.0));
        double totalCost = items.stream().mapToDouble(ShoppingCart.CartItem::getTotalCost).sum();

        // Προσθήκη παραγγελίας δύο φορές
        orderHistory.addOrder(items, totalCost);
        orderHistory.addOrder(items, totalCost);

        // Έλεγχος ότι δεν προστέθηκε διπλή παραγγελία
        List<Order> orders = orderHistory.getOrders();
        assertEquals(2, orders.size());
    }

    @Test
    public void testSaveAndLoadOrderHistory() throws IOException {
        // Δημιουργία ψεύτικων δεδομένων παραγγελίας
        List<ShoppingCart.CartItem> items = new ArrayList<>();
        items.add(new ShoppingCart.CartItem("Βούτυρο Αγελαδινό 250g", 1200.50, 1));
        items.add(new ShoppingCart.CartItem("Γάλα Πλήρες 1lt", 2.99, 2.0));
        double totalCost = items.stream().mapToDouble(ShoppingCart.CartItem::getTotalCost).sum();

        // Προσθήκη παραγγελίας
        orderHistory.addOrder(items, totalCost);

        // Δημιουργία νέου instance της OrderHistory για να φορτωθεί το αρχείο
        OrderHistory newOrderHistory = new OrderHistory();
        List<Order> loadedOrders = newOrderHistory.getOrders();

        // Έλεγχος αν το αρχείο φορτώθηκε σωστά
        assertEquals(1, loadedOrders.size());
        Order loadedOrder = loadedOrders.get(0);
        assertEquals(totalCost, loadedOrder.getTotalCost(), 0.0001);
        assertEquals(items.size(), loadedOrder.getItems().size());
    }

    @Test
    public void testInvalidFileFormat() throws IOException {
        // Δημιουργία αρχείου με λανθασμένη μορφή
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Files\\CompletedOrders\\testUserOrders.txt"))) {
            writer.write("InvalidData | InvalidItems | InvalidCost");
            writer.newLine();
        }

        // Δημιουργία νέου instance για να φορτώσει το αρχείο
        OrderHistory newOrderHistory = new OrderHistory();

        // Έλεγχος ότι δεν προστέθηκε καμία παραγγελία
        List<Order> loadedOrders = newOrderHistory.getOrders();
        assertTrue(loadedOrders.isEmpty());
    }

    @Test
    public void testEmptyOrderNotSaved() {
        // Προσπάθεια προσθήκης παραγγελίας χωρίς προϊόντα
        List<ShoppingCart.CartItem> emptyItems = new ArrayList<>();
        double totalCost = 0.0;

        // Καταγραφή μεγέθους παραγγελιών πριν την προσθήκη
        int initialOrderCount = orderHistory.getOrders().size();

        // Προσθήκη άδειας παραγγελίας
        orderHistory.addOrder(emptyItems, totalCost);

        // Καταγραφή μεγέθους παραγγελιών μετά την προσθήκη
        int finalOrderCount = orderHistory.getOrders().size();

        // Έλεγχος ότι το πλήθος παραγγελιών δεν άλλαξε
        assertEquals(initialOrderCount, finalOrderCount);
    }

}
