package com.example.minhhuynh.productsproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minhhuynh.productsproject.R;
import com.example.minhhuynh.productsproject.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PhonesAdapter extends BaseAdapter {

    ArrayList<Product> phoneArray;
    Context context;

    public PhonesAdapter(ArrayList<Product> phoneArray, Context context) {
        this.phoneArray = phoneArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return phoneArray.size();
    }

    @Override
    public Object getItem(int i) {
        return phoneArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_phone_row,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Product product = (Product) getItem(i);
        viewHolder.textViewNamePhone.setText(product.getName());
        viewHolder.textViewPricePhone.setText("$" + product.getPrice());
        viewHolder.textViewDesPhone.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.textViewDesPhone.setText(product.getDescription());
        Picasso.with(context).load(product.getImage())
                .placeholder(android.R.drawable.ic_dialog_alert)
                .error(android.R.drawable.ic_input_delete)
                .into(viewHolder.imageViewPhone);
        return view;
    }

    public class ViewHolder {
        public ImageView imageViewPhone;
        public TextView textViewNamePhone;
        public TextView textViewPricePhone;
        public TextView textViewDesPhone;

        public ViewHolder(@NonNull View itemView) {
            imageViewPhone = itemView.findViewById(R.id.imageViewPhone);
            textViewNamePhone = itemView.findViewById(R.id.textViewNamePhone);
            textViewPricePhone = itemView.findViewById(R.id.textViewPricePhone);
            textViewDesPhone = itemView.findViewById(R.id.textViewDesPhone);

        }
    }

}
