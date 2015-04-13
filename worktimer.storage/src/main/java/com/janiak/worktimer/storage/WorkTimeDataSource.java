package com.janiak.worktimer.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

/**
 * Created by Chips on 10.04.2015.
 */
public class WorkTimeDataSource {
    private final WorkTimerSqliteOpenHelper dbHelper;
    private SQLiteDatabase database = null;

    public WorkTimeDataSource(Context context) {
        dbHelper = new WorkTimerSqliteOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public WorkTime getUnfinishedWorkTime() {
       Cursor cursor = database.query(SqliteOpenHelper_WorkTime.TABLE_WORKTIMES,
           null, SqliteOpenHelper_WorkTime.COLUMN_END + " IS NULL", null, null, null, SqliteOpenHelper_WorkTime.COLUMN_START + " DESC", String.valueOf(1));

        cursor.moveToFirst();
        WorkTime unfinishedWorkTime = null;

        if (!cursor.isAfterLast()) {
            unfinishedWorkTime = cursorToWorkTime(cursor);
        }

        cursor.close();
        return unfinishedWorkTime;
    }

    public WorkTime insertWorkTime(WorkTime workTime) {
        ContentValues values = new ContentValues();
        values.put(SqliteOpenHelper_WorkTime.COLUMN_START, workTime.getStart().getMillis());
        values.put(SqliteOpenHelper_WorkTime.COLUMN_END, workTime.isFinished() ? workTime.getEnd().getMillis() : null);

        long id = database.insert(SqliteOpenHelper_WorkTime.TABLE_WORKTIMES, null, values);
        return workTime.persist(id);
    }

    public void updateFinishedWorkTime(WorkTime workTime) {
        ContentValues values = new ContentValues();
        values.put(SqliteOpenHelper_WorkTime.COLUMN_END, workTime.getEnd().getMillis());

        database.update(SqliteOpenHelper_WorkTime.TABLE_WORKTIMES,
                values, SqliteOpenHelper_WorkTime.COLUMN_ID + "=?",
                new String[] { String.valueOf(workTime.getId()) }
        );
    }

    private static WorkTime cursorToWorkTime(Cursor cursor) {
        int indexColumnEnd = cursor.getColumnIndex(SqliteOpenHelper_WorkTime.COLUMN_END);

        return new WorkTime(
           cursor.getLong(cursor.getColumnIndex(SqliteOpenHelper_WorkTime.COLUMN_ID)),
           new DateTime(cursor.getLong(cursor.getColumnIndex(SqliteOpenHelper_WorkTime.COLUMN_START))),
           !cursor.isNull(indexColumnEnd)
                   ? new DateTime(cursor.getLong(indexColumnEnd))
                   : null
        );
    }
}
