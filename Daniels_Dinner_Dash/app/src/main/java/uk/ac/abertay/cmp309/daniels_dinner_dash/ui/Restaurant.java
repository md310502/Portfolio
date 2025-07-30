package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import java.io.Serializable;
import java.util.List;
 // used in order screen
public class Restaurant implements Serializable {
    private String name;
    private List<MenuItem> menuItems;

    public Restaurant(String name, List<MenuItem> menuItems) {
        this.name = name;
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}

