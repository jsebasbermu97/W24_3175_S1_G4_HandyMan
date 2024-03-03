package com.example.handyman.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.handyman.security.SecurityUtils;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "handyman.sqlite";
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
                "profession VARCHAR(255)," +
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
        addOwner(db,"Pirooz", "pirooz@gmail.com", "123456");
        addOwner(db,"Sebastian", "sebastian@gmail.com", "123456");
        addOwner(db,  "Alex", "alex@gmail.com", "123456");
        addOwner(db,"Maria", "maria@gmail.com", "123456");
        addOwner(db,"John", "john@gmail.com", "123456");

        // Insert default data for workers
        addWorker(db,"Carlos", "carlos@gmail.com", "profession1", "123456");
        addWorker(db,"Fiona", "fiona@gmail.com", "profession2", "123456");
        addWorker(db,"Erik", "erik@gmail.com", "profession3", "123456");
        addWorker(db,"Diana", "diana@gmail.com", "profession4", "123456");
        addWorker(db,"Bob", "bob@gmail.com", "profession5", "123456");
    }

    public void addOwner(SQLiteDatabase db, String name, String email, String password) {
        ContentValues values = new ContentValues();
        String hashedPassword = SecurityUtils.hashPassword(password);
        values.put("name", name);
        values.put("email", email);
        values.put("password", hashedPassword);
        db.insert("owner", null, values);
    }

    public void addWorker(SQLiteDatabase db, String name, String email, String profession, String password) {
        ContentValues values = new ContentValues();
        String hashedPassword = SecurityUtils.hashPassword(password);
        values.put("name", name);
        values.put("email", email);
        values.put("profession", profession);
        values.put("password", hashedPassword);
        db.insert("worker", null, values);
    }


    public boolean checkUserCredentials(String email, String password) {
        if (checkUserCredentials("worker", email, password)) {
            return true;
        } else return checkUserCredentials("owner", email, password);
    }

    private boolean checkUserCredentials(String tableName, String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"password"};
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("password");
            if (passwordColumnIndex != -1) {
                String storedHashedPassword = cursor.getString(passwordColumnIndex);
                String inputHashedPassword = SecurityUtils.hashPassword(password);
                cursor.close();
                db.close();
                return storedHashedPassword.equals(inputHashedPassword);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }


}
