package com.example.pavan.inventoryapp.DataStore;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by PAVAN on 1/5/2018.
 */

public class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.pavan.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY = "inventory";

    public static final class InventoryEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INVENTORY).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final String TABLE_NAME = "inventory";

        public static final String COLUMN_PRODUCT_ID = "inventory_id";

        public static final String COLUMN_INVENTORY_PRODUCT_NAME = "product_name";

        public static final String COLUMN_INVENTORY_PRODUCT_PRICE = "product_price";

        public static final String COLUMN_INVENTORY_PRODUCT_QUANTITY = "product_quantity";

        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "product_supplier_name";

        public static final String COLUMN_PRODUCT_SUPPLIER_EMAIL = "product_supplier_email";

        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE = "product_supplier_phone";
    }

}
