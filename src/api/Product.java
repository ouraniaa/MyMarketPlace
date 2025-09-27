package api;

//Η κλάση Product αναπαριστά ένα προϊόν στο σύστημα. Περιλαμβάνει πληροφορίες όπως τίτλο, περιγραφή, κατηγορία, υποκατηγορία, τιμή, μονάδα μέτρησης, και stock.

public class Product {
    private String title;
    private String description;
    private String category;
    private String subcategory;
    private double price;
    private String unitType;
    private Object stock;  // Χρησιμοποιούμε Object για να έχουμε και int και double τύπους για το stock

    public Product(String title, String description, String category, String subcategory, double price, String unitType, double stock) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
        this.unitType = unitType;
        setStock(stock);  // Καθορίζουμε το stock με βάση την μονάδα
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Object getStock() {
        return stock;
    }

    public void setStock(double stock) {
        if ("τεμάχια".equalsIgnoreCase(unitType)) {
            this.stock = (int) stock;  // Εάν η μονάδα είναι "τεμάχια", το stock είναι int
        } else if ("kg".equalsIgnoreCase(unitType)) {
            this.stock = stock;  // Εάν η μονάδα είναι "kg", το stock είναι double
        } else {
            throw new IllegalArgumentException("Ακατάλληλη μονάδα για stock.");
        }
    }

    public void updateStock(double quantity) {
        if (unitType.equals("τεμάχια")) {
            int currentStock = (int) stock;
            if (currentStock - quantity < 0) {
                throw new IllegalArgumentException("Δεν υπάρχει αρκετό stock.");
            }
            stock = currentStock - (int) quantity;
        } else if (unitType.equals("kg")) {
            double currentStock = (double) stock;
            if (currentStock - quantity < 0) {
                throw new IllegalArgumentException("Δεν υπάρχει αρκετό stock.");
            }
            stock = currentStock - quantity;
        } else {
            throw new IllegalArgumentException("Ακατάλληλη μονάδα για ενημέρωση του stock.");
        }
    }

    @Override
    public String toString() {
        return title + " - "
                + description + " - "
                + category + " / "
                + subcategory + ": "
                + price + "€ (" + stock + " διαθέσιμα " + unitType + ")";
    }
}
