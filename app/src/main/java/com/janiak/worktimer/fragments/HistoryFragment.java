package com.janiak.worktimer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.janiak.worktimer.R;
import com.janiak.worktimer.adapters.WorkTimeAdapter;
import com.janiak.worktimer.asynctasks.LoadRecordedWorkTimesTask;
import com.janiak.worktimer.storage.WorkTime;

import java.util.List;

/**
 * Created by Chips on 29.04.2015.
 */
public class HistoryFragment extends Fragment {
    ListView recordedWorkTimesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed_history, container, false);

        recordedWorkTimesList = (ListView)rootView.findViewById(R.id.recordedWorkTimes);

        loadRecordedWorkTimesFromDb();
        return rootView;
    }

    private void loadRecordedWorkTimesFromDb() {
        new LoadRecordedWorkTimesTask() {
            @Override
            protected void onPostExecute(List<WorkTime> recordedWorkTimes) {
                WorkTimeAdapter adapter = new WorkTimeAdapter(
                    getActivity(),
                    R.layout.view_worktime,
                    recordedWorkTimes
                );

                recordedWorkTimesList.setAdapter(adapter);
            }
        }.execute(getActivity());
    }
}
