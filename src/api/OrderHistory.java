package api;

import java.io.*;
import gui.guiLogin;
import java.text.SimpleDateFormat;
import java.util.*;

//Η κλάση OrderHistory χειρίζεται το ιστορικό παραγγελιών ενός χρήστη.
// * Περιλαμβάνει λειτουργίες για την προσθήκη νέων παραγγελιών, τη φόρτωση ιστορικού από αρχείο, και την αποθήκευση του ιστορικού πίσω στο αρχείο.

public class OrderHistory {
    private List<Order> orders;
    private String userFilePath; // Μοναδικό αρχείο ιστορικού για κάθε χρήστη

    public OrderHistory() {
        String username = guiLogin.activeUser;
        this.orders = new ArrayList<>();
        this.userFilePath = "Files\\CompletedOrders\\" + username + "Orders.txt";
        loadOrderHistory(); // Φόρτωση ιστορικού από το αρχείο
    }

    // Προσθήκη νέας παραγγελίας στο ιστορικό
    public void addOrder(List<ShoppingCart.CartItem> cartItems, double totalCost) {
        // Άμεσος έλεγχος για μη έγκυρες παραγγελίες
        if (cartItems == null || cartItems.isEmpty() || totalCost <= 0.0) {
            System.out.println("Η παραγγελία δεν περιέχει προϊόντα ή το κόστος είναι μηδέν. Δεν προστίθεται.");
            return;
        }

        Order newOrder = new Order(new Date(), cartItems, totalCost);

        // Έλεγχος αν η παραγγελία υπάρχει ήδη
        for (Order order : orders) {
            if (order.getOrderDate().equals(newOrder.getOrderDate()) &&
                    order.getTotalCost() == newOrder.getTotalCost() &&
                    order.getItems().equals(newOrder.getItems())) {
                System.out.println("Η παραγγελία ήδη υπάρχει στο ιστορικό.");
                return; // Ακύρωση της προσθήκης
            }
        }

        orders.add(newOrder);
        saveOrderHistory(); // Αποθήκευση στο αρχείο
    }




    // Επιστροφή όλων των παραγγελιών
    public List<Order> getOrders() {
        return orders;
    }

    // Φόρτωση ιστορικού από το αρχείο
    private void loadOrderHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| "); // Χωρίζουμε με βάση τον διαχωριστή
                if (parts.length != 3) {
                    System.out.println("Λανθασμένη μορφή γραμμής στο αρχείο: " + line);
                    continue;
                }

                String dateStr = parts[0];
                String itemsStr = parts[1];
                double totalCost;

                try {
                    totalCost = Double.parseDouble(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Μη έγκυρο συνολικό κόστος: " + parts[2]);
                    continue;
                }

                Date orderDate;
                try {
                    orderDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateStr);
                } catch (java.text.ParseException e) {
                    System.out.println("Μη έγκυρη ημερομηνία: " + dateStr);
                    continue;
                }

                // Ανακατασκευή της λίστας των CartItems
                List<ShoppingCart.CartItem> items = new ArrayList<>();
                String[] itemDetails = itemsStr.split(";");
                for (String itemDetail : itemDetails) {
                    String[] itemParts = itemDetail.split(",");
                    if (itemParts.length != 3 || itemParts[0].isEmpty() || itemParts[1].isEmpty() || itemParts[2].isEmpty()) {
                        System.out.println("Λανθασμένη μορφή προϊόντος: " + itemDetail);
                        System.out.println("Πλήρης γραμμή: " + line);
                        continue;
                    }
                    try {
                        String productName = itemParts[0];
                        double quantity = Double.parseDouble(itemParts[1]);
                        double pricePerUnit = Double.parseDouble(itemParts[2]);
                        items.add(new ShoppingCart.CartItem(productName, pricePerUnit, quantity));
                    } catch (NumberFormatException e) {
                        System.out.println("Λανθασμένα δεδομένα προϊόντος: " + itemDetail);
                        System.out.println("Πλήρης γραμμή: " + line);
                    }
                }


                orders.add(new Order(orderDate, items, totalCost));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Δεν υπάρχει ιστορικό παραγγελιών για τον χρήστη.");
        } catch (IOException e) {
            throw new RuntimeException("Σφάλμα κατά τη φόρτωση του ιστορικού παραγγελιών.", e);
        }
    }

    // Αποθήκευση ιστορικού στο αρχείο
    private void saveOrderHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            for (Order order : orders) {
                if (order.getItems().isEmpty() || order.getTotalCost() == 0.0) {
                    System.out.println("Η παραγγελία δεν περιέχει προϊόντα ή το κόστος είναι μηδέν. Δεν αποθηκεύεται.");
                    continue; // Αγνόηση παραγγελίας χωρίς προϊόντα
                }

                StringBuilder itemsStr = new StringBuilder();
                for (ShoppingCart.CartItem item : order.getItems()) {
                    // Έλεγχος για έγκυρη μορφή προϊόντων
                    if (item.getProductName() == null || item.getProductName().isEmpty() ||
                            item.getQuantity() <= 0 || item.getPricePerUnit() <= 0) {
                        System.out.println("Λανθασμένη μορφή προϊόντος: " + item.getProductName());
                        continue; // Αγνόηση λανθασμένων προϊόντων
                    }
                    itemsStr.append(item.getProductName()).append(",")
                            .append(item.getQuantity()).append(",")
                            .append(item.getPricePerUnit()).append(";");
                }

                // Αποθήκευση έγκυρης παραγγελίας
                writer.write(sdf.format(order.getOrderDate()) + " | " +
                        itemsStr + " | " + order.getTotalCost());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Σφάλμα κατά την αποθήκευση του ιστορικού παραγγελιών.", e);
        }
    }

}
