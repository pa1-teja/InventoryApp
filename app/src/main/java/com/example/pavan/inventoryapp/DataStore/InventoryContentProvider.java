package com.example.pavan.inventoryapp.DataStore;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.pavan.inventoryapp.DataStore.InventoryContract.InventoryEntry.CONTENT_URI;

/**
 * Created by PAVAN on 1/5/2018.
 */

public class InventoryContentProvider extends ContentProvider {

    public static final int INVENTORY = 100;
    public static final int INVENTORY_SPECIFIC_PRODUCT_DETAILS = 101;

    private static final UriMatcher URI_MATCHER = buildUriMathcher();

    private InventoryDBHelper inventoryDBHelper;


    static UriMatcher buildUriMathcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = InventoryContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, InventoryContract.PATH_INVENTORY, INVENTORY);
        matcher.addURI(authority, InventoryContract.PATH_INVENTORY + "/#", INVENTORY_SPECIFIC_PRODUCT_DETAILS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        inventoryDBHelper = new InventoryDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor retCursor = null;
        SQLiteDatabase database = inventoryDBHelper.getReadableDatabase();
        int match = URI_MATCHER.match(uri);

        switch (match) {
            case INVENTORY:
                database.beginTransaction();
                retCursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder); // TODO: check params
                if (retCursor != null)
                    database.setTransactionSuccessful();
                database.endTransaction();
                break;
            case INVENTORY_SPECIFIC_PRODUCT_DETAILS:
                database.beginTransaction();
                retCursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                if (retCursor != null)
                    database.setTransactionSuccessful();
                database.endTransaction();
            default:
                new UnsupportedOperationException("Unknown URI : " + uri);
        }


        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = URI_MATCHER.match(uri);


        switch (match) {
            case INVENTORY:
                return InventoryContract.InventoryEntry.CONTENT_TYPE;
            case INVENTORY_SPECIFIC_PRODUCT_DETAILS:
                return InventoryContract.InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                new UnsupportedOperationException("Unknown URI : " + uri);
        }

        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = inventoryDBHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case INVENTORY:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
        }

        return super.bulkInsert(uri, values);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase database = inventoryDBHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        long id = 0;

        switch (match) {
            case INVENTORY:
                database.beginTransaction();
                id = database.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    Log.d(getClass().getName(), "Values inserted sucessfully");
                    database.setTransactionSuccessful();
                } else
                    Log.d(getClass().getName(), "Values insertion error.");

                database.endTransaction();
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        database.close();
        return buildWeatherUri(id);
    }

    public static Uri buildWeatherUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase database = inventoryDBHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsDeleted = 0;

        if (null == selection)
            selection = "1"; // this makes delete all rows & return the no.of rows.

        switch (match) {
            case INVENTORY:
                database.beginTransaction();
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    database.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                database.endTransaction();
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = inventoryDBHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsUpdated;

        switch (match) {
            case INVENTORY:
                database.beginTransaction();
                rowsUpdated = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                if (rowsUpdated != 0) {
                    database.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                database.endTransaction();
                break;
            case INVENTORY_SPECIFIC_PRODUCT_DETAILS:
                database.beginTransaction();
                rowsUpdated = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                if (rowsUpdated != 0) {
                    database.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                database.endTransaction();
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        return rowsUpdated;
    }
}
