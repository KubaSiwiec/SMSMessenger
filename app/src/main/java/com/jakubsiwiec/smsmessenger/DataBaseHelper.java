package com.jakubsiwiec.smsmessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.util.Date;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private  static final String TAG = "DataBaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "messagesManager";

    // Table Names
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_MESSAGES = "messages";

    // Contacts table keys
    private static final String KEY_ID_CONTACT = "ID_contact";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NUMBER = "phone_number";

    // Messages table keys
    private static final String KEY_ID_MESSAGE= "ID_message";
    private static final String KEY_SENT_PHONE_NUMBER = "sent_phone_number";
    private static final String KEY_RECEIVED_PHONE_NUMBER = "received_phone_number";
    private static final String KEY_DATETIME = "datetime";
    private static final String KEY_SENT = "sent";  // idk if necessary
    private static final String KEY_SEEN = "seen";  // also maybe remove it later

    // Create tables
    private static final String CREATE_TABLE_CONTACTS =  "CREATE TABLE " + TABLE_CONTACTS + " (" + KEY_ID_CONTACT
            + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_PHONE_NUMBER + " TEXT);";

    // Create tables
    private static final String CREATE_TABLE_MESSAGES =  "CREATE TABLE " + TABLE_MESSAGES + " (" + KEY_ID_MESSAGE
            + " INTEGER PRIMARY KEY, " + KEY_SENT_PHONE_NUMBER + " TEXT, " + KEY_RECEIVED_PHONE_NUMBER + " TEXT, " +
            KEY_DATETIME + " DATETIME, " + KEY_SENT + " BIT, " + KEY_SEEN + " BIT);";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "Constructor being called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_CONTACTS);
        Log.i(TAG, "Contacts DB created");
        db.execSQL(CREATE_TABLE_MESSAGES);
        Log.i(TAG, "Messages DB created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES + ";");
        onCreate(db);

    }

    // Check if contact exists
    public boolean checkIfContactExists(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT 1 FROM " + TABLE_CONTACTS + " WHERE " + KEY_NAME + " ='" + name + "';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    // Add methods
    public boolean addContact(String name, String phone){
        Log.i(TAG, "Adding to Contacts DB");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_PHONE_NUMBER, phone);

        Log.d(TAG, "add data: adding: "  + name + ", " + phone);

        long result = db.insert(TABLE_CONTACTS, null, contentValues);

        //if something was inserted incorrectly
        if (result == -1) return false;
        else return true;


    }




    // Get methods
    public Cursor getContacts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTACTS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getContacts(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + KEY_NAME + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // Edit methods
    public void EditContact(String contactName, String newName, String newPhone){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, newName); //These Fields should be your String values of actual column names
        cv.put(KEY_PHONE_NUMBER, newPhone);

        String filter = KEY_NAME + " = '" + contactName + "'";

        db.update(TABLE_CONTACTS, cv, filter, null);

    }



    // Delete methods
    public void deleteContact(String contactName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CONTACTS + " WHERE " + KEY_NAME + " = '" + contactName + "'";
        db.execSQL(query);
    }



}
