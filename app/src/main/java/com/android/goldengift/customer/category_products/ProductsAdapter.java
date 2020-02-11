package com.android.goldengift.customer.category_products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.goldengift.R;
import com.android.goldengift.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final CategoryProductsPresenter presenter;

    public ProductsAdapter(CategoryProductsPresenter presenter) {
        this.presenter = presenter;
    }

    public void bindProducts(ArrayList<Product> products) {
        presenter.bindProducts(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        presenter.onBindProductAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getProductsSize();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImageView;
        private TextView productNameTextView, unitPriceTextView, totalTextView, quantityTextView;
        private ImageButton increaseButton, decreaseButton;
        private Context context;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            productImageView = itemView.findViewById(R.id.item_image);
            productNameTextView = itemView.findViewById(R.id.item_name);
            unitPriceTextView = itemView.findViewById(R.id.unit_price_textview);
            totalTextView = itemView.findViewById(R.id.total);
            quantityTextView = itemView.findViewById(R.id.quantity_textview);
            increaseButton = itemView.findViewById(R.id.increase_button);
            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.increaseQuantity(ProductViewHolder.this, getAdapterPosition());
                }
            });
            decreaseButton = itemView.findViewById(R.id.decrease_button);
            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.decreaseQuantity(ProductViewHolder.this, getAdapterPosition());
                }
            });
        }

        public void setProductImage(String imageUrl) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.products)
                    .error(R.drawable.products)
                    .into(productImageView);
        }

        public void setProductName(String productName) {
            productNameTextView.setText(productName);
        }

        public void setUnitPrice(double unitPrice) {
            unitPriceTextView.setText(String.format("Unit Price: %.1f", unitPrice));
        }

        public void setProductItemTotalCost(double cost) {
            totalTextView.setText(String.format("Total: %.1f", cost));
        }

        public void setQuantity(int quantity) {
            quantityTextView.setText(String.valueOf(quantity));
        }
    }
}
