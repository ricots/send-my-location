package com.example.rusheelshah.sendmylocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rusheelshah on 6/14/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PhoneEntry.db";
    private static final String TAG = "DBHelper";

    public static final String CONTACTS_TABLE_NAME = "contacts";
    //public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public String selectQuery;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE_NAME + "("
                + " INTEGER PRIMARY KEY," + CONTACTS_COLUMN_NAME + " TEXT,"
                + CONTACTS_COLUMN_PHONE+ " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS" + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public void insertContact  (String name, String phone) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", name);
//        contentValues.put("phone", phone);
//        db.insert("contacts", null, contentValues);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTACTS_COLUMN_NAME, name); // Contact Name
        values.put(CONTACTS_COLUMN_PHONE, phone); // Contact Phone Number

        // Inserting Row
        db.insert(CONTACTS_TABLE_NAME, null, values);
        db.close(); // Closing database connection
        Log.i(TAG, "addRecord called");
    }

    public String getContact(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String data = "";
        Cursor cursor = db.query(CONTACTS_TABLE_NAME, new String[]{
                        CONTACTS_COLUMN_NAME, CONTACTS_COLUMN_PHONE}, CONTACTS_COLUMN_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            data = "Name: " + cursor.getString(0) + " Number:" + cursor.getString(1);
            Log.i(TAG, "Name: " + cursor.getString(0) + " Number: " + cursor.getString(1));
        }
        return data;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (String name, String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        //db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void deleteContact (String id)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CONTACTS_TABLE_NAME, CONTACTS_COLUMN_NAME + " = ?",
                    new String[]{id});
            db.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

//    public void deleteAllContacts(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.deleteDatabase(null);
//    }

    public String getAllContacts()
    {
        // Select All Query
        selectQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String data = "";
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                data += "data ID:" + cursor.getString(0) + " Name:" + cursor.getString(1) + " Number:" + cursor.getString(2) + "\n";
                Log.i(TAG, "data ID:" + cursor.getString(0) + " Name:" + cursor.getString(1) + " Number:" + cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return data;
    }
    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, null, null);
    }
}