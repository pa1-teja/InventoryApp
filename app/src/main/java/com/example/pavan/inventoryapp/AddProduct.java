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

public class AddProduct extends AppCompatActivity {

    public EditText productName, price,quantity,supplierName,supplierEmail,supplierPhone;
    public Button addQuantity,reduceQuantity,saveProduct,placeOrder;
    public String productName_str, price_str,quantity_str,supplierName_str,supplierEmail_str,supplierPhone_str;
    public long prod_quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

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


        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = insertProduct();
                if (b){
                    Toast.makeText(AddProduct.this,"Product details saved",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddProduct.this,MainActivity.class));
                }
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textValidations()) {
                    String[] TO = {getSupplierEmail_str()};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);

                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Placing Inventory Order");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n I just wanted to place an order of " + getProductName_str() + " of Quantity: "
                            + getQuantity_str() + " which is worth of Rs." + getPrice_str() + "/-" + "\n" + "Please deliver the goods as soon as possible. \n"
                            + "\n" + " Thank you.");


                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i(getClass().getName(), "Finished sending email...");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(AddProduct.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

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
    }

    public void decreaseQuantity(){
        if (prod_quantity >0)
        quantity.setText(String.valueOf(prod_quantity--));
        else
            quantity.setText(String.valueOf(0));
    }

    public void increaseQuantity(){
        quantity.setText(String.valueOf(prod_quantity++));
    }

    public void setProductName_str(String productName_str) {
        this.productName_str = productName_str;
    }

    public String getProductName_str() {
        return productName_str;
    }

    public void setPrice_str(String price_str) {
        this.price_str = price_str;
    }

    public String getPrice_str() {
        return price_str;
    }

    public void setQuantity_str(String quantity_str) {
        this.quantity_str = quantity_str;
    }

    public String getQuantity_str() {
        return quantity_str;
    }

    public void setSupplierName_str(String supplierName_str) {
        this.supplierName_str = supplierName_str;
    }

    public String getSupplierName_str() {
        return supplierName_str;
    }

    public void setSupplierEmail_str(String supplierEmail_str) {
        this.supplierEmail_str = supplierEmail_str;
    }

    public String getSupplierEmail_str() {
        return supplierEmail_str;
    }

    public void setSupplierPhone_str(String supplierPhone_str) {
        this.supplierPhone_str = supplierPhone_str;
    }

    public String getSupplierPhone_str() {
        return supplierPhone_str;
    }

    public boolean textValidations(){

        productName_str = productName.getText().toString();
        price_str = price.getText().toString();
        quantity_str = quantity.getText().toString();
        supplierName_str = supplierName.getText().toString();
        supplierEmail_str = supplierEmail.getText().toString();
        supplierPhone_str = supplierPhone.getText().toString();

        setProductName_str(productName_str);
        setPrice_str(price_str);
        setQuantity_str(quantity_str);
        setSupplierName_str(supplierName_str);
        setSupplierEmail_str(supplierEmail_str);
        setSupplierPhone_str(supplierPhone_str);


        if (productName_str.isEmpty() || productName_str == null){
            alertUser("productName_str");
            return false;
        }
        else if (!price_str.contains("[^0-9]") && (price_str.isEmpty() || price_str == null)){
            alertUser("price_str");
            return false;
        }
        else if (!quantity_str.contains("[^0-9]") && (quantity_str.isEmpty() || quantity_str == null)){
            alertUser("quantity_str");
            return false;
        }
        else if (supplierName_str.isEmpty() || supplierName_str == null){
            alertUser("supplierName_str");
            return false;
        }
        else if (!(!TextUtils.isEmpty(supplierEmail_str) && Patterns.EMAIL_ADDRESS.matcher(supplierEmail_str).matches())){
            alertUser("supplierEmail_str");
            return false;
        }
        else if (!(android.util.Patterns.PHONE.matcher(supplierPhone_str).matches())){
            alertUser("supplierPhone_str");
            return false;
        }
        else {
            prod_quantity = Integer.parseInt(quantity_str);
            return true;
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

    private boolean insertProduct() {
        boolean b = textValidations();
        if (b){
            ContentValues values = new ContentValues();
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_NAME,productName_str);
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_PRICE,price_str);
            values.put(InventoryContract.InventoryEntry.COLUMN_INVENTORY_PRODUCT_QUANTITY,quantity_str);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,supplierName_str);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL,supplierEmail_str);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE,supplierPhone_str);

            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI,values);

            if (newUri != null){
                Log.d(getClass().getName(),"Data Inserted successfully");
                return true;
            }
            else
                Log.d(getClass().getName(),"Data Insertion failed");
        }
        return false;
    }
}
