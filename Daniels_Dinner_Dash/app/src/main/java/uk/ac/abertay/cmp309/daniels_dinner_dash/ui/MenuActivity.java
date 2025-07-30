package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uk.ac.abertay.cmp309.daniels_dinner_dash.R;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private Basket basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
       // used in order screen
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        basket = new Basket();

        // Get the passed restaurant data
        Restaurant restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
        if (restaurant != null) {
            menuItems = restaurant.getMenuItems();
        }

        menuAdapter = new MenuAdapter(menuItems, new MenuAdapter.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(MenuItem menuItem) {
                // Add item to basket
                basket.addItem(menuItem);
                Toast.makeText(MenuActivity.this, menuItem.getName() + " added to basket", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(menuAdapter);
        // used in order screen
        Button viewBasketButton = findViewById(R.id.view_basket_button);
        viewBasketButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, BasketActivity.class);
            startActivity(intent);
        });
    }
}
