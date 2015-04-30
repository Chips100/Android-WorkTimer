package com.janiak.worktimer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.janiak.worktimer.JodaFormatterProvider;
import com.janiak.worktimer.R;
import com.janiak.worktimer.asynctasks.LoadUnfinishedWorkTimeTask;
import com.janiak.worktimer.asynctasks.ToggleActiveWorkTimeTask;
import com.janiak.worktimer.storage.WorkTime;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

/**
 * Created by Chips on 29.04.2015.
 */
public class TimerFragment extends Fragment {
    private TextView activeTimerDisplay;
    private Button timerRecordButton;

    private WorkTime activeWorkTime;
    private final Handler updateTimerHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed_timer, container, false);

        activeTimerDisplay = (TextView)rootView.findViewById(R.id.activeTimerDisplay);
        timerRecordButton = (Button)rootView.findViewById(R.id.timerRecordButton);

        loadUnfinishedWorkTimeFromDb();
        return rootView;
    }

    private void loadUnfinishedWorkTimeFromDb() {
        new LoadUnfinishedWorkTimeTask() {
            @Override
            protected void onPostExecute(WorkTime workTime) {
                activeWorkTime = workTime;
                updateUiElements();
            }
        }.execute(this.getActivity());
    }

    public void toggleTimer(View view) {
        new ToggleActiveWorkTimeTask() {
            @Override
            protected void onPostExecute(WorkTime workTime) {

                activeWorkTime = !workTime.isFinished() ? workTime : null;
                updateUiElements();
            }
        }.execute(this.getActivity());
    }

    private void updateUiElements() {
        updateTimerButtonText();
        updateActiveTimerDisplay();
    }

    private void updateTimerButtonText() {
        timerRecordButton.setText(activeWorkTime == null ? "Start" : "Stop");
    }

    private void updateActiveTimerDisplay() {
        if (activeWorkTime == null) {
            activeTimerDisplay.setText("");
        }
        else {
            Period period = new Duration(activeWorkTime.getStart(), DateTime.now()).toPeriod();
            activeTimerDisplay.setText(JodaFormatterProvider.getDurationFormatter().print(period));

            // if we have an active running timer, schedule a UI update.
            updateTimerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateActiveTimerDisplay();
                }
            }, 1000);
        }
    }
}
