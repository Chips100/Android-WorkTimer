package com.janiak.worktimer.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chips on 08.04.2015.
 */
public class WorkTimerSqliteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WorkTimer.db";
    private static final int DATABASE_VERSION = 1;

    public WorkTimerSqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SqliteOpenHelper_WorkTime.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SqliteOpenHelper_WorkTime.onUpgrade(db, oldVersion, newVersion);
    }
}
