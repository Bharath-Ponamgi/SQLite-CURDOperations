package com.example.bharath.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    SQLHelper helper;
    TextView textView;
    ListView itemsLv;
    ArrayList<StockModel> modelArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helper = new SQLHelper(HomeActivity.this);

        textView = (TextView) findViewById(R.id.no_items_tv);
        itemsLv = (ListView) findViewById(R.id.items_lv);

        modelArr = new ArrayList<>();

        getAllData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                getStockDetails(view);
            }
        });
    }

    public void getStockDetails(final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Add item details");
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.alert_dialogue, (ViewGroup) findViewById(R.id.rl));
        alertDialog.setView(v);
        final EditText itemNameEt = (EditText) v.findViewById(R.id.item_Name_et);
        final EditText inStockEt = (EditText) v.findViewById(R.id.item_instock_et);
        final EditText outStockEt = (EditText) v.findViewById(R.id.item_outstock_et);

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    String strName = itemNameEt.getText().toString().trim();
                    String strInStock = inStockEt.getText().toString().trim();
                    String strOutStock = outStockEt.getText().toString().trim();
                    int intInStock = Integer.parseInt(strInStock);
                    int intOutStock = Integer.parseInt(strOutStock);
                /*if (TextUtils.isEmpty(strName) && intInStock > 0 && intOutStock > 0){

                }*/
                    long id = helper.insertData(strName, intInStock, intOutStock);
                    //Log.e(TAG, "id::" + id);
                    if (id < 0) {
                        Message.message(view, "Insertion failed.., Please try again");
                    } else {
                        Message.message(view, "1 item added..!");
                        getAllData();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception:-:" + e.getMessage());
                    Message.message(view, "All fields required");
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    public void getAllData() {
        try {
            modelArr = helper.getAllData();
            if (modelArr.size() == 0) {
                textView.setVisibility(View.VISIBLE);
                itemsLv.setVisibility(View.GONE);
                return;
            }
            //Log.e(TAG, "modelArr:-:" + modelArr);
            ItemsAdapter itemsAdapter = new ItemsAdapter(HomeActivity.this, R.layout.list_items, modelArr, helper, textView);
            itemsAdapter.notifyDataSetChanged();
            textView.setVisibility(View.GONE);
            itemsLv.setVisibility(View.VISIBLE);
            itemsLv.setAdapter(itemsAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Exception:-:" + e.getMessage());
        }

    }
}
