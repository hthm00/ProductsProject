package com.example.minhhuynh.productsproject.actvities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minhhuynh.productsproject.R;
import com.example.minhhuynh.productsproject.adapter.PhonesAdapter;
import com.example.minhhuynh.productsproject.model.Product;
import com.example.minhhuynh.productsproject.util.Connection;
import com.example.minhhuynh.productsproject.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhoneActivity extends AppCompatActivity {


    ArrayList<Product> phoneArrayList;
    PhonesAdapter phoneAdapter;
    ListView listViewPhone;
    android.support.v7.widget.Toolbar toolbarPhone;
    View footerView;
    Boolean isLoading;
    int page;
    MyHandler myHandler;
    Boolean isEndData;


    Boolean DEBUG_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        mapped();
        listViewPhoneLoad(page);
        toolbarCustom();
        loadMore();
    }

    private void loadMore() {
        listViewPhone.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int first, int visible, int total) {
                if (total != 0 && !isLoading && !isEndData && first + visible == total) {
                    isLoading = true;
                    MyThread myThread = new MyThread();
                    myThread.start();
                }
            }
        });
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listViewPhone.addFooterView(footerView);
                    break;
                case 1:
                    listViewPhoneLoad(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class MyThread extends Thread {
        @Override
        public void run() {
            myHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage(1);
            myHandler.sendMessage(message);
            super.run();
        }
    }

    private void toolbarCustom() {
        setSupportActionBar(toolbarPhone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarPhone.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void listViewPhoneLoad(int page) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , Server.productsServer + "?page=" + String.valueOf(page), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    listViewPhone.removeFooterView(footerView);
                    if (DEBUG_MODE) Log.d("PhoneActivity", response.toString());
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response);
                        if (jsonArray.length() == 0) isEndData = true;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product product = new Product(jsonObject.getInt("id"), jsonObject.getString("name")
                                    , jsonObject.getString("price"), jsonObject.getString("image")
                                    , jsonObject.getString("description"), jsonObject.getString("productid"));
                            phoneArrayList.add(product);
                            phoneAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (DEBUG_MODE) Log.d("PhoneActivity", "Phone array list: " + phoneArrayList.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Connection.showError(getApplicationContext());
                if (DEBUG_MODE) Log.d("ERROR", error.toString());
            }
        });
        requestQueue.add(stringRequest);
        if (DEBUG_MODE) Log.d("PhoneActivity", "listViewPhoneLoad() completed");
    }

    private void mapped() {
        DEBUG_MODE = true;
        isLoading = false;
        isEndData = false;
        page = 1;
        phoneArrayList = new ArrayList<>();
        phoneAdapter = new PhonesAdapter(phoneArrayList,this);
        listViewPhone = findViewById(R.id.listViewPhone);
        listViewPhone.setAdapter(phoneAdapter);
        toolbarPhone = findViewById(R.id.toolBarPhone);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = layoutInflater.inflate(R.layout.progressbar,null);
        myHandler = new MyHandler();

    }
}
