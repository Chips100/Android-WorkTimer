package com.janiak.worktimer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.janiak.worktimer.storage.WorkTime;
import com.janiak.worktimer.storage.WorkTimeDataSource;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Chips on 29.04.2015.
 */
public abstract class LoadRecordedWorkTimesTask extends AsyncTask<Context, Void, List<WorkTime>> {
    @Override
    protected List<WorkTime> doInBackground(Context... params) {
        if (params.length == 0) {
            throw new IllegalArgumentException("No Context provided for loading of unfinished WorkTime.");
        }

        WorkTimeDataSource workTimeDataSource = new WorkTimeDataSource(params[0]);
        workTimeDataSource.open();
        List<WorkTime> recordedWorkTimes = workTimeDataSource.getRecordedWorkTimes(DateTime.now().minusWeeks(2), DateTime.now());
        workTimeDataSource.close();

        return recordedWorkTimes;
    }

    @Override
    protected abstract void onPostExecute(List<WorkTime> recordedWorkTimes);
}