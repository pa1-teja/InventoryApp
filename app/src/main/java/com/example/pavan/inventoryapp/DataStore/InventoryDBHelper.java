package com.example.pavan.inventoryapp.DataStore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PAVAN on 1/5/2018.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "inventory.db";

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + InventoryContract.InventoryEntry.TABLE_NAME +
                " (" + InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                       InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME + " TEXT NOT NULL," +
                       InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE+ " TEXT NOT NULL," +
                       InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY + " TEXT NOT NULL," +
                       InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL," +
                       InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL+ " TEXT NOT NULL," +
                       InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE+ " TEXT NOT NULL" + ")";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DROP_INVENTORY_TABLE = "DROP TABLE IF EXISTS " + InventoryContract.InventoryEntry.TABLE_NAME;

        db.execSQL(SQL_DROP_INVENTORY_TABLE);

        onCreate(db);
    }
}
