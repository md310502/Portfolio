package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uk.ac.abertay.cmp309.daniels_dinner_dash.R;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private List<MenuItem> basketItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemPrice;

        public ViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.item_name);
            itemPrice = view.findViewById(R.id.item_price);
        }
    }
     // Allows items to be placed in the basket
    public BasketAdapter(List<MenuItem> basketItems) {
        this.basketItems = basketItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_basket_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = basketItems.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return basketItems.size();
    }
}
