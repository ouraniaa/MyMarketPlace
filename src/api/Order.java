package api;

import java.util.Date;
import java.util.List;

//Η κλάση Order αναπαριστά μια παραγγελία που περιέχει την ημερομηνία της παραγγελίας, τη λίστα των αντικειμένων στο καλάθι αγορών και το συνολικό κόστος της παραγγελίας.

public class Order {
    private Date orderDate;
    private List<ShoppingCart.CartItem> items;
    private double totalCost;

    public Order(Date orderDate, List<ShoppingCart.CartItem> items, double totalCost) {
        this.orderDate = orderDate;
        this.items = items;
        this.totalCost = totalCost;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public List<ShoppingCart.CartItem> getItems() {
        return items;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
