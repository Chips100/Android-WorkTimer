package com.janiak.worktimer;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Created by Chips on 15.04.2015.
 */
public class JodaFormatterProvider {
    private static final PeriodFormatter durationFormatter = new PeriodFormatterBuilder()
        .minimumPrintedDigits(2).printZeroAlways()
        .appendHours().appendSeparator(":")
        .appendMinutes().appendSeparator(":")
        .appendSeconds().toFormatter();


    public static PeriodFormatter getDurationFormatter() {
        return durationFormatter;
    }
}
