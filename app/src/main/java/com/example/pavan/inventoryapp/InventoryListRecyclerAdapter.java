package com.example.pavan.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavan.inventoryapp.DataStore.InventoryContract;

/**
 * Created by PAVAN on 1/3/2018.
 */

public class InventoryListRecyclerAdapter extends
        CursorAdapterForRecyclerView<InventoryListRecyclerAdapter.InventoryListViewHolder> {

    public Cursor cursor;
    public Context context;
    private String product_name;
    private String quantity;
    private String price;
    private String supplierName;
    private String supplierEmail;
    private String supplierPhone;


    public InventoryListRecyclerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;
    }


    @Override
    public InventoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.inventory_list_item,parent,false);
        return new InventoryListViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(InventoryListViewHolder viewHolder, Cursor cursor) {
        int idIndex,productNameColumnIndex,priceColIndex,quantityColIndex,supplierNameColIndex,supplierEmailColIndex,supplierPhoneColIndex;

        productNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME);
        priceColIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE);
        quantityColIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY);
        supplierNameColIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        supplierEmailColIndex= cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL);
        supplierPhoneColIndex= cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);
        idIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);

       String product_name = cursor.getString(productNameColumnIndex);
       String quantity     = cursor.getString(quantityColIndex);
       String price        = cursor.getString(priceColIndex);
       String supplierName = cursor.getString(supplierNameColIndex);
       String supplierEmail= cursor.getString(supplierEmailColIndex);
       String supplierPhone= cursor.getString(supplierPhoneColIndex);
       int  _id = cursor.getInt(idIndex);

        setQuantity(quantity);
        viewHolder.inventoryProductName.setText(String.format(context.getString(R.string.inventory_product_name),product_name));
        viewHolder.productQuantity.setText(String.format(context.getString(R.string.inventory_quantity),getQuantity()));
        viewHolder.productPrice.setText(String.format(context.getString(R.string.inventory_price),price));


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditActivity.class);
                intent.putExtra("product_name",product_name);
                intent.putExtra("quantity",getQuantity());
                intent.putExtra("price",price);
                intent.putExtra("data_id",_id);
                intent.putExtra("supplier_name",supplierName);
                intent.putExtra("supplier_email",supplierEmail);
                intent.putExtra("supplier_phone",supplierPhone);

                context.startActivity(intent);
            }
        });


        if (cursor.getString(quantityColIndex) == "0")
            viewHolder.saleButton.setEnabled(false);

        viewHolder.saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = viewHolder.productQuantity.getText().toString().substring(10);

                long v1 = Integer.parseInt(s);
                int id = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
                if (v1 > 0) {
                    --v1;
                    s = String.valueOf(v1);
                    setQuantity(s);
                    viewHolder.productQuantity.setText(String.format(context.getString(R.string.inventory_quantity), getQuantity()));
                    Uri newUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,id);
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY,String.valueOf(v1));
                    context.getContentResolver().update(newUri,values, InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME + "=?",new String[]{product_name});

                }
                else{
                    viewHolder.productQuantity.setText(String.format(context.getString(R.string.inventory_quantity), String.valueOf(0)));
                    viewHolder.saleButton.setEnabled(false);
                    Toast.makeText(context,"Not Available",Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    class InventoryListViewHolder extends RecyclerView.ViewHolder{


        public TextView inventoryProductName, productPrice, productQuantity;
        public Button saleButton;

        public InventoryListViewHolder(View itemView) {
            super(itemView);
            inventoryProductName = itemView.findViewById(R.id.inventory_product_name);
            productPrice         = itemView.findViewById(R.id.inventory_price);
            productQuantity      = itemView.findViewById(R.id.inventory_quantity);
            saleButton           = itemView.findViewById(R.id.sale_button);
        }

    }
}
