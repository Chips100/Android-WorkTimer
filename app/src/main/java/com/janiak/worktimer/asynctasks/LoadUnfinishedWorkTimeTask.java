package com.janiak.worktimer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.janiak.worktimer.storage.WorkTime;
import com.janiak.worktimer.storage.WorkTimeDataSource;

/**
 * Created by Chips on 15.04.2015.
 */
public abstract class LoadUnfinishedWorkTimeTask extends AsyncTask<Context, Void, WorkTime> {
    @Override
    protected WorkTime doInBackground(Context... params) {
        if (params.length == 0) {
            throw new IllegalArgumentException("No Context provided for loading of unfinished WorkTime.");
        }

        WorkTimeDataSource workTimeDataSource = new WorkTimeDataSource(params[0]);
        workTimeDataSource.open();
        WorkTime unfinishedWorkTime = workTimeDataSource.getUnfinishedWorkTime();
        workTimeDataSource.close();

        return unfinishedWorkTime;
    }

    @Override
    protected abstract void onPostExecute(WorkTime workTime);
}
