package com.janiak.worktimer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.janiak.worktimer.JodaFormatterProvider;
import com.janiak.worktimer.R;
import com.janiak.worktimer.storage.WorkTime;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chips on 29.04.2015.
 */
public class WorkTimeAdapter extends ArrayAdapter<WorkTime> {
    private static class ViewHolder {
        private TextView itemView;
    }

    public WorkTimeAdapter(Context context, int textViewResourceId, List<WorkTime> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.view_worktime, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView)convertView.findViewById(R.id.workTimeTitle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WorkTime item = getItem(position);
        if (item!= null) {
            String durationText = "";

            if (item.getEnd() != null) {
                Period period = new Duration(item.getStart(), item.getEnd()).toPeriod();
                durationText = JodaFormatterProvider.getDurationFormatter().print(period);
            }


            viewHolder.itemView.setText(
                durationText + ": " +

                item.getStart().toString("dd.MM.yyyy HH:mm:ss")
                + (item.getEnd() == null ? "" : (" - " + item.getEnd().toString("dd.MM.yyyy HH:mm:ss")))
            );
        }

        return convertView;
    }
}
