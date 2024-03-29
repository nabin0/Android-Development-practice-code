package com.nabin.sqlitecontactsdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.net.IDN;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "contact_database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CONTACTS = "contacts";
    public static final String KEY_ID = "contact_id";
    public static final String KEY_NAME = "contact_name";
    public static final String KEY_CONTACT_NUMBER = "contact_number";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT, " + KEY_CONTACT_NUMBER + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(sqLiteDatabase);
    }

    public void insertContacts(ContactModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, contact.getName());
        contentValues.put(KEY_CONTACT_NUMBER, contact.getContact_no());

        db.insert(TABLE_CONTACTS, null, contentValues);
        db.close();
    }

    public ArrayList<ContactModel> fetchAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<ContactModel> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContactModel model = new ContactModel(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2));

            contacts.add(model);
        }
        db.close();
        return contacts;
    }

    public void updateContact(ContactModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, model.getName());
        cv.put(KEY_CONTACT_NUMBER, model.getContact_no());
        db.update(TABLE_CONTACTS, cv, KEY_ID + "= " + model.id, null);
        db.close();
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
