package com.janiak.worktimer.storage;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Chips on 08.04.2015.
 */
public class SqliteOpenHelper_WorkTime {
    public static final String TABLE_WORKTIMES = "worktimes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";

    private static final String DATABASE_CREATE = "create table " + TABLE_WORKTIMES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_START + " INTEGER NOT NULL, "
            + COLUMN_END + " INTEGER);";


    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
