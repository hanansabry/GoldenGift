package com.android.goldengift.store.orders.order_invoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.goldengift.R;
import com.android.goldengift.model.OrderItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {

    private final OrderInvoicePresenter presenter;

    public OrderItemsAdapter(OrderInvoicePresenter presenter) {
        this.presenter = presenter;
    }

    public void bindOrderItems(ArrayList<OrderItem> orderItems) {
        presenter.bindOrderItems(orderItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_invoice_item_layout, parent, false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        presenter.onBindOrderItemAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getOrderItemsSize();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {

        private TextView itemNameTextView, quantityTextView, costTextView;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.item_name);
            quantityTextView = itemView.findViewById(R.id.quantity);
            costTextView = itemView.findViewById(R.id.item_cost);
        }

        public void setOrderItemData(OrderItem orderItem) {
            itemNameTextView.setText(orderItem.getItemName());
            quantityTextView.setText(String.format("Quantity : %d", orderItem.getQuantity()));
            costTextView.setText(String.valueOf(orderItem.getCost()));
        }
    }

}
