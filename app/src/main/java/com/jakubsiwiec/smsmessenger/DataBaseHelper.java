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
    private static final String DATABASE_NAME = "messagesMenager";

    // Table Names
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_MESSAGES = "messages";

    // Contacts table keys
    private static final String KEY_ID_CONTACT = "ID_contact";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NUMBER = "phone_number";

    // Messages table keys
    private static final String KEY_ID_MESSAGE= "ID_message";
    private static final String KEY_SENT_PHONE_NUMBER = "received_phone_number";
    private static final String KEY_RECEIVED_PHONE_NUMBER = "received_phone_number";
    private static final String KEY_DATETIME = "datetime";
    private static final String KEY_SENT = "sent";  // idk if necessary
    private static final String KEY_SEEN = "seen";  // also maybe remove it later

    // Create tables
    private static final String CREATE_TABLE_CONTACTS =  "CREATE TABLE " + TABLE_CONTACTS + " (" + KEY_ID_CONTACT
            + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT " + KEY_PHONE_NUMBER + " TEXT;";

    // Create tables
    private static final String CREATE_TABLE_MESSAGES =  "CREATE TABLE " + TABLE_MESSAGES + " (" + KEY_ID_MESSAGE
            + " INTEGER PRIMARY KEY," + KEY_SENT_PHONE_NUMBER + " TEXT " + KEY_RECEIVED_PHONE_NUMBER + " TEXT " +
            KEY_DATETIME + " DATETIME " + KEY_SENT + " BIT " + KEY_SEEN + " BIT;";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES + ";");
        onCreate(db);

    }


}