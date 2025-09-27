package api;

import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedWriter;
import java.nio.file.StandardOpenOption;


public class fileEditor {
    // Λίστα όλων των κατηγοριών που φορτώνονται από τα αρχεία
    private static ArrayList<Category> categories = new ArrayList<>();

    private fileEditor() {
        throw new UnsupportedOperationException("Cannot instantiate FileEditor");
    }

    public static void loadCategoriesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Χωρίζουμε τη γραμμή σε κατηγορία και υποκατηγορίες
                String[] parts = line.split("\\s*\\(\\s*"); // Χωρίζουμε πριν από την πρώτη παρενθέση

                if (parts.length < 2) continue; // Αν η γραμμή δεν περιέχει κατηγορία και υποκατηγορίες, την αγνοούμε

                String categoryName = parts[0].trim(); // Το όνομα της κατηγορίας
                String subcategoriesPart = parts[1].replace(")", ""); // Αφαιρούμε την τελική παρενθέση
                String[] subcategories = subcategoriesPart.split("@"); // Χωρίζουμε τις υποκατηγορίες με βάση το '@'

                // Δημιουργούμε ή βρίσκουμε την κατηγορία
                Category category = findOrCreateCategory(categoryName);

                // Προσθέτουμε τις υποκατηγορίες
                for (String subcategoryName : subcategories) {
                    findOrCreateSubcategory(category, subcategoryName.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου!" + e.getMessage());
        }
    }


    private static Category findOrCreateCategory(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        Category newCategory = new Category(categoryName);
        categories.add(newCategory);
        return newCategory;
    }

    private static Subcategory findOrCreateSubcategory(Category category, String subcategoryName) {
        for (Subcategory subcategory : category.getSubcategories()) {
            if (subcategory.getName().equalsIgnoreCase(subcategoryName)) {
                return subcategory;
            }
        }
        Subcategory newSubcategory = new Subcategory(subcategoryName);
        category.addSubcategory(newSubcategory);
        return newSubcategory;
    }

    public static void loadProductsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String title = null, description = null, category = null, subcategory = null, unitType = null;
            double price = 0, stock = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Αγνοούμε τις κενές γραμμές

                if (line.startsWith("Τίτλος:")) {
                    if (title != null) {
                        // Δημιουργία και προσθήκη προϊόντος στην κατηγορία
                        Product product = new Product(title, description, category, subcategory, price, unitType, stock);
                        addProductToCategory(product);
                    }
                    title = line.substring(7).trim();
                    description = null;
                    category = null;
                    subcategory = null;
                    unitType = null;
                    price = 0;
                    stock = 0;
                } else if (line.startsWith("Περιγραφή:")) {
                    description = line.substring(11).trim();
                } else if (line.startsWith("Κατηγορία:")) {
                    category = line.substring(10).trim();
                } else if (line.startsWith("Υποκατηγορία:")) {
                    subcategory = line.substring(13).trim();
                } else if (line.startsWith("Τιμή:")) {
                    String priceString = line.substring(5).trim();
                    price = parsePrice(priceString);
                } else if (line.startsWith("Ποσότητα:")) {
                    String quantity = line.substring(9).trim();
                    if (quantity.endsWith("kg")) {
                        unitType = "kg";
                        stock = Double.parseDouble(quantity.replace("kg", "").trim());
                    } else if (quantity.endsWith("τεμάχια")) {
                        unitType = "τεμάχια";
                        stock = Double.parseDouble(quantity.replace("τεμάχια", "").trim());
                    }
                }
            }

            // Προσθέτουμε το τελευταίο προϊόν αν υπάρχει
            if (title != null) {
                Product product = new Product(title, description, category, subcategory, price, unitType, stock);
                addProductToCategory(product);
            }
        }catch (IOException e) {
            System.err.println("Error while trying to read from file: " + e.getMessage());
        }
    }

    private static double parsePrice(String priceString) {
        // Αφαιρεί το σύμβολο € και κόβει τα κενά
        priceString = priceString.replace("€", "").trim();
        // Αντικαθιστά το κόμμα με τελεία για να γίνει έγκυρο double
        priceString = priceString.replace(",", ".");
        // Μετατροπή σε double
        return Double.parseDouble(priceString);
    }
    private static void addProductToCategory(Product product) {
        Category category = findOrCreateCategory(product.getCategory());
        Subcategory subcategory = findOrCreateSubcategory(category, product.getSubcategory());
        subcategory.addProduct(product);
    }

    public static void createFile(String strFile, ArrayList<String> strData, String role, String fileType) {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(strFile))) {
            switch (fileType.toLowerCase()) {
                case "registration":
                    for (String data : strData) {
                        buffer.write(data);
                        buffer.newLine();
                    }
                    if (role.equals("Διαχειριστής")) {
                        buffer.write("Διαχειριστής");
                    } else if (role.equals("Πελάτης")) {
                        buffer.write("Πελάτης");
                    }
                    break;

                case "completedorders":
                    for (String order : strData) {
                        buffer.write(order);
                        buffer.newLine();
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Μη υποστηριζόμενος τύπος αρχείου: " + fileType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Σφάλμα κατά τη δημιουργία του αρχείου: " + e.getMessage());
        }
    }

    // Επιστρέφει το περιεχόμενο μιας συγκεκριμένης γραμμής από ένα αρχείο
    public static String read(String path, Integer lineWanted) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        File file=new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        try (LineNumberReader rdr = new LineNumberReader(new FileReader(file))) {
            for (String line = null; (line = rdr.readLine()) != null; ) {
                if (rdr.getLineNumber() == lineWanted) {
                    sb.append(line);
                }
            }
            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String contents = String.valueOf(sb);

        return contents;
    }


    public static void writeProductToFile(String path, String title, String description, String category,
                                          String subcategory, String price, String quantity, String unitType) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.newLine();
            writer.write("Τίτλος: " + title);
            writer.newLine();
            writer.write("Περιγραφή: " + description);
            writer.newLine();
            writer.write("Κατηγορία: " + category);
            writer.newLine();
            writer.write("Υποκατηγορία: " + subcategory);
            writer.newLine();
            double priceValue = Double.parseDouble(price);
            writer.write("Τιμή: " + String.format("%.2f€", priceValue));
            writer.newLine();
            writer.write("Ποσότητα: " + quantity + unitType);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την εγγραφή στο αρχείο: " + e.getMessage());
        }
    }

    public static String searchProductFromFile(String path, String title) {
        StringBuilder productDetails = new StringBuilder();
        boolean productFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Τίτλος: ") && line.substring(8).equalsIgnoreCase(title)) {
                    productFound = true;
                    productDetails.append(line).append("\n");
                    for (int i = 0; i < 5; i++) {
                        line = reader.readLine();
                        if (line != null) {
                            productDetails.append(line).append("\n");
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
        }

        if (productFound) {
            return productDetails.toString();
        } else {
            return "Το προϊόν με τίτλο \"" + title + "\" δεν βρέθηκε.";
        }
    }

    public static String getProductDetailByTitle(String path, String title, String detailType) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Τίτλος: ") && line.substring(8).equalsIgnoreCase(title)) {
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith(detailType + ": ")) {
                            // Επιστρέφουμε το ζητούμενο χαρακτηριστικό (αφαιρώντας την ετικέτα π.χ. "Κατηγορία: ")
                            return line.substring(detailType.length() + 2).trim();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
        }
        return "Το χαρακτηριστικό δεν βρέθηκε ή το προϊόν δεν υπάρχει.";
    }

    public static void updateProductInFile(String filePath, String title, Map<String, String> updatedFields) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<String> updatedLines = new ArrayList<>();
        boolean productFound = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("Τίτλος:") && line.contains(title)) {
                productFound = true;
                updatedLines.add(line); // Διατήρηση της γραμμής τίτλου
                // Ενημέρωση των επόμενων γραμμών με βάση τα updatedFields
                for (int j = i + 1; j < lines.size(); j++) {
                    String nextLine = lines.get(j);
                    if (nextLine.trim().isEmpty()) {
                        updatedLines.add(nextLine);
                        i = j;
                        break;
                    }
                    String[] keyValue = nextLine.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        if (updatedFields.containsKey(key)) {
                            updatedLines.add(key + ": " + updatedFields.get(key));
                        } else {
                            updatedLines.add(nextLine);
                        }
                    } else {
                        updatedLines.add(nextLine);
                    }
                }
            } else {
                updatedLines.add(line);
            }
        }

        if (!productFound) {
            throw new FileNotFoundException("Το προϊόν με τον τίτλο '" + title + "' δεν βρέθηκε.");
        }

        Files.write(Paths.get(filePath), updatedLines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }


    public static void updateProductTitle(String filePath, String oldTitle, String newTitle) throws IOException {
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Τίτλος: ") && line.substring(8).equalsIgnoreCase(oldTitle)) {
                line = "Τίτλος: " + newTitle; // Αλλαγή τίτλου
            }
            content.append(line).append(System.lineSeparator());
        }
        reader.close();

        // Γράφουμε το ενημερωμένο περιεχόμενο στο αρχείο
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content.toString());
        writer.close();
    }


    public static String getProductUnit(String filePath, String productName) throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ελέγχουμε αν η γραμμή είναι ο τίτλος του προϊόντος
                if (line.startsWith("Τίτλος: ") && line.substring(8).equalsIgnoreCase(productName)) {
                    // Διαβάζουμε τις υπόλοιπες γραμμές για να βρούμε την ποσότητα του προϊόντος
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Ποσότητα: ")) {
                            // Διαβάζουμε τη γραμμή της ποσότητας και κόβουμε το "Ποσότητα: "
                            String quantityInfo = line.substring(11).trim();
                            // Αναγνωρίζουμε τη μονάδα μέτρησης με βάση το τελευταίο μέρος της ποσότητας
                            String unit = "";
                            if (quantityInfo.endsWith("kg")) {
                                unit = "kg";
                            } else if (quantityInfo.endsWith("τεμάχια")) {
                                unit = "τεμάχια";
                            }

                            // Ελέγχουμε αν η μονάδα μέτρησης βρέθηκε
                            if (!unit.isEmpty()) {
                                return unit;
                            } else {
                                System.out.println("Η μονάδα μέτρησης δεν βρέθηκε!");  // Debugging statement
                                return "";  // Επιστρέφουμε κενό αν δεν υπάρχει μονάδα μέτρησης
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
        }

        return "Δεν βρέθηκε η μονάδα μέτρησης.";
    }

    public static void updateProductStock(String filePath, List<ShoppingCart.CartItem> purchasedItems) {
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                boolean productUpdated = false;
                for (ShoppingCart.CartItem item : purchasedItems) {
                    if (line.startsWith("Τίτλος: " + item.getProductName())) {
                        // Διατήρηση αρχικών γραμμών
                        lines.add(line); // Τίτλος
                        lines.add(reader.readLine()); // Κατηγορία
                        lines.add(reader.readLine()); // Υποκατηγορία
                        lines.add(reader.readLine()); // Περιγραφή
                        lines.add(reader.readLine()); // Τιμή

                        // Ενημέρωση ποσότητας
                        String quantityLine = reader.readLine();
                        String[] parts = quantityLine.split(" ");
                        String stockString = parts[1]; // Παίρνουμε τη διαθέσιμη ποσότητα
                        String unit = parts.length > 2 ? parts[2] : ""; // Παίρνουμε τη μονάδα μέτρησης (π.χ., "τεμάχια" ή "kg")
                        double currentStock = Double.parseDouble(
                                parts[1].replaceAll("[^0-9.,]", "").replace(",", ".").trim()
                        );

                        double newStock = currentStock - item.getQuantity();

                        if (newStock < 0) {
                            throw new IllegalStateException("Το απόθεμα του προϊόντος " + item.getProductName() + " δεν επαρκεί.");
                        }

                        // Αν η μονάδα μέτρησης είναι τεμάχια, διατηρήστε ακέραιο αριθμό
                        if (unit.equalsIgnoreCase("τεμάχια")) {
                            lines.add("Ποσότητα: " + (int) newStock + " " + unit);
                        } else {
                            lines.add("Ποσότητα: " + newStock + "kg");
                        }

                        productUpdated = true;
                        break;
                    }
                }
                if (!productUpdated) {
                    lines.add(line); // Κράτησε τη γραμμή αν δεν ανήκει σε προϊόν που αγοράστηκε
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Εγγραφή των ενημερωμένων δεδομένων πίσω στο αρχείο
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Category> getCategories() {
        return categories;
    }

}
