package com.janiak.worktimer;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.janiak.worktimer.storage.WorkTime;
import com.janiak.worktimer.storage.WorkTimeAlreadyFinishedException;
import com.janiak.worktimer.storage.WorkTimeDataSource;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class MainActivity extends ActionBarActivity {
    private TextView activeTimerDisplay;
    private Button timerRecordButton;

    private WorkTime activeWorkTime;
    private PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .minimumPrintedDigits(2).printZeroAlways()
            .appendHours().appendSeparator(":")
            .appendMinutes().appendSeparator(":")
            .appendSeconds().toFormatter();

    private final Handler updateTimerHandler = new Handler();
    private final Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
        updateActiveTimerDisplay();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activeTimerDisplay = (TextView)findViewById(R.id.activeTimerDisplay);
        timerRecordButton = (Button)findViewById(R.id.timerRecordButton);

        loadUnfinishedWorkTimeFromDb();
        updateUiElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startTimer(View view) {
        WorkTimeDataSource workTimeDataSource = new WorkTimeDataSource(this);
        workTimeDataSource.open();

        if (activeWorkTime == null) {
            activeWorkTime = workTimeDataSource.insertWorkTime(WorkTime.startNew());
        }
        else {
            try {
                workTimeDataSource.updateFinishedWorkTime(activeWorkTime.finish());
            }
            catch(WorkTimeAlreadyFinishedException exception) {
                // error message.
                // forget about the activeWorkTime as if has already been finished.
            }

            activeWorkTime = null;
        }

        workTimeDataSource.close();
        updateUiElements();
    }

    private void loadUnfinishedWorkTimeFromDb() {
        WorkTimeDataSource workTimeDataSource = new WorkTimeDataSource(this);
        workTimeDataSource.open();
        activeWorkTime = workTimeDataSource.getUnfinishedWorkTime();
        workTimeDataSource.close();
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
            Duration duration = new Duration(activeWorkTime.getStart(), DateTime.now());
            activeTimerDisplay.setText(periodFormatter.print(duration.toPeriod()));

            // if we have an active running timer, schedule a UI update.
            updateTimerHandler.postDelayed(updateTimerRunnable, 1000);
        }
    }
}