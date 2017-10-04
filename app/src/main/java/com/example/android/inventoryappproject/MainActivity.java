package com.example.android.inventoryappproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.inventoryappproject.data.ProductContract.ProductEntry;
import com.example.android.inventoryappproject.data.ProductDbHelper;
import com.example.android.inventoryappproject.data.ProductItem;


public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    ProductDbHelper mDbHelper;
    ProductCursorAdapter mAdapter;

    //To keep track of the dummy data that can be added
    public int dataCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new ProductDbHelper(this);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(i);
            }
        });

        final ListView listV = (ListView) findViewById(R.id.listView);
        View emptyV = findViewById(R.id.emptyView);
        listV.setEmptyView(emptyV);

        Cursor cur = mDbHelper.productStock();

        mAdapter = new ProductCursorAdapter(this, cur);
        listV.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swapCursor(mDbHelper.productStock());
    }

    public void clickOnViewItem(long id) {
        Intent i = new Intent(this, EditorActivity.class);
        i.putExtra("itemId", id);
        startActivity(i);
    }

    public void selectBtnSold(int i, long id, int quant, int quantSold) {
        if (i == 0) mDbHelper.sellOneItem(id, quant, quantSold);
        else mDbHelper.sellTenItem(id, quant, quantSold);

        mAdapter.swapCursor(mDbHelper.productStock());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addAllData:
                if (dataCheck == 8) dataCheckPrompt();
                // add all dummy data
                addAllData(dataCheck);
                mAdapter.swapCursor(mDbHelper.productStock());
                return true;
            case R.id.addSingleData:
                if (dataCheck == 8) dataCheckPrompt();
                // add 1 dummy data
                addSingleData(dataCheck);
                mAdapter.swapCursor(mDbHelper.productStock());
                return true;
            case R.id.deleteAllBtn:
                //Delete all the data
                showDeleteConfirmationDialog();
                dataCheck = 0;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dataCheckPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dummyDataMax);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dataCheck = 0;
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

    private void addAllData(int data) {

        //Inform the user that all the dummy data has been added
        Log.v(LOG_TAG, "All dummy data has been added");
        Toast.makeText(this, getString(R.string.successAddAllProduct), Toast.LENGTH_SHORT).show();

        //This switch case will add the remainder dummy data depending on how much data has been already added.
        switch (data) {
            case 0:
                ProductItem bee = new ProductItem("Busy Bee", 5, 2, 0, "Valentines Inc", "valenInc@valen.com", "android.resource://com.example.android.inventoryappproject/drawable/beea");
                mDbHelper.addItem(bee);
            case 1:
                ProductItem dog = new ProductItem("Fluffy Dog", 7, 12, 0, "PetsUk", "petsuk@pets.com", "android.resource://com.example.android.inventoryappproject/drawable/doga");
                mDbHelper.addItem(dog);
            case 2:
                ProductItem womble = new ProductItem("Football Womble", 12, 19, 0, "UK Toys Inc", "uktoysinc@toys.com", "android.resource://com.example.android.inventoryappproject/drawable/footballwomblea");
                mDbHelper.addItem(womble);
            case 3:
                ProductItem garfield = new ProductItem("Garfield Boxer", 14, 18, 0, "UK Toys Inc", "uktoysinc@toys.com", "android.resource://com.example.android.inventoryappproject/drawable/garfieldboxera");
                mDbHelper.addItem(garfield);
            case 4:
                ProductItem heart = new ProductItem("Big Heart", 7, 24, 0, "Valentines Inc", "valenInc@valen.com", "android.resource://com.example.android.inventoryappproject/drawable/hearta");
                mDbHelper.addItem(heart);
            case 5:
                ProductItem penguin = new ProductItem("Festive Penguin", 4, 35, 0, "Collectimals plc", "collectimals@bus.com", "android.resource://com.example.android.inventoryappproject/drawable/penguina");
                mDbHelper.addItem(penguin);
            case 6:
                ProductItem rabbit = new ProductItem("Rabbit", 12, 14, 0, "UK Toys Inc", "uktoysinc@toys.com", "android.resource://com.example.android.inventoryappproject/drawable/rabbita");
                mDbHelper.addItem(rabbit);
            case 7:
                ProductItem warthog = new ProductItem("Warthog", 5, 30, 0, "Collectimals plc", "collectimals@bus.com", "android.resource://com.example.android.inventoryappproject/drawable/warthoga");
                mDbHelper.addItem(warthog);
                dataCheck = 8;
                break;
        }
    }

    private void addSingleData(int data) {

        //Inform the user that the dummy data has been added
        Log.v(LOG_TAG, "One dummy data has been added");
        Toast.makeText(this, getString(R.string.successAddOneProduct), Toast.LENGTH_SHORT).show();

        //This switch case will add data depending on how much dummy data has been added already
        switch (data) {
            case 0: ProductItem bee = new ProductItem("Busy Bee", 5, 2, 0, "Valentines Inc", "valenInc@valen.com", "android.resource://com.example.android.inventoryappproject/drawable/beea");
                mDbHelper.addItem(bee);
                dataCheck += 1;
                break;
            case 1: ProductItem dog = new ProductItem("Fluffy Dog", 7, 12, 0, "PetsUk", "petsuk@pets.com", "android.resource://com.example.android.inventoryappproject/drawable/doga");
                mDbHelper.addItem(dog);
                dataCheck += 1;
                break;
            case 2: ProductItem womble = new ProductItem("Football Womble", 12, 19, 0, "UK Toys Inc", "uktoysinc@toys.com", "android.resource://com.example.android.inventoryappproject/drawable/footballwomblea");
                mDbHelper.addItem(womble);
                dataCheck += 1;
                break;
            case 3: ProductItem garfield = new ProductItem("Garfield Boxer", 14, 18, 0, "UK Toys Inc", "uktoysinc@toys.com", "android.resource://com.example.android.inventoryappproject/drawable/garfieldboxera");
                mDbHelper.addItem(garfield);
                dataCheck += 1;
                break;
            case 4: ProductItem heart = new ProductItem("Big Heart", 7, 24, 0, "Valentines Inc", "valenInc@valen.com", "android.resource://com.example.android.inventoryappproject/drawable/hearta");
                mDbHelper.addItem(heart);
                dataCheck += 1;
                break;
            case 5: ProductItem penguin = new ProductItem("Festive Penguin", 4, 35, 0, "Collectimals plc", "collectimals@bus.com", "android.resource://com.example.android.inventoryappproject/drawable/penguina");
                mDbHelper.addItem(penguin);
                dataCheck += 1;
                break;
            case 6: ProductItem rabbit = new ProductItem("Rabbit", 12, 14, 0, "UK Toys Inc", "uktoysinc@toys.com", "android.resource://com.example.android.inventoryappproject/drawable/rabbita");
                mDbHelper.addItem(rabbit);
                dataCheck += 1;
                break;
            case 7: ProductItem warthog = new ProductItem("Warthog", 5, 30, 0, "Collectimals plc", "collectimals@bus.com", "android.resource://com.example.android.inventoryappproject/drawable/warthoga");
                mDbHelper.addItem(warthog);
                dataCheck += 1;
                break;
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteItem);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            deleteAll();
            finish();
            startActivity(getIntent());
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

    private int deleteAll() {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        Log.v(LOG_TAG, "All product data deleted");
        Toast.makeText(this, getString(R.string.successDeleteAllProduct), Toast.LENGTH_SHORT).show();
        return database.delete(ProductEntry.TBL_NAME, null, null);
    }
}
