package com.example.bharath.stock;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bharath on 12/14/2016
 */

class ItemsAdapter extends ArrayAdapter<StockModel> {
    private static final String TAG = "ItemsAdapter";
    private ArrayList<StockModel> modelArr;
    private LayoutInflater vi;
    private SQLiteHelper helper;
    private TextView textView;

    ItemsAdapter(Context context, int resource, ArrayList<StockModel> modelArr, SQLiteHelper helper, TextView textView) {
        super(context, resource, modelArr);
        this.modelArr = modelArr;
        this.helper = helper;
        this.textView = textView;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_items, parent, false);
            holder = new ViewHolder();
            holder.snoTv = (TextView) convertView.findViewById(R.id.sno);
            holder.itemNameTv = (TextView) convertView.findViewById(R.id.item_name);
            holder.inStockTv = (TextView) convertView.findViewById(R.id.instock);
            holder.outStockTv = (TextView) convertView.findViewById(R.id.outstock);
            holder.totalStockTv = (TextView) convertView.findViewById(R.id.total_stock);
            holder.editBtn = (Button) convertView.findViewById(R.id.edit_btn);
            holder.deleteBtn = (Button) convertView.findViewById(R.id.delete_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.snoTv.setText(String.valueOf(position + 1));
        holder.itemNameTv.setText(modelArr.get(position).getName());
        holder.inStockTv.setText(String.valueOf(modelArr.get(position).getInStock()));
        holder.outStockTv.setText(String.valueOf(modelArr.get(position).getOutStock()));
        holder.totalStockTv.setText(String.valueOf(modelArr.get(position).getTotStock()));
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStockDetails(position, view);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = helper.delete(modelArr.get(position).getName());
                if (id <= 0) {
                    Message.message(view, "Item deletion failed..");
                    //Log.e(TAG, "modelArr:-:" + modelArr);
                } else {
                    Message.message(view, "1 item deleted..");
                    modelArr.remove(position);
                    if (modelArr.size() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
                }

            }
        });
        return convertView;
    }

    private void getStockDetails(final int position, final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Add item details");
        final View v = vi.inflate(R.layout.alert_dialogue, null);
        alertDialog.setView(v);
        final EditText itemNameEt = (EditText) v.findViewById(R.id.item_Name_et);
        final EditText inStockEt = (EditText) v.findViewById(R.id.item_instock_et);
        final EditText outStockEt = (EditText) v.findViewById(R.id.item_outstock_et);
        final String nameStr = modelArr.get(position).getName();
        final String inStockStr = String.valueOf(modelArr.get(position).getInStock());
        final String outStockStr = String.valueOf(modelArr.get(position).getOutStock());

        itemNameEt.setText(nameStr);
        inStockEt.setText(inStockStr);
        outStockEt.setText(outStockStr);

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    String strName = itemNameEt.getText().toString().trim();
                    String strInStock = inStockEt.getText().toString().trim();
                    String strOutStock = outStockEt.getText().toString().trim();
                    int intInStock = Integer.parseInt(strInStock);
                    int intOutStock = Integer.parseInt(strOutStock);
                    int totStock = intInStock + intOutStock;
                    long id = helper.update(nameStr, strName, inStockStr, strInStock, outStockStr, strOutStock);
                    if (id < 0) {
                        Message.message(view, "Updation failed.., Please try again");
                    } else {
                        Message.message(view, "1 item updated..!");
                        modelArr.get(position).setName(strName);
                        modelArr.get(position).setInStock(Integer.parseInt(strInStock));
                        modelArr.get(position).setOutStock(Integer.parseInt(strOutStock));
                        modelArr.get(position).setTotStock(totStock);
                        notifyDataSetChanged();

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

    private static class ViewHolder {
        TextView snoTv;
        TextView itemNameTv;
        TextView inStockTv;
        TextView outStockTv;
        TextView totalStockTv;
        Button editBtn;
        Button deleteBtn;
    }
}
