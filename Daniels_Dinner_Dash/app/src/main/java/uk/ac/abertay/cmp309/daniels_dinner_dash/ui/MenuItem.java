package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import java.io.Serializable;
// used for order screen
public class MenuItem implements Serializable {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
