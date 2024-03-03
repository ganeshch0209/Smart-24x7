package com.example.smart24x7;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "smart.db";
    public static final String TABLE_NAME = "userdata";
    private static final String COLUMN_1 = "Id";
    private static final String COLUMN_2 = "Name";
    private static final String COLUMN_3 = "PhoneNo";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_1 + " INTEGER PRIMARY KEY, " +
                COLUMN_2+" TEXT, " +
                COLUMN_3+" TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public List<MyModel> getAllData() {
       List<MyModel> dataList = new ArrayList<>();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
       if (cursor.moveToFirst()) {
           do {
                String column1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_1));
                String column2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_2));
                String column3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_3));
                 MyModel model = new MyModel(column1, column2, column3);
                dataList.add(model);
                cursor.moveToNext();
           } while (!cursor.isAfterLast() && !cursor.isAfterLast());
        }
       cursor.close();
        db.close();
        return dataList;
    }
    public String getPhoneNumberById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String phoneNumber = null;
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_3}, COLUMN_1 + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_3));
        }
        cursor.close();
        db.close();
        return phoneNumber;
    }
    public static String getPhoneNumberColumnName() {
        return COLUMN_3;
    }
    public boolean deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_1 + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
    public boolean deleteAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, null, null);
        db.close();
        return result > 0;
    }

}
