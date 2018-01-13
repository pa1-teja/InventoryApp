package com.example.pavan.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pavan.inventoryapp.DataStore.InventoryContract;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    public FloatingActionButton addInventory_button;
    public RecyclerView inventoryListRecyclerView;
    public TextView emptyView;
    private InventoryListRecyclerAdapter listRecyclerAdapter;
    static final int INVENTORY_LOADER = 1993;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(INVENTORY_LOADER,null,this);

        inventoryListRecyclerView = findViewById(R.id.inventory_list);
        addInventory_button = findViewById(R.id.add_inventory);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);

        addInventory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddProduct.class));
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE
        };

        return new CursorLoader(MainActivity.this,
                InventoryContract.InventoryEntry.CONTENT_URI,
                projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        listRecyclerAdapter = new InventoryListRecyclerAdapter(this, data);

        if (data.getCount() >0) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            inventoryListRecyclerView.setLayoutManager(layoutManager);
            inventoryListRecyclerView.setAdapter(listRecyclerAdapter);
        } else
            emptyView.setVisibility(View.VISIBLE);

        listRecyclerAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listRecyclerAdapter.swapCursor(null);
    }
}
