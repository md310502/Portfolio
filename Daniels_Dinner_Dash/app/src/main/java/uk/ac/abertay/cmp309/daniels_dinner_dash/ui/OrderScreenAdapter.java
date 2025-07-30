package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.abertay.cmp309.daniels_dinner_dash.R;

public class OrderScreenAdapter extends RecyclerView.Adapter<OrderScreenAdapter.OrderViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(OrderItem item);
    }

    private List<OrderItem> orderItems;
    private OnItemClickListener listener;

    public OrderScreenAdapter(List<OrderItem> orderItems, OnItemClickListener listener) {
        this.orderItems = orderItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_view, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orderItems.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }
          // class is used to manage order activity key items
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
        }

        public void bind(final OrderItem item, final OnItemClickListener listener) {
            itemName.setText(item.getName());
            itemPrice.setText(String.valueOf(item.getPrice()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}



