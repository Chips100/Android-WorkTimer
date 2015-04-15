package com.janiak.worktimer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.janiak.worktimer.storage.WorkTime;
import com.janiak.worktimer.storage.WorkTimeAlreadyFinishedException;
import com.janiak.worktimer.storage.WorkTimeDataSource;

/**
 * Created by Chips on 15.04.2015.
 */
public abstract class ToggleActiveWorkTimeTask extends AsyncTask<Context, Void, WorkTime> {
    @Override
    protected WorkTime doInBackground(Context... params) {
        if (params.length == 0) {
            throw new IllegalArgumentException("No Context provided for starting new WorkTime.");
        }

        WorkTimeDataSource workTimeDataSource = new WorkTimeDataSource(params[0]);
        workTimeDataSource.open();

        WorkTime activeWorkTime = workTimeDataSource.getUnfinishedWorkTime();

        if (activeWorkTime == null) {
            activeWorkTime = workTimeDataSource.insertWorkTime(WorkTime.startNew());
        }
        else {
            try {
                activeWorkTime = activeWorkTime.finish();
                workTimeDataSource.updateFinishedWorkTime(activeWorkTime);
            }
            catch(WorkTimeAlreadyFinishedException exception) {
                // error message.
                // forget about the activeWorkTime as it has already been finished inbetween.
            }
        }

        workTimeDataSource.close();
        return activeWorkTime;
    }

    @Override
    protected abstract void onPostExecute(WorkTime workTime);
}