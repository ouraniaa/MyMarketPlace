package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class SubcategoryTest {

    private Subcategory subcategory;

    @Before
    public void setUp() {
        subcategory = new Subcategory("Φρούτα");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("Φρούτα", subcategory.getName());
        assertTrue(subcategory.getProducts().isEmpty());
    }

    @Test
    public void testAddProduct() {
        Product product1 = new Product("Μήλα", "Φρέσκα μήλα από την περιοχή μας", "Φρέσκα τρόφιμα", "Φρούτα", 2.50, "kg", 100);
        Product product2 = new Product("Πορτοκάλια", "Γλυκά πορτοκάλια για χυμό", "Φρέσκα τρόφιμα", "Φρούτα", 1.80, "kg", 50);

        subcategory.addProduct(product1);
        subcategory.addProduct(product2);

        ArrayList<Product> products = subcategory.getProducts();

        assertEquals(2, products.size());
        assertEquals(product1, products.get(0));
        assertEquals(product2, products.get(1));
    }

    @Test
    public void testToString() {
        Product product1 = new Product("Μήλα", "Φρέσκα μήλα από την περιοχή μας", "Φρέσκα τρόφιμα", "Φρούτα", 2.50, "kg", 100);
        Product product2 = new Product("Πορτοκάλια", "Γλυκά πορτοκάλια για χυμό", "Φρέσκα τρόφιμα", "Φρούτα", 1.80, "kg", 50);

        subcategory.addProduct(product1);
        subcategory.addProduct(product2);

        String expected = "Subcategory: Φρούτα, Products: 2 items";
        assertEquals(expected, subcategory.toString());
    }
}
