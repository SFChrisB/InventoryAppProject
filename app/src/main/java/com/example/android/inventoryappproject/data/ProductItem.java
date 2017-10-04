package com.example.android.inventoryappproject.data;

/**
 * Created by AgiChrisPC on 20/07/2017.
 */

public class ProductItem {
    private final String mName, mSName, mSEmail, mImage;
    private final int mQuant, mPrice, mSold;

    public ProductItem(String name, int price, int quant, int sold, String sName, String sEmail, String image) {
        mName = name;
        mPrice = price;
        mQuant = quant;
        mSold = sold;
        mSName = sName;
        mSEmail = sEmail;
        mImage = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + mName + '\'' +
                ", sName='" + mSName + '\'' +
                ", sEmail='" + mSEmail + '\'' +
                ", price='" + mPrice + '\'' +
                ", quant=" + mQuant +
                ", sold=" + mSold + '\'' +
                '}';
    }

    public String getName() {return mName;}
    public int getPrice() {return mPrice;}
    public int getQuant() {return mQuant;}
    public int getSold() {return mSold;}
    public String getSName() {return mSName;}
    public String getSEmail() {return mSEmail;}
    public String getImage() {return mImage;}


}
