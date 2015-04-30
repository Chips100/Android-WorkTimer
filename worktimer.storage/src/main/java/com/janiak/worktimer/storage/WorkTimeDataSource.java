package com.janiak.worktimer.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

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

    public List<WorkTime> getRecordedWorkTimes(DateTime from, DateTime to) {
        List<WorkTime> workTimes = new ArrayList<WorkTime>();

        // TODO: currently, we set replace missing parameters with min/max values.
        // It would be better to remove the condition itself.
        // Joining the clauses with AND would be helpful, see:
        // http://stackoverflow.com/questions/1751844/java-convert-liststring-to-a-joind-string
        String selection = SqliteOpenHelper_WorkTime.COLUMN_END
                + " >= ? AND ? >= " + SqliteOpenHelper_WorkTime.COLUMN_START;

        String[] selectArgs = new String[] {
            String.valueOf(from != null ? from.getMillis() : Long.MIN_VALUE),
            String.valueOf(to != null ? to.getMillis() : Long.MAX_VALUE)
        };

        Cursor cursor = database.query(
            SqliteOpenHelper_WorkTime.TABLE_WORKTIMES, null, selection, selectArgs,
            null, null, SqliteOpenHelper_WorkTime.COLUMN_START + " DESC"
        );

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            workTimes.add(cursorToWorkTime(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return workTimes;
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
