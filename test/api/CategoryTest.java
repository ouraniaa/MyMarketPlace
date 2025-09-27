package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;

public class CategoryTest {

    private Category category;

    @Before
    public void setUp() {
        category = new Category("Φρέσκα τρόφιμα");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("Φρέσκα τρόφιμα", category.getName());
        assertTrue(category.getSubcategories().isEmpty());
    }

    @Test
    public void testAddSubcategory() {
        Subcategory subcategory1 = new Subcategory("Φρούτα");
        Subcategory subcategory2 = new Subcategory("Λαχανικά");

        category.addSubcategory(subcategory1);
        category.addSubcategory(subcategory2);

        ArrayList<Subcategory> subcategories = category.getSubcategories();

        assertEquals(2, subcategories.size());
        assertEquals(subcategory1, subcategories.get(0));
        assertEquals(subcategory2, subcategories.get(1));
    }

    @Test
    public void testToString() {
        Subcategory subcategory1 = new Subcategory("Φρούτα");
        Subcategory subcategory2 = new Subcategory("Λαχανικά");

        category.addSubcategory(subcategory1);
        category.addSubcategory(subcategory2);

        String expected = "Category: Φρέσκα τρόφιμα, Subcategories: [Subcategory: Φρούτα, Products: 0 items, Subcategory: Λαχανικά, Products: 0 items]";
        assertEquals(expected, category.toString());
    }
}
