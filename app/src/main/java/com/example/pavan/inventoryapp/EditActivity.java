package com.example.pavan.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pavan.inventoryapp.DataStore.InventoryContract;

public class EditActivity extends AppCompatActivity {

    public EditText productName, price,quantity,supplierName,supplierEmail,supplierPhone;
    public Button addQuantity,reduceQuantity,saveProduct,placeOrder;
    private long prod_quantity;
    private String supplier_phone;
    private String product_name;
    private String product_price;
    private String product_quantity;
    private String supplier_name;
    private String supplier_email;
    private int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        productName = findViewById(R.id.product_name_input);
        price       = findViewById(R.id.product_price_input);
        quantity    = findViewById(R.id.product_quantity_input);
        supplierName= findViewById(R.id.supplier_name);
        supplierEmail= findViewById(R.id.supplier_email);
        supplierPhone= findViewById(R.id.supplier_phone);

        addQuantity = findViewById(R.id.add_quantity);
        reduceQuantity = findViewById(R.id.reduct_quantity);
        saveProduct = findViewById(R.id.save_button);
        placeOrder  = findViewById(R.id.place_order_button);

        product_name = getIntent().getStringExtra("product_name");
        product_price = getIntent().getStringExtra("price");
        product_quantity = getIntent().getStringExtra("quantity");
        supplier_name = getIntent().getStringExtra("supplier_name");
        supplier_email= getIntent().getStringExtra("supplier_email");
        supplier_phone= getIntent().getStringExtra("supplier_phone");
        _id = getIntent().getIntExtra("data_id",-1);

        prod_quantity = Integer.parseInt(product_quantity);

        productName.setText(product_name);
        price.setText(product_price);
        quantity.setText(product_quantity);
        supplierName.setText(supplier_name);
        supplierEmail.setText(supplier_email);
        supplierPhone.setText(supplier_phone);

        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        reduceQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              boolean b = updateProduct();
              if (b)
                  startActivity(new Intent(EditActivity.this,MainActivity.class));
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO = {supplier_email};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Placing Inventory Order");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n I just wanted to place an order of "+ product_name+ " of Quantity: "
                        + product_quantity + " which is worth of Rs." + product_price + "/-" + "\n" + "Please deliver the goods as soon as possible. \n"
                + "\n" + " Thank you.");


                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i(getClass().getName(), "Finished sending email...");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(EditActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void decreaseQuantity(){
        if (prod_quantity >0)
            quantity.setText(String.valueOf(--prod_quantity));
        else
            quantity.setText(String.valueOf(0));
    }

    public void increaseQuantity(){
        quantity.setText(String.valueOf(++prod_quantity));
    }

    private boolean updateProduct() {


            product_name = productName.getText().toString();
            product_price= price.getText().toString();
            product_quantity = quantity.getText().toString();
            supplier_name = supplierName.getText().toString();
            supplier_email = supplierEmail.getText().toString();
            supplier_phone = supplierPhone.getText().toString();


        if (product_name.isEmpty() || product_name == null){
            alertUser("productName_str");
            return false;
        }
        else if (!product_price.contains("[^0-9]") && (product_price.isEmpty() || product_price== null)){
            alertUser("price_str");
            return false;
        }
        else if (!product_quantity.contains("[^0-9]") && (product_quantity.isEmpty() || product_quantity == null)){
            alertUser("quantity_str");
            return false;
        }
        else if (supplier_name.isEmpty() || supplier_name== null){
            alertUser("supplierName_str");
            return false;
        }
        else if (!(!TextUtils.isEmpty(supplier_email) && Patterns.EMAIL_ADDRESS.matcher(supplier_email).matches())){
            alertUser("supplierEmail_str");
            return false;
        }
        else if (!(android.util.Patterns.PHONE.matcher(supplier_phone).matches())){
            alertUser("supplierPhone_str");
            return false;
        }
        else {
//            prod_quantity = Integer.parseInt(quantity_str);
            ContentValues values = new ContentValues();
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,product_name);
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE,product_price);
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY,prod_quantity);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,supplier_name);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,supplier_email);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE,supplier_phone);

            int rowsUpdated = getContentResolver().update(InventoryContract.InventoryEntry.CONTENT_URI,
                    values, InventoryContract.InventoryEntry._ID + "=?" , new String[]{String.valueOf(_id)});

            if (rowsUpdated > 0){
                Log.d(getClass().getName(),"Data Updated successfully");
                Toast.makeText(this,"Data Updated successfully",Toast.LENGTH_LONG).show();
                return true;
            }
            else {
                Log.d(getClass().getName(), "Data Update failed");
                Toast.makeText(this, "Data Update failed", Toast.LENGTH_LONG).show();
                return true;
            }
        }
    }


    public void alertUser(String match){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oops");
        switch (match){
            case "productName_str":
                builder.setMessage("The product name cannot be empty. " +
                        "Please check and re-type the product name.");
                builder.setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productName.setFocusable(true);
                    }
                });
                break;
            case "price_str":
                builder.setMessage("The product price cannot be empty or contain alphabets or charecters. It can only be numbers. " +
                        "Please check and re-type the product price.");
                builder.setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        price.setFocusable(true);
                    }
                });
                break;
            case "quantity_str":
                builder.setMessage("The product quantity cannot be alphabets or special charecters. It can only be numbers. " +
                        "Please check and re-type the product quantity.");
                builder.setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quantity.setFocusable(true);
                    }
                });
                break;
            case "supplierName_str":
                builder.setMessage("The Supplier Name cannot contain special charecters or numbers." +
                        "Please check and re-type the Supplier Name.");
                builder.setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        supplierName.setFocusable(true);
                    }
                });
                break;
            case "supplierEmail_str":
                builder.setMessage("The input doesn't seem to be a valid E-mail address" +
                        "Please check and re-type the Supplier E-mail address.");
                builder.setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        supplierEmail.setFocusable(true);
                    }
                });
                break;
            case "supplierPhone_str":
                builder.setMessage("The Phone number cannot be alphabets or special charecters. It can only be numbers. " +
                        "Please check and re-type the Supplier's phone number.");
                builder.setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        supplierPhone.setFocusable(true);
                    }
                });
                break;
            default:
                builder.setMessage("Please re-check all the information.");
                builder.setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productName.setFocusable(true);
                    }
                });
                break;
        }

        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.del_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.delete_product) {
            boolean b = deleteProduct();
            if (b){
                Toast.makeText(this,"Product details deleted",Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditActivity.this,MainActivity.class));
            }
            return true;
        }

        return false;

    }

    private boolean deleteProduct() {
        String del_item = productName.getText().toString();

        int deletedRows = getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI,
                InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME + "=?", new String[]{del_item});

        if (deletedRows != 0)
        {
            Log.d(getClass().getName(),"deleted rows : " + deletedRows);
            return true;
        }
        return false;
    }
}
