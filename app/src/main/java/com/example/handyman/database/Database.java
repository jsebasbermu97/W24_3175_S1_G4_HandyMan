package com.example.handyman.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "handyman.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE owner (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT," +
                "password TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
        populateDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop table if existed and create it again
        db.execSQL("DROP TABLE IF EXISTS my_table");
        onCreate(db);
    }

    private void populateDefaultData(SQLiteDatabase db) {
        // insert default data
        insertData(db, "Pirooz", "pirooz@gmail.com", "123456");
        insertData(db, "Sebastian", "sebastian@gmail.com", "123456");
    }

    private void insertData(SQLiteDatabase db, String name, String email, String password) {
        String insertSql = "INSERT INTO owner (name, email, password) VALUES ('" + name + "', '" + email + "', '" + password + "')";
        db.execSQL(insertSql);
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"}; // You just need to check existence, so fetching the id is enough
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query("owner", columns, selection, selectionArgs, null, null, null);
        boolean userExists = cursor.moveToFirst(); // Check if cursor has at least one entry
        cursor.close();
        return userExists;
    }

}
