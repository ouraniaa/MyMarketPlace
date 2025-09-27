package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import api.Product;

public class ProductTest {

    private Product productWithPieces;
    private Product productWithKg;

    @Before
    public void setUp() {
        productWithKg = new Product("Πορτοκάλια 1kg", "Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση.", "Φρέσκα τρόφιμα", "Φρούτα", 1.20, "kg", 198.0);
        productWithPieces = new Product("Γάλα Πλήρες 1lt", "Πλήρες γάλα με πλούσια γεύση και θρεπτικά συστατικά.", "Προϊόντα ψυγείου", "Γάλα", 1.30, "τεμάχια", 250);
    }

    @Test
    public void testConstructorAndGetters() {
        // Testing product with "τεμάχια"
        assertEquals("Πορτοκάλια 1kg", productWithKg.getTitle());
        assertEquals("Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση.", productWithKg.getDescription());
        assertEquals("Φρέσκα τρόφιμα", productWithKg.getCategory());
        assertEquals("Φρούτα", productWithKg.getSubcategory());
        assertEquals(1.20, productWithKg.getPrice(), 0.0001);
        assertEquals("kg", productWithKg.getUnitType());
        assertEquals(198.0, productWithKg.getStock());

        // Testing product with "kg"
        assertEquals("Γάλα Πλήρες 1lt", productWithPieces.getTitle());
        assertEquals("Πλήρες γάλα με πλούσια γεύση και θρεπτικά συστατικά.", productWithPieces.getDescription());
        assertEquals("Προϊόντα ψυγείου", productWithPieces.getCategory());
        assertEquals("Γάλα", productWithPieces.getSubcategory());
        assertEquals(1.30, productWithPieces.getPrice(), 0.0001);
        assertEquals("τεμάχια", productWithPieces.getUnitType());
        assertEquals(250, productWithPieces.getStock());
    }

    @Test
    public void testSetters() {
        productWithPieces.setTitle("Κιμάς Μοσχαρίσιος 500g");
        productWithPieces.setDescription("Φρέσκος κιμάς μοσχαρίσιος από τοπικό κρεοπωλείο.");
        productWithPieces.setCategory("Φρέσκα τρόφιμα");
        productWithPieces.setSubcategory("Κρέατα");
        productWithPieces.setPrice(6.50);
        productWithPieces.setUnitType("τεμάχια");
        productWithPieces.setStock(100);

        assertEquals("Κιμάς Μοσχαρίσιος 500g", productWithPieces.getTitle());
        assertEquals("Φρέσκος κιμάς μοσχαρίσιος από τοπικό κρεοπωλείο.", productWithPieces.getDescription());
        assertEquals("Φρέσκα τρόφιμα", productWithPieces.getCategory());
        assertEquals("Κρέατα", productWithPieces.getSubcategory());
        assertEquals(6.50, productWithPieces.getPrice(), 0.0001);
        assertEquals("τεμάχια", productWithPieces.getUnitType());
        assertEquals(100, productWithPieces.getStock());
    }

    @Test
    public void testUpdateStockForPieces() {
        productWithPieces.updateStock(3);
        assertEquals(247, productWithPieces.getStock());

        productWithPieces.updateStock(7);
        assertEquals(240, productWithPieces.getStock());

    }

    @Test
    public void testUpdateStockForKg() {
        productWithKg.updateStock(10.5);
        assertEquals(187.5, productWithKg.getStock());

        productWithKg.updateStock(40);
        assertEquals(147.5, productWithKg.getStock());

    }

    @Test
    public void testInvalidUnitType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Product("Test", "Invalid", "TestCat", "TestSub", 10.0, "invalidUnit", 5));
        assertEquals("Ακατάλληλη μονάδα για stock.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            productWithPieces.setUnitType("invalidUnit");
            productWithPieces.setStock(5);
        });
        assertEquals("Ακατάλληλη μονάδα για stock.", exception.getMessage());
    }

    @Test
    public void testToString() {
        String expectedPieces = "Πορτοκάλια 1kg - Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση. - Φρέσκα τρόφιμα / Φρούτα: 1.2€ (198.0 διαθέσιμα kg)";
        String expectedKg = "Γάλα Πλήρες 1lt - Πλήρες γάλα με πλούσια γεύση και θρεπτικά συστατικά. - Προϊόντα ψυγείου / Γάλα: 1.3€ (250 διαθέσιμα τεμάχια)";

        assertEquals(expectedPieces, productWithKg.toString());
        assertEquals(expectedKg, productWithPieces.toString());
    }
}