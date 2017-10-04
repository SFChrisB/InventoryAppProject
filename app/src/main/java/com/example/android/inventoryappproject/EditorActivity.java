package com.example.android.inventoryappproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventoryappproject.data.ProductContract.ProductEntry;
import com.example.android.inventoryappproject.data.ProductDbHelper;
import com.example.android.inventoryappproject.data.ProductItem;

import static android.R.id.message;


/**
 * Created by AgiChrisPC on 20/07/2017.
 */

public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    private static final int SELECTED_IMAGE = 0;
    private ProductDbHelper mDbHelper;

    EditText nameEdit, priceEdit, quantEdit, soldEdit, suppNameEdit, suppEmailEdit;
    long currItemID;
    Button imageBtn, addBtn, minBtn;
    ImageView imageView;

    Uri productUri;

    Boolean StockHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        nameEdit = (EditText) findViewById(R.id.productNameEdit);
        priceEdit = (EditText) findViewById(R.id.priceEdit);
        quantEdit = (EditText) findViewById(R.id.quantityEdit);
        soldEdit = (EditText) findViewById(R.id.quantitySold);
        suppNameEdit = (EditText) findViewById(R.id.sNameEdit);
        suppEmailEdit = (EditText) findViewById(R.id.sEmailEdit);
        imageView = (ImageView) findViewById(R.id.imageView);
        mDbHelper = new ProductDbHelper(this);
        currItemID = getIntent().getLongExtra("itemId", 0);

        imageBtn = (Button) findViewById(R.id.selectImage);

        //If the ID is 0, this means that we are creating a new Product
        if (currItemID == 0) setTitle(getString(R.string.titleNewProduct));
        else {
            //This is an existing product, so we change the title to say "Edit Product"
            setTitle(getString(R.string.titleEditProduct));
            editExistProduct(currItemID);
        }

        //Attempt to open the image selector method
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                StockHasChanged = true;
            }
        });

        //Change the quantity depending on which button has been pressed
        initQuantBtns();
    }

    private void initQuantBtns() {
        //Initialise the buttons on the page
        addBtn = (Button) findViewById(R.id.addBtn);
        minBtn = (Button) findViewById(R.id.minBtn);

        //Initialise the preset values.
        String lastPrice = priceEdit.getText().toString().trim();
        String lastQuantity = quantEdit.getText().toString().trim();
        String lastSold = soldEdit.getText().toString().trim();
        String lastSuppEmail = suppEmailEdit.getText().toString().trim();

        //Make checks on whether the data gets changed, alerting the user if they forget to save the changes
        if (lastPrice != priceEdit.getText().toString().trim()) StockHasChanged = true;
        if (lastQuantity != quantEdit.getText().toString().trim()) StockHasChanged = true;
        if (lastSold != soldEdit.getText().toString().trim()) StockHasChanged = true;
        if (lastSuppEmail != suppEmailEdit.getText().toString().trim()) StockHasChanged = true;

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            int newQuantI;
            String lastQuantS = quantEdit.getText().toString();
                if (lastQuantS.isEmpty()) {
                    Toast.makeText(EditorActivity.this, "A quantity value has not been input yet.", Toast.LENGTH_SHORT).show();
                    return;
                }

            newQuantI = Integer.parseInt(lastQuantS);
            quantEdit.setText(String.valueOf(newQuantI + 1));
            StockHasChanged = true;
            }
        });

        //Track the changes made when the negative quantity button is pressed.
        //The counter will not fall lower then 0
        minBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            int newQuantI;
            String lastQuantS = quantEdit.getText().toString();

            if (lastQuantS.isEmpty()) {
                Toast.makeText(EditorActivity.this, "You cannot subtract more units of quantity.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!lastQuantS.equals("0")) {
                newQuantI = Integer.parseInt(lastQuantS);
                quantEdit.setText(String.valueOf(newQuantI - 1));
                StockHasChanged = true;
                return;
            }
            else {
                Toast.makeText(EditorActivity.this, "You cannot subtract more units of quantity.", Toast.LENGTH_SHORT).show();
                return;
            }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!StockHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discard =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discard);
    }

    private boolean addProductToDB() {
        int message = 0;

        if (TextUtils.isEmpty(nameEdit.getText())) message = 1;
        if (TextUtils.isEmpty(priceEdit.getText())) message = 1;
        if (TextUtils.isEmpty(quantEdit.getText())) message = 1;
        if (TextUtils.isEmpty(soldEdit.getText())) message = 1;
        if (TextUtils.isEmpty(suppNameEdit.getText())) message = 1;
        if (TextUtils.isEmpty(suppEmailEdit.getText())) message = 1;
        if (currItemID == 0 && productUri == null) message = 1;

        if (message == 1) {
            Toast.makeText(this, "There are fields that have not been filled in.\nPlease do so before continuing.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (currItemID == 0) {
            String nameE = nameEdit.getText().toString().trim();
            int priceE = Integer.parseInt(priceEdit.getText().toString().trim());
            int quantE = Integer.parseInt(quantEdit.getText().toString().trim());
            int soldE = Integer.parseInt(soldEdit.getText().toString().trim());
            String suppNameE = suppNameEdit.getText().toString().trim();
            String suppEmailE = suppEmailEdit.getText().toString().trim();

            ProductItem product = new ProductItem(nameE, priceE, quantE, soldE, suppNameE, suppEmailE, productUri.toString());
            mDbHelper.addItem(product);
            Log.v(LOG_TAG, "Added new product " + product + " into database.");
            Toast.makeText(this, "You have successfully added a new product into the database.", Toast.LENGTH_SHORT).show();
        } else {
            int priceE = Integer.parseInt(priceEdit.getText().toString().trim());
            int quantE = Integer.parseInt(quantEdit.getText().toString().trim());
            int soldE = Integer.parseInt(soldEdit.getText().toString().trim());
            String suppEmailE = suppEmailEdit.getText().toString().trim();

            mDbHelper.updateItem(currItemID, priceE, suppEmailE, quantE, soldE);
            Log.v(LOG_TAG, "Successfully edited product");
            Toast.makeText(this, "You have successfully edited the product.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void editExistProduct(long id) {
        //When editing an existing item, there are only a few fields we want to be accessible.
        //This disables the core items that shouldn't be changed.
        Cursor cur = mDbHelper.currentProduct(id);
        cur.moveToFirst();

        //Image can't be changed from original picture
        imageView.setImageURI(Uri.parse(cur.getString(cur.getColumnIndex(ProductEntry.COL_IMAGE))));
        imageBtn.setEnabled(false);

        //Name of the product can't be changed from original
        nameEdit.setText(cur.getString(cur.getColumnIndex(ProductEntry.COL_NAME)));
        nameEdit.setEnabled(false);

        //Name of the supplier can't be changed from original
        suppNameEdit.setText(cur.getString(cur.getColumnIndex(ProductEntry.COL_SUPP_NAME)));
        suppNameEdit.setEnabled(false);

        //Load up the data that can be edited on the particular product
        suppEmailEdit.setText(cur.getString(cur.getColumnIndex(ProductEntry.COL_SUPP_EMAIL)));
        soldEdit.setText(cur.getString(cur.getColumnIndex(ProductEntry.COL_SOLD)));
        priceEdit.setText(cur.getString(cur.getColumnIndex(ProductEntry.COL_PRICE)));
        quantEdit.setText(cur.getString(cur.getColumnIndex(ProductEntry.COL_QUANT)));

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsavedChanges);
        builder.setPositiveButton(R.string.accept, discardButtonClickListener);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currItemID == 0) {
            MenuItem deleteOneItemMenuItem = menu.findItem(R.id.deleteOneItemBtn);
            MenuItem orderMenuItem = menu.findItem(R.id.orderBtn);
            deleteOneItemMenuItem.setVisible(false);
            orderMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveBtn:
                // save item in DB
                if (!addProductToDB()) return true;
                finish();
                return true;
            case R.id.orderBtn:
                // dialog with email
                emailConfig();
                return true;
            case R.id.deleteOneItemBtn:
                // delete one item
                showDeleteConfirmationDialog(currItemID);
                return true;
            case android.R.id.home:
                if (!StockHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discard =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discard);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //For ordering more units of a particular product via email.
    private void emailConfig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.orderMessage);
        builder.setPositiveButton(R.string.emailAccept, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + suppEmailEdit.getText().toString().trim()));
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Re: New order");
                String bodyMessage = "Dear " + suppNameEdit.getText().toString().trim() + ", \n\nWe " +
                        "would like to place a new order of " +
                        nameEdit.getText().toString().trim() +
                        ". \nPlease send them asap.\n\nYours sincerely";
                intent.putExtra(android.content.Intent.EXTRA_TEXT, bodyMessage);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int deleteProduct(long id) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        String sel = ProductEntry._ID + "=?";
        String[] selArgs = { String.valueOf(id) };
        Log.v(LOG_TAG, "Successfully deleted product: " + id);
        int rowsDeleted = database.delete(ProductEntry.TBL_NAME, sel, selArgs);

        //Display a toast message saying the deletion was successful
        Toast.makeText(this, getString(R.string.successDeleteProduct), Toast.LENGTH_SHORT).show();

        return rowsDeleted;
    }

    private void showDeleteConfirmationDialog(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteItem);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct(id);
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void onActivityResult(int req, int res, Intent picSelected) {
        if (picSelected != null) {
            productUri = picSelected.getData();
            imageView.setImageURI(productUri);
        }
    }

    private void selectImage() {
        Intent i;
        if (Build.VERSION.SDK_INT < 19) i = new Intent(Intent.ACTION_GET_CONTENT);
        else {
            i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
        }
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select image to use: "), SELECTED_IMAGE);
    }

}
