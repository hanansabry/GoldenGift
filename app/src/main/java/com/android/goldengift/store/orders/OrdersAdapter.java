package com.android.goldengift.store.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.goldengift.R;
import com.android.goldengift.model.Order;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final OrdersPresenter presenter;
    private final OrdersPresenter.OrderCallback orderCallback;

    public OrdersAdapter(OrdersPresenter presenter, OrdersPresenter.OrderCallback orderCallback) {
        this.presenter = presenter;
        this.orderCallback = orderCallback;
    }

    public void bindOrders(ArrayList<Order> orders) {
        presenter.bindOrders(orders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_layout, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        presenter.onBindOrderAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getOrdersSize();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{

        private TextView orderNumberTextView, dateTextView, statusTextView;
        private ImageButton moreImageButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            orderNumberTextView = itemView.findViewById(R.id.order_number_textview);
            dateTextView = itemView.findViewById(R.id.date_textview);
            statusTextView = itemView.findViewById(R.id.order_status_textview);
            moreImageButton = itemView.findViewById(R.id.more_button);

            moreImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderCallback.onOrderClicked();
                }
            });
        }

        public void setOrderData(Order order) {
            orderNumberTextView.setText(String.format("Order Number: %d", order.getOrderNumber()));
            dateTextView.setText(order.getDate());
            statusTextView.setText(order.getStatus());
        }
    }
}
