package com.example.bharath.stock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Bharath on 12/14/2016
 */

class SQLHelper {
    private Helper helper;

    SQLHelper(Context context) {
        helper = new Helper(context);

    }

    long insertData(String Name, int inStock, int outStock) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.NAME, Name);
        contentValues.put(Helper.INSTOCK, inStock);
        contentValues.put(Helper.OUTSTOCK, outStock);
        return db.insert(Helper.TABLE_NAME, null, contentValues);
    }

    int update(String oldName, String newName, String oldInStock, String newInStiock, String oldOutStock, String newOutStock) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.NAME, newName);
        contentValues.put(Helper.INSTOCK, newInStiock);
        contentValues.put(Helper.OUTSTOCK, newOutStock);
        String[] whereArgs = {oldName, oldInStock, oldOutStock};
        return db.update(Helper.TABLE_NAME, contentValues,
                Helper.NAME + " =? and " + Helper.INSTOCK + " =? and " + Helper.OUTSTOCK + " =?", whereArgs);
    }

    ArrayList<Model> getAllData() {
        String[] columns = {Helper.UID, Helper.NAME, Helper.INSTOCK, Helper.OUTSTOCK};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(Helper.TABLE_NAME, columns, null, null, null, null, null);
        //StringBuilder buffer = new StringBuilder();
        ArrayList<Model> modelArr = new ArrayList<>();
        while (cursor.moveToNext()) {
            Model model = new Model();
            //int index0 = cursor.getColumnIndex(Helper.UID);
            int index1 = cursor.getColumnIndex(Helper.NAME);
            int index2 = cursor.getColumnIndex(Helper.INSTOCK);
            int index3 = cursor.getColumnIndex(Helper.OUTSTOCK);
            //int id = cursor.getInt(index0);
            String name = cursor.getString(index1);
            int inStock = cursor.getInt(index2);
            int outStock = cursor.getInt(index3);
            model.setName(name);
            model.setInStock(inStock);
            model.setOutStock(outStock);
            model.setTotStock(inStock + outStock);
            modelArr.add(model);
            //buffer.append(id).append(name).append(inStock).append(outStock).append("\n");
        }
        cursor.close();
        return modelArr;
    }


    int delete(String uid) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {uid};
        return db.delete(Helper.TABLE_NAME, Helper.NAME + " =? ", whereArgs);
    }

    private static class Helper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "STOCK";
        private static final String TABLE_NAME = "STOCK_DETAILS";
        private static final String UID = "_id";
        private static final String NAME = "ITEM_NAME";
        private static final String INSTOCK = "INSTOCK";
        private static final String OUTSTOCK = "OUTSTOCK";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(255), " + INSTOCK + " INTEGER, " + OUTSTOCK + " INTEGER);";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private static final String TAG = "SQLHelperAdapter";
        private Context context;

        Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                //Log.e(TAG, "CREATE_TABLE:-:" + CREATE_TABLE);
                sqLiteDatabase.execSQL(CREATE_TABLE);
                //Message.message(context, "onCreate called");
            } catch (SQLException e) {
                //Message.message(context, "" + e.getMessage());
                Log.e(TAG, "SQLException:-:" + e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                //Log.e(TAG, "DROP_TABLE:-:" + DROP_TABLE);
                //Message.message(context, "onUpgrade called");
                sqLiteDatabase.execSQL(DROP_TABLE);
                onCreate(sqLiteDatabase);
            } catch (SQLException e) {
                //Message.message(context, "" + e.getMessage());
                Log.e(TAG, "SQLException1:-:" + e.getMessage());
            }
        }
    }
}
