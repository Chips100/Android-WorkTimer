package com.janiak.worktimer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.janiak.worktimer.R;
import com.janiak.worktimer.asynctasks.SaveWorkTimeTask;
import com.janiak.worktimer.storage.WorkTime;

import org.joda.time.DateTime;

/**
 * Created by Chips on 06.05.2015.
 */
public class ManualInputFragment extends Fragment {
    private DatePicker manualInputFromDate;
    private DatePicker manualInputToDate;
    private TimePicker manualInputFromTime;
    private TimePicker manualInputToTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed_manualinput, container, false);

        manualInputFromDate = (DatePicker)rootView.findViewById(R.id.manualInputFromDate);
        manualInputToDate = (DatePicker)rootView.findViewById(R.id.manualInputToDate);
        manualInputFromTime = (TimePicker)rootView.findViewById(R.id.manualInputFromTime);
        manualInputToTime = (TimePicker)rootView.findViewById(R.id.manualInputToTime);

        manualInputFromTime.setIs24HourView(true);
        manualInputToTime.setIs24HourView(true);

        return rootView;
    }

    public void saveManualInput(View view) {
        new SaveWorkTimeTask(this.getActivity()) {
            @Override
            protected void onPostExecute(WorkTime[] workTime) {
                Toast toast = Toast.makeText(this.getContext(), "Eingabe gespeichert.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }.execute(this.getEnteredWorkTime());
    }

    private WorkTime getEnteredWorkTime() {
        DateTime from = new DateTime(
                manualInputFromDate.getYear(),
                manualInputFromDate.getMonth() + 1,
                manualInputFromDate.getDayOfMonth(),
                manualInputFromTime.getCurrentHour(),
                manualInputFromTime.getCurrentMinute()
        );

        DateTime to = new DateTime(
                manualInputToDate.getYear(),
                manualInputToDate.getMonth() + 1,
                manualInputToDate.getDayOfMonth(),
                manualInputToTime.getCurrentHour(),
                manualInputToTime.getCurrentMinute()
        );

        return WorkTime.createFinished(from, to);
    }
}
