package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import java.util.ArrayList;
import java.util.List;

// used for order menu
public class Basket {
    private List<MenuItem> items;

    public Basket() {
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public double getTotalCost() {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public void clearBasket() {
        items.clear();
    }
}
