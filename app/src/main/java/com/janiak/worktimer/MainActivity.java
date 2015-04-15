package com.janiak.worktimer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.janiak.worktimer.asynctasks.LoadUnfinishedWorkTimeTask;
import com.janiak.worktimer.asynctasks.ToggleActiveWorkTimeTask;
import com.janiak.worktimer.storage.WorkTime;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

public class MainActivity extends ActionBarActivity {
    private TextView activeTimerDisplay;
    private Button timerRecordButton;

    private WorkTime activeWorkTime;
    private final Handler updateTimerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activeTimerDisplay = (TextView)findViewById(R.id.activeTimerDisplay);
        timerRecordButton = (Button)findViewById(R.id.timerRecordButton);

        loadUnfinishedWorkTimeFromDb();
    }

    private void loadUnfinishedWorkTimeFromDb() {
        new LoadUnfinishedWorkTimeTask() {
            @Override
            protected void onPostExecute(WorkTime workTime) {
                activeWorkTime = workTime;
                updateUiElements();
            }
        }.execute(this);
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

    public void toggleTimer(View view) {
        new ToggleActiveWorkTimeTask() {
            @Override
            protected void onPostExecute(WorkTime workTime) {
                activeWorkTime = !workTime.isFinished() ? workTime : null;
                updateUiElements();
            }
        }.execute(this);
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