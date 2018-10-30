package com.example.minhhuynh.productsproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minhhuynh.productsproject.R;
import com.example.minhhuynh.productsproject.model.ProductType;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProductTypeAdapter extends BaseAdapter {

    ArrayList<ProductType> productTypeArrayList;
    Context context;

    public ProductTypeAdapter(ArrayList<ProductType> productTypeArrayList, Context context) {
        this.productTypeArrayList = productTypeArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return productTypeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return productTypeArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_type_row,null);
            viewHolder.imageViewHolder = view.findViewById(R.id.imageViewInflater);
            viewHolder.textViewHolder = view.findViewById(R.id.textViewInflater);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ImageView imageViewInflater = viewHolder.imageViewHolder;
        TextView textViewInflater = viewHolder.textViewHolder;
        ProductType productType = (ProductType) getItem(i);
        textViewInflater.setText(productType.getName());
        Picasso.with(context).load(productType.getImage())
                .placeholder(android.R.drawable.ic_dialog_alert)
                .error(android.R.drawable.ic_delete)
                .into(imageViewInflater);
        return view;
    }

    private class ViewHolder {
        ImageView imageViewHolder;
        TextView textViewHolder;
    }
}
