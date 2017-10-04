package com.example.android.inventoryappproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventoryappproject.data.ProductContract.ProductEntry;


/**
 * Created by AgiChrisPC on 20/07/2017.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    public final static String LOG_TAG = ProductDbHelper.class.getSimpleName();

    public final static String DB_NAME = "productdatabase.db";
    public final static int DB_VERS = 1;

    public ProductDbHelper(Context c) {
        super(c, DB_NAME, null, DB_VERS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a string that contains the SQL statement to create the Product table
        String SQL_CREATE_STOCK_TABLE = "CREATE TABLE " + ProductEntry.TBL_NAME + " ("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COL_NAME + " TEXT NOT NULL,"
                + ProductEntry.COL_PRICE + " INTEGER NOT NULL,"
                + ProductEntry.COL_QUANT + " INTEGER NOT NULL DEFAULT 0,"
                + ProductEntry.COL_SOLD + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COL_SUPP_NAME + " TEXT NOT NULL,"
                + ProductEntry.COL_SUPP_EMAIL + " TEXT NOT NULL,"
                + ProductEntry.COL_IMAGE + " TEXT NOT NULL);";

        //Log the create statement syntax
        Log.v(LOG_TAG, SQL_CREATE_STOCK_TABLE);

        db.execSQL(SQL_CREATE_STOCK_TABLE);
    }

    public void addItem(ProductItem i) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put(ProductEntry.COL_NAME, i.getName());
        v.put(ProductEntry.COL_PRICE, i.getPrice());
        v.put(ProductEntry.COL_QUANT, i.getQuant());
        v.put(ProductEntry.COL_SOLD, i.getSold());
        v.put(ProductEntry.COL_SUPP_NAME, i.getSName());
        v.put(ProductEntry.COL_SUPP_EMAIL, i.getSEmail());
        v.put(ProductEntry.COL_IMAGE, i.getImage());

        Log.v(LOG_TAG, " Added new product to database.");

        long id = db.insert(ProductEntry.TBL_NAME, null, v);
    }

    public void updateItem(long currItem, int price, String suppEmail, int quant, int sold) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        //Updates the four possible values that can be updated during the edit.
        v.put(ProductEntry.COL_PRICE, price);
        v.put(ProductEntry.COL_SUPP_EMAIL, suppEmail);
        v.put(ProductEntry.COL_QUANT, quant);
        v.put(ProductEntry.COL_SOLD, sold);

        String sel = ProductEntry._ID + "=?";
        String[] selArgs = new String[] { String.valueOf(currItem) };

        //Log the product update
        Log.v(LOG_TAG, "Updated: \nPrice = " + price + "\nSupplier Email = " + suppEmail + "\nQuantity = " + quant + "\nSold = " + sold);

        db.update(ProductEntry.TBL_NAME, v, sel, selArgs);
    }

    public void sellOneItem(long itemId, int quant, int sold) {
        SQLiteDatabase db = getWritableDatabase();

        //Init the int variables
        int newQuant, newSold;
        newQuant = 0;
        newSold = 0;

        //If the quantity is already at 0, return without affecting the quantity count
        if (quant == 0) {
            Log.v(LOG_TAG, "Insufficient amount to sell 1 stock.");
            return;
        }

        //Subtract one from the quantity counter, and add one to the sold counter
        if (quant > 0) {
            newQuant = quant -1;
            newSold = sold +1;
        }
        ContentValues v = new ContentValues();

        //make the new Quantity and new Sold counters become the current quantity and sold counters
        v.put(ProductEntry.COL_QUANT, newQuant);
        v.put(ProductEntry.COL_SOLD, newSold);

        String sel = ProductEntry._ID + "=?";
        String[] selArgs = new String[] { String.valueOf(itemId) };

        db.update(ProductEntry.TBL_NAME, v, sel, selArgs);
    }

    public void sellTenItem(long itemId, int quant, int sold) {
        SQLiteDatabase db = getWritableDatabase();

        //Init the int variables
        int newQuant, newSold;
        newQuant = 0;
        newSold = 0;

        //If the quantity is less then 10, return without affecting the quantity count
        if (quant < 10) {
            Log.v(LOG_TAG, "Insufficient amount to sell 10 stock.");
            return;
        }

        //Subtract ten from the quantity counter, and add ten to the sold counter
        if (quant >= 10) {
            newQuant = quant -10;
            newSold = sold +10;
        }
        ContentValues v = new ContentValues();

        //make the new Quantity and new Sold counters become the current quantity and sold counters
        v.put(ProductEntry.COL_QUANT, newQuant);
        v.put(ProductEntry.COL_SOLD, newSold);

        String sel = ProductEntry._ID + "=?";
        String[] selArgs = new String[] { String.valueOf(itemId) };

        db.update(ProductEntry.TBL_NAME, v, sel, selArgs);
    }

    public Cursor productStock() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur;

        String[] proj = {
                ProductEntry._ID,
                ProductEntry.COL_NAME,
                ProductEntry.COL_PRICE,
                ProductEntry.COL_QUANT,
                ProductEntry.COL_SOLD,
                ProductEntry.COL_SUPP_NAME,
                ProductEntry.COL_SUPP_EMAIL,
                ProductEntry.COL_IMAGE
        };
        cur = db.query(
                ProductEntry.TBL_NAME,
                proj,
                null,
                null,
                null,
                null,
                null
        );
        return cur;
    }

    public Cursor currentProduct(long i) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur;

        String[] proj = {
                ProductEntry._ID,
                ProductEntry.COL_NAME,
                ProductEntry.COL_PRICE,
                ProductEntry.COL_QUANT,
                ProductEntry.COL_SOLD,
                ProductEntry.COL_SUPP_NAME,
                ProductEntry.COL_SUPP_EMAIL,
                ProductEntry.COL_IMAGE
        };

        String sel = ProductEntry._ID + "=?";
        String[] selArgs = new String[] { String.valueOf(i) };

        cur = db.query(
                ProductEntry.TBL_NAME,
                proj,
                sel,
                selArgs,
                null,
                null,
                null
        );
        return cur;
    }

    //Called when db needs an upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers) {
        //Still version one for the time being
    }
}
