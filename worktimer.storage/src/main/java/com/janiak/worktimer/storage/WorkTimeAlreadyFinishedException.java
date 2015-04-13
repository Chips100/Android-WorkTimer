package com.janiak.worktimer.storage;

import org.joda.time.DateTime;

/**
 * Created by Chips on 10.04.2015.
 */
public class WorkTimeAlreadyFinishedException extends Exception {
    private final WorkTime finishedWorkTime;
    private final DateTime attemptedFinish;

    public WorkTimeAlreadyFinishedException(WorkTime finishedWorkTime, DateTime attemptedFinish) {
        this.finishedWorkTime = finishedWorkTime;
        this.attemptedFinish = attemptedFinish;
    }


    public WorkTime getFinishedWorkTime() {
        return finishedWorkTime;
    }

    public DateTime getAttemptedFinish() {
        return attemptedFinish;
    }
}
