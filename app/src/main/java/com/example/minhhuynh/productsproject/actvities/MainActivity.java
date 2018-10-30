package com.example.minhhuynh.productsproject.actvities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.minhhuynh.productsproject.R;
import com.example.minhhuynh.productsproject.adapter.PhonesAdapter;
import com.example.minhhuynh.productsproject.adapter.ProductTypeAdapter;
import com.example.minhhuynh.productsproject.adapter.ProductsAdapter;
import com.example.minhhuynh.productsproject.model.Product;
import com.example.minhhuynh.productsproject.model.ProductType;
import com.example.minhhuynh.productsproject.util.Connection;
import com.example.minhhuynh.productsproject.util.Server;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbarHome;
    ViewFlipper viewFlipper;
    DrawerLayout drawerLayout;
    RecyclerView recyclerViewHome;
    NavigationView navigationView;
    ListView listViewMenu;
    ProductTypeAdapter productTypeAdapter;
    ArrayList<ProductType> productTypeArrayList;
    ArrayList<Product> productArrayList;
    ProductsAdapter productsAdapter;


    Boolean DEBUG_MODE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DEBUG_MODE = true;

        mapped();

        if (!Connection.haveNetworkConnection(this)){
            Connection.showError(this);
            finish();
        }
        if (DEBUG_MODE) setUpPicasso();
        viewFlipper();
        actionBar();
        loadProductTypeFromServer();
        loadProductsToHome();
        listViewMenuListener();

// https://image.ibb.co/eUqi80/ssgalaxyj7.jpg

    }



    private void listViewMenuListener() {
        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intentPhone = new Intent(MainActivity.this, PhoneActivity.class);
                        startActivity(intentPhone);
                        break;

                }
            }
        });
    }

    private void setUpPicasso() {
        try {
            Picasso picasso = new Picasso.Builder(this).loggingEnabled(true).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                    Log.d("Picasso",exception.getMessage());
                }
            }).build();
            Picasso.setSingletonInstance(picasso);
        } catch (Exception e){
            Log.d("ERROR", e.toString());
        }
    }

    private void loadProductsToHome() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.productsServer + "?page=1", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Product product = new Product(jsonObject.getInt("id"), jsonObject.getString("name")
                                    , jsonObject.getString("price"), jsonObject.getString("image")
                                    , jsonObject.getString("description"), jsonObject.getString("productid"));
                            productArrayList.add(product);
                            productsAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (DEBUG_MODE) Log.d("MainActivity", "Products array list: " + productsAdapter.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Connection.showError(getApplicationContext());
                if (DEBUG_MODE) Log.d("ERROR", error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
        if (DEBUG_MODE) Log.d("MainActivity", "loadProductToHome() completed");
    }

    private void loadProductTypeFromServer() {
        productTypeArrayList.add(new ProductType(0, "Home", "https://image.ibb.co/ebLzkq/home-icon.png"));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.productTypeServer, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (DEBUG_MODE) Log.d("AAA", response.toString());
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            if (jsonObject != null) {
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String image = jsonObject.getString("image");
                                ProductType productType = new ProductType(id, name, image);
                                productTypeArrayList.add(i+1,productType);
                                productTypeAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Connection.showError(getApplicationContext());
                if (DEBUG_MODE) Log.d("ERROR", error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
        productTypeArrayList.add(new ProductType(-1, "Contact", "https://image.ibb.co/dVdfrV/contact-icon.png"));
        productTypeArrayList.add(new ProductType(-2, "Info", "https://image.ibb.co/cM2vQq/info-icon.png"));
        if (DEBUG_MODE) Log.d("MainActivity", "Product type array list: " + productTypeArrayList.toString());
        if (DEBUG_MODE) Log.d("MainActivity", "loadProductTypeFromServer() completed");
    }

    private void actionBar() {
        setSupportActionBar(toolbarHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarHome.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void viewFlipper() {
        List<String> list = new ArrayList<>();
        list.add("https://image.ibb.co/fwsS1L/nuug3.jpg");
        list.add("https://image.ibb.co/nQeJQf/ssgalaxynote9.jpg");
        list.add("https://image.ibb.co/cNLQ5f/ggpixel3.jpg");
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(this);
            try {
                Picasso.with(this).load(list.get(i)).into(imageView);
            } catch (Exception e) {
                Log.d("ERROR", "Something went wrong");
            }
            viewFlipper.addView(imageView);
            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(5000);
            Animation inAnimation = new AnimationUtils().loadAnimation(this, R.anim.slide_in_from_right);
            Animation outAnimation = new AnimationUtils().loadAnimation(this, R.anim.slide_out_to_left);
            viewFlipper.setInAnimation(inAnimation);
            viewFlipper.setOutAnimation(outAnimation);
        }
        if (DEBUG_MODE) Log.d("MainActivity", "viewFlipper() completed");
    }

    private Drawable loadImageFromWeb(String s) {
        try {
            URL url = new URL(s);
            InputStream is = (InputStream) url.getContent();
            Drawable drawable = Drawable.createFromStream(is, "image");
            return drawable;
        } catch (IOException e) {
            return null;
        }
    }

    private void mapped() {
        toolbarHome = findViewById(R.id.toolBar);
        viewFlipper = findViewById(R.id.viewFlipper);
        drawerLayout = findViewById(R.id.drawerLayout);
        recyclerViewHome = findViewById(R.id.recyclerViewHome);
        navigationView = findViewById(R.id.navigationView);
        listViewMenu = findViewById(R.id.listViewMenu);
        productTypeArrayList = new ArrayList<>();
        productArrayList = new ArrayList<>();
        productTypeAdapter = new ProductTypeAdapter(productTypeArrayList,this);
        productsAdapter = new ProductsAdapter(productArrayList, this);
        listViewMenu.setAdapter(productTypeAdapter);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewHome.setAdapter(productsAdapter);
        if (DEBUG_MODE) Log.d("MainActivity", "mapped() completed");
    }
}
