package com.janiak.worktimer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.janiak.worktimer.storage.WorkTime;
import com.janiak.worktimer.storage.WorkTimeDataSource;

/**
 * Created by Chips on 06.05.2015.
 */
public abstract class SaveWorkTimeTask extends AsyncTask<WorkTime, Void, WorkTime[]> {
    private final Context context;

    public SaveWorkTimeTask(Context context) {
        this.context = context;
    }

    @Override
    protected WorkTime[] doInBackground(WorkTime... params) {
        WorkTime[] insertedWorkTimes = new WorkTime[params.length];

        WorkTimeDataSource db = new WorkTimeDataSource(this.getContext());
        db.open();

        for (int i = 0; i < params.length; i++) {
            insertedWorkTimes[i] = db.insertWorkTime(params[i]);
        }

        db.close();
        return insertedWorkTimes;
    }

    public Context getContext() {
        return this.context;
    }

    @Override
    protected abstract void onPostExecute(WorkTime[] workTime);
}
