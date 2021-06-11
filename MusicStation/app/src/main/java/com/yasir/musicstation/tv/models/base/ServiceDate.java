package com.yasir.musicstation.tv.models.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Only consider the date portion of any instance of this class. The time
 * portion is uninitialized.
 */

public class ServiceDate {

    private static final String DATE_FORMAT = "MMM dd, yyyy | EEE";
    private static final String DAY_FORMAT = "EEEEEEE";
    private static final String TIME_FORMAT = "h:mm a";
    private Date date;

    public String getDayFormatted() {

        SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);
        return sdf.format(getDate());

    }

    public String getDateFormatted() {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(getDate());

    }

    public String getTimeFormatted() {

        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        return sdf.format(getDate());

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
