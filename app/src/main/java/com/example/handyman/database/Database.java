package com.example.handyman.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.handyman.job.Job;
import com.example.handyman.security.SecurityUtils;
import com.example.handyman.worker.Worker;

import java.util.ArrayList;
import java.util.List;

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
                "password VARCHAR(255)," +
                "address VARCHAR(255)" +
                ")";

        db.execSQL(CREATE_WORKER_TABLE);

        String CREATE_JOB_TABLE = "CREATE TABLE job (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "worker_id INTEGER," +
                "owner_id INTEGER," +
                "title TEXT," +
                "description TEXT," +
                "start_date TEXT," +
                "end_date TEXT," +
                "budget DOUBLE" +
                ")";

        db.execSQL(CREATE_JOB_TABLE);

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
        // insert default data for owners
        addOwner(db,"Pirooz", "pirooz@gmail.com", "123456");
        addOwner(db,"Sebastian", "sebastian@gmail.com", "123456");
        addOwner(db,  "Alex", "alex@gmail.com", "123456");
        addOwner(db,"Maria", "maria@gmail.com", "123456");
        addOwner(db,"John", "john@gmail.com", "123456");

        // insert default data for workers
        addWorker(db,"Carlos", "carlos@gmail.com", "Carpenter", "123456", "7520 1st St, Burnaby, BC V3N 3T2");
        addWorker(db,"Fiona", "fiona@gmail.com", "Electrician", "123456","8042 15th Ave, Burnaby, BC V3N 1X2");
        addWorker(db,"Erik", "erik@gmail.com", "Carpenter", "123456","350 SE Marine Dr, Vancouver, BC V5X 2S5");
        addWorker(db,"Diana", "diana@gmail.com", "Plumber", "123456", "9001 Bill Fox Way, Burnaby, BC V5J 5J3");
        addWorker(db,"Bob", "bob@gmail.com", "Painter", "123456", "11970 88 Ave, Delta, BC V4C 3C8");
    }

    public void addOwner(SQLiteDatabase db, String name, String email, String password) {
        ContentValues values = new ContentValues();
        String hashedPassword = SecurityUtils.hashPassword(password);
        values.put("name", name);
        values.put("email", email);
        values.put("password", hashedPassword);
        db.insert("owner", null, values);
    }

    public void addJob(int workerId, int ownerId, String title, String description, String startDate, String endDate, double budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("worker_id", workerId);
        values.put("owner_id", ownerId);
        values.put("title", title);
        values.put("description", description);
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        values.put("budget", budget);
        db.insert("job", null, values);
        db.close();
    }

    public void addGoogleOwner(SQLiteDatabase db, String name, String email) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", "GOOGLE_SIGN_IN_NO_PASSWORD");
        db.insert("owner", null, values);
    }

    public void addWorker(SQLiteDatabase db, String name, String email, String profession, String password,
                          String address) {
        ContentValues values = new ContentValues();
        String hashedPassword = SecurityUtils.hashPassword(password);
        values.put("name", name);
        values.put("email", email);
        values.put("profession", profession);
        values.put("password", hashedPassword);
        values.put("address", address);
        db.insert("worker", null, values);
    }

    public List<Worker> getWorkersByProfession(String profession) {
        List<Worker> workers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("worker", new String[]{"id", "name", "email", "profession", "address"},
                "profession = ?", new String[]{profession}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Worker worker = new Worker(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("profession")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address"))
                );
                workers.add(worker);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return workers;
    }

    public int getWorkerIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int workerId = -1;

        String[] projection = {"id"};
        String selection = "email = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                "worker",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            workerId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }
        db.close();
        return workerId;
    }

    public String getWorkerNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String ownerName = "";

        String[] projection = {"name"};
        String selection = "email = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                "worker",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            ownerName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
        }
        db.close();
        return ownerName;
    }

    public String getWorkerNameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String workerName = "";

        String[] projection = {"name"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                "worker",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            workerName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
        }
        db.close();
        return workerName;
    }
    public String getWorkerProfessionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String workerProfession = "";

        String[] projection = {"profession"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                "worker",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            workerProfession = cursor.getString(cursor.getColumnIndexOrThrow("profession"));
            cursor.close();
        }
        db.close();
        return workerProfession;
    }


    public int getOwnerIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int ownerId = -1;

        String[] projection = {"id"};
        String selection = "email = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                "owner",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            ownerId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }
        db.close();
        return ownerId;
    }

    public String getOwnerNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String ownerName = "";

        String[] projection = {"name"};
        String selection = "email = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                "owner",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            ownerName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
        }
        db.close();
        return ownerName;
    }

    public String getOwnerNameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String ownerName = "";

        String[] projection = {"name"};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                "owner",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            ownerName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
        }
        db.close();
        return ownerName;
    }

    public List<Job> getJobsForUser(int userId) {
        List<Job> jobs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = db.rawQuery("SELECT * FROM job WHERE owner_id = ?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                Job job = new Job();
                job.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                job.setOwner_id(cursor.getInt(cursor.getColumnIndexOrThrow("owner_id")));
                job.setWorker_id(cursor.getInt(cursor.getColumnIndexOrThrow("worker_id")));
                job.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                job.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                job.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow("start_date")));
                job.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow("end_date")));
                job.setBudget(cursor.getInt(cursor.getColumnIndexOrThrow("budget")));
                jobs.add(job);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return jobs;
    }

    public List<Job> getJobsForWorker(int workerId) {
        List<Job> jobs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM job WHERE worker_id = ?", new String[]{String.valueOf(workerId)});

        if (cursor.moveToFirst()) {
            do {
                Job job = new Job();
                job.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                job.setOwner_id(cursor.getInt(cursor.getColumnIndexOrThrow("owner_id")));
                job.setWorker_id(cursor.getInt(cursor.getColumnIndexOrThrow("worker_id")));
                job.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                job.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                job.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow("start_date")));
                job.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow("end_date")));
                job.setBudget(cursor.getInt(cursor.getColumnIndexOrThrow("budget")));
                jobs.add(job);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return jobs;
    }


    public String checkUserCredentials(String email, String password) {
        if (checkUserCredentials("worker", email, password)) {
            return "worker";
        } else if (checkUserCredentials("owner", email, password)) {
            return "owner";
        } else {
            return null;
        }
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

    public void deleteJobById(int jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("job", "id = ?", new String[]{String.valueOf(jobId)});
        db.close();
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }


}
