package api;

import java.util.ArrayList;

//Η κλάση Category αναπαριστά μια κατηγορία προϊόντων. Κάθε κατηγορία περιλαμβάνει ένα όνομα και μια λίστα από υποκατηγορίες.
public class Category {
    private String name;
    private ArrayList<Subcategory> subcategories;

    public Category(String name) {
        this.name = name;
        this.subcategories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addSubcategory(Subcategory subcategory) {
        this.subcategories.add(subcategory);
    }

    public ArrayList<Subcategory> getSubcategories() {
        return subcategories;
    }

    @Override
    public String toString() {
        return "Category: " + name + ", Subcategories: " + subcategories;
    }
}
