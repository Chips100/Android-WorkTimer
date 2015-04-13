package com.janiak.worktimer.storage;

import org.joda.time.DateTime;

/**
 * Created by Chips on 08.04.2015.
 */
public final class WorkTime {
    private static final long DEFAULT_ID = -1;

    private final long id;
    private final DateTime start;
    private final DateTime end;

    public WorkTime(long id, DateTime start, DateTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public static WorkTime startNew() {
        return new WorkTime(DEFAULT_ID, DateTime.now(), null);
    }

    public WorkTime finish() throws WorkTimeAlreadyFinishedException {
        return this.finish(DateTime.now());
    }

    public WorkTime finish(DateTime finished) throws WorkTimeAlreadyFinishedException {
        if (this.isFinished()) {
            throw new WorkTimeAlreadyFinishedException(this, finished);
        }

        return new WorkTime(this.getId(), this.getStart(), finished);
    }

    public WorkTime persist(long id) {
        return new WorkTime(id, this.getStart(), this.getEnd());
    }


    public boolean isPersisted() {
        return this.getId() != DEFAULT_ID;
    }

    public boolean isFinished() {
        return this.getEnd() != null;
    }
}
