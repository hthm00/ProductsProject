package com.example.minhhuynh.productsproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minhhuynh.productsproject.R;
import com.example.minhhuynh.productsproject.model.Product;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ItemHolder> {

    ArrayList<Product> productsArray;
    Context context;

    public ProductsAdapter(ArrayList<Product> productsArray, Context context) {
        this.productsArray = productsArray;
        this.context = context;
    }

    @NonNull
    @Override

    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_homepage_item, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        Product product = productsArray.get(i);
        itemHolder.textViewName.setText(product.getName());
        itemHolder.textViewPrice.setText("$" + product.getPrice());

        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(product.getImage())
                .placeholder(android.R.drawable.ic_dialog_alert)
                .error(android.R.drawable.ic_delete)
                .into(itemHolder.imageViewHome);

    }

    @Override
    public int getItemCount() {
        return productsArray.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewHome;
        public TextView textViewPrice;
        public TextView textViewName;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imageViewHome = itemView.findViewById(R.id.imageViewProduct);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewName = itemView.findViewById(R.id.textViewName);

        }
    }

}
