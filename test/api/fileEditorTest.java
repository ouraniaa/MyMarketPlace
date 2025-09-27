package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

public class fileEditorTest {

    private static final String TEST_FILE_PATH = "testFile.txt";

    @Before
    public void setUp() throws IOException {
        // Set up a test file with predefined data
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE_PATH));
        writer.write("Κατηγορία1 (υποκατηγορία1@υποκατηγορία2)\n");
        writer.write("Τίτλος: Προϊόν 1\nΠεριγραφή: Περιγραφή προϊόντος 1\nΚατηγορία: Κατηγορία1\nΥποκατηγορία: υποκατηγορία1\nΤιμή: 10.00€\nΠοσότητα: 5 τεμάχια\n");
        writer.write("Τίτλος: Προϊόν 2\nΠεριγραφή: Περιγραφή προϊόντος 2\nΚατηγορία: Κατηγορία1\nΥποκατηγορία: υποκατηγορία2\nΤιμή: 20.00€\nΠοσότητα: 10 kg\n");
        writer.close();
    }

    @Test
    public void testLoadCategoriesFromFile() {
        fileEditor.loadCategoriesFromFile(TEST_FILE_PATH);

        // Test if categories are loaded correctly
        assertEquals(1, fileEditor.getCategories().size()); // Should contain 1 category
        Category category = fileEditor.getCategories().get(0);
        assertEquals("Κατηγορία1", category.getName());
        assertEquals(2, category.getSubcategories().size()); // Should contain 2 subcategories
    }

    @Test
    public void testLoadProductsFromFile() {
        fileEditor.loadProductsFromFile(TEST_FILE_PATH);

        // Test if products are loaded correctly
        Category category = fileEditor.getCategories().get(0);
        Subcategory subcategory1 = category.getSubcategories().get(0);
        Subcategory subcategory2 = category.getSubcategories().get(1);

        assertEquals(4, subcategory1.getProducts().size()); // 1 product in subcategory1
        assertEquals(4, subcategory2.getProducts().size()); // 1 product in subcategory2

        Product product1 = subcategory1.getProducts().get(0);
        assertEquals("Προϊόν 1", product1.getTitle());
        assertEquals(10.00, product1.getPrice(), 0.01);
    }

    @Test
    public void testCreateFile() throws IOException {
        ArrayList<String> data = new ArrayList<>();
        data.add("Data 1");
        data.add("Data 2");

        fileEditor.createFile(TEST_FILE_PATH, data, "Διαχειριστής", "registration");

        // Verify that file is created and has the expected contents
        BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_PATH));
        String line = reader.readLine();
        assertEquals("Data 1", line);
        line = reader.readLine();
        assertEquals("Data 2", line);
        line = reader.readLine();
        assertEquals("Διαχειριστής", line);
        reader.close();
    }

    @Test
    public void testReadFile() throws IOException {
        fileEditor.createFile(TEST_FILE_PATH, new ArrayList<>(List.of("Test data")), "Πελάτης", "registration");

        String line = fileEditor.read(TEST_FILE_PATH, 1); // Read the first line
        assertEquals("Test data", line);
    }

    @Test
    public void testSearchProductFromFile() {
        fileEditor.loadProductsFromFile(TEST_FILE_PATH);

        String result = fileEditor.searchProductFromFile(TEST_FILE_PATH, "Προϊόν 1");
        assertTrue(result.contains("Τίτλος: Προϊόν 1"));
        assertTrue(result.contains("Περιγραφή: Περιγραφή προϊόντος 1"));
    }

    @Test
    public void testUpdateProductInFile() throws IOException {
        fileEditor.loadProductsFromFile(TEST_FILE_PATH);

        // Update product stock
        Map<String, String> updatedFields = new HashMap<>();
        updatedFields.put("Ποσότητα", "15 τεμάχια");
        fileEditor.updateProductInFile(TEST_FILE_PATH, "Προϊόν 1", updatedFields);

        // Verify the updated stock
        String updatedProduct = fileEditor.searchProductFromFile(TEST_FILE_PATH, "Προϊόν 1");
        assertTrue(updatedProduct.contains("Ποσότητα: 15 τεμάχια"));
    }

    @Test
    public void testUpdateProductTitle() throws IOException {
        fileEditor.loadProductsFromFile(TEST_FILE_PATH);

        fileEditor.updateProductTitle(TEST_FILE_PATH, "Προϊόν 1", "Προϊόν Νέο");

        String updatedProduct = fileEditor.searchProductFromFile(TEST_FILE_PATH, "Προϊόν Νέο");
        assertTrue(updatedProduct.contains("Προϊόν Νέο"));
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateProductStockWithInsufficientQuantity() throws IOException {
        List<ShoppingCart.CartItem> purchasedItems = new ArrayList<>();
        purchasedItems.add(new ShoppingCart.CartItem("Προϊόν 1", 10.0, 6)); // Provide the price per unit as well

        fileEditor.updateProductStock(TEST_FILE_PATH, purchasedItems);
    }

    @Test
    public void testWriteProductToFile() throws IOException {
        fileEditor.writeProductToFile(TEST_FILE_PATH, "Προϊόν 3", "Περιγραφή 3", "Κατηγορία1", "υποκατηγορία1", "30.00", "10", "τεμάχια");

        String result = fileEditor.searchProductFromFile(TEST_FILE_PATH, "Προϊόν 3");
        assertTrue(result.contains("Προϊόν 3"));
        assertTrue(result.contains("Περιγραφή 3"));
    }

    @Test
    public void testGetProductUnit() throws FileNotFoundException {
        String unit = fileEditor.getProductUnit(TEST_FILE_PATH, "Προϊόν 1");
        assertEquals("τεμάχια", unit);
    }
}
