package com.example.taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskmanager.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table
        db.execSQL("CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "datetime INTEGER," +
                "status TEXT)");

        // Create notifications table
        db.execSQL("CREATE TABLE notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "message TEXT," +
                "datetime INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS notifications");
        onCreate(db);
    }
}