package com.example.android.inventoryappproject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryappproject.data.ProductContract.ProductEntry;


/**
 * Created by AgiChrisPC on 20/07/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {

    private final MainActivity mMainActivity;

    public ProductCursorAdapter(MainActivity con, Cursor cur) {
        super(con, cur, 0);
        mMainActivity = con;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View v, final Context con, final Cursor cur) {
        final int sellOneCounter = 0;
        final int sellTenCounter = 1;

        TextView nameTV = (TextView) v.findViewById(R.id.productName);
        TextView quantTV = (TextView) v.findViewById(R.id.quantity);
        TextView soldTV = (TextView) v.findViewById(R.id.quantitySold);
        TextView priceTV = (TextView) v.findViewById(R.id.price);
        Button sellOneBtn = (Button) v.findViewById(R.id.btnSell);
        Button sellTenBtn = (Button) v.findViewById(R.id.btnTenSell);
        ImageView image = (ImageView) v.findViewById(R.id.imageView);

        String name = cur.getString(cur.getColumnIndex(ProductEntry.COL_NAME));
        String price = cur.getString(cur.getColumnIndex(ProductEntry.COL_PRICE));
        final int quant = cur.getInt(cur.getColumnIndex(ProductEntry.COL_QUANT));
        final int sold = cur.getInt(cur.getColumnIndex(ProductEntry.COL_SOLD));
        image.setImageURI(Uri.parse(cur.getString(cur.getColumnIndex(ProductEntry.COL_IMAGE))));

        nameTV.setText(name);
        quantTV.setText(String.valueOf(quant));
        priceTV.setText(price);
        soldTV.setText(String.valueOf(sold));

        final long id = cur.getLong(cur.getColumnIndex(ProductEntry._ID));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.clickOnViewItem(id);
            }
        });

        //Allows the user to sell one unit, updating quantity and sold values
        sellOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.selectBtnSold(sellOneCounter, id,
                        quant, sold);
            }
        });

        //Allows the user to sell ten units, updating quantity and sold values
        sellTenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivity.selectBtnSold(sellTenCounter, id,
                        quant, sold);
            }
        });
    }

}