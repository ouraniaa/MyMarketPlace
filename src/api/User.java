package api;

import java.util.ArrayList;

public class User {
    /**
     * Χαρακτηριστικά χρήστη.
     */
    private String name;
    private String surname;
    private String username;
    private String password;
    private boolean manager;
    private boolean customer;
    private ArrayList<String> stoixeia = new ArrayList<String>();
    public User(String name,String surname,String username,String password){
        this.name=name;
        this.surname=surname;
        this.username=username;
        this.password=password;

        stoixeia.add(name);
        stoixeia.add(surname);
        stoixeia.add(username);
        stoixeia.add(password);

    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getStoixeia() {
        return stoixeia;
    }

    public void setCustomer(boolean customer) {
        this.customer = customer;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }
}
