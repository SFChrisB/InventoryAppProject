package com.example.android.inventoryappproject.data;

import android.provider.BaseColumns;

/**
 * Created by AgiChrisPC on 20/07/2017.
 */

public class ProductContract {
    public ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {

        public static final String TBL_NAME = "products";

        public static final String _ID = BaseColumns._ID;
        public static final String COL_NAME = "name";
        public static final String COL_PRICE = "price";
        public static final String COL_QUANT = "quant";
        public static final String COL_SOLD = "sold";
        public static final String COL_SUPP_NAME = "supp_name";
        public static final String COL_SUPP_EMAIL = "supp_email";
        public static final String COL_IMAGE = "image";

    }
}

