package com.example.handyman.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "handyman.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OWNER_TABLE = "CREATE TABLE owner (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(255)," +
                "email VARCHAR(255)," +
                "type VARCHAR(255) DEFAULT 'owner'," +
                "password VARCHAR(255)" +
                ")";
        db.execSQL(CREATE_OWNER_TABLE);

        String CREATE_WORKER_TABLE = "CREATE TABLE worker (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(255)," +
                "email VARCHAR(255)," +
                "type VARCHAR(255) DEFAULT 'worker'," +
                "password VARCHAR(255)" +
                ")";
        db.execSQL(CREATE_WORKER_TABLE);

        populateDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop table if existed and create it again
        db.execSQL("DROP TABLE IF EXISTS owner");
        db.execSQL("DROP TABLE IF EXISTS worker");
        onCreate(db);
    }

    private void populateDefaultData(SQLiteDatabase db) {
        // Insert default data for owners
        insertData(db, "owner", "Pirooz", "pirooz@gmail.com", "owner", "123456");
        insertData(db, "owner", "Sebastian", "sebastian@gmail.com", "owner", "123456");
        insertData(db, "owner", "Alex", "alex@gmail.com", "owner", "123456");
        insertData(db, "owner", "Maria", "maria@gmail.com", "owner", "123456");
        insertData(db, "owner", "John", "john@gmail.com", "owner", "123456");

        // Insert default data for workers
        insertData(db, "worker", "Carlos", "carlos@gmail.com", "worker", "123456");
        insertData(db, "worker", "Fiona", "fiona@gmail.com", "worker", "123456");
        insertData(db, "worker", "Erik", "erik@gmail.com", "worker", "123456");
        insertData(db, "worker", "Diana", "diana@gmail.com", "worker", "123456");
        insertData(db, "worker", "Bob", "bob@gmail.com", "worker", "123456");
    }

    private void insertData(SQLiteDatabase db, String tableName, String name, String email, String type, String password) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("type", type);
        values.put("password", password);
        db.insert(tableName, null, values);
    }

    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query("owner", columns, selection, selectionArgs, null, null, null);
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        return userExists;
    }

}
