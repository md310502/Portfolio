package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import java.util.List;
import uk.ac.abertay.cmp309.daniels_dinner_dash.R;
// Used In Order Menu
public class BasketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BasketAdapter basketAdapter;
    private Basket basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        // tied to reycler view
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        basket = new Basket();

        // Creates List
        List<MenuItem> basketItems = basket.getItems();

        basketAdapter = new BasketAdapter(basketItems);
        recyclerView.setAdapter(basketAdapter);

        // Calculate and display total cost
        double totalCost = basket.getTotalCost();
        TextView totalCostTextView = findViewById(R.id.total_cost_text_view);
        totalCostTextView.setText("Total: £" + totalCost);
    }


        private void orderItems() {

    }
}
