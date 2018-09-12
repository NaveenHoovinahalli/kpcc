package teli.com.kpcc.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by madhuri on 17/1/15.
 */
public class KpccCalendar {
    Long id ;
    private static final int MINUTES_BEFORE = 60 * 4;
    Context mContext;

    public KpccCalendar(Context context) {
        this.mContext = context;
    }


    public Long addAllDayEvent(Event event) throws ParseException {

        long calId = getCalendarId();
        if (calId == -1) {
            Toast.makeText(mContext, "Calendar Not Found!", Toast.LENGTH_SHORT).show();
            return null;
        }

        /*List<String> repeatDates = Arrays.asList(event.get.split(","));

        if(repeatDates.isEmpty()){
            return null;
        }*/

        String eventDate = event.getEventDate();
        String eventTime = event.getEventTime();

        if (eventDate.isEmpty()){
            return null;
        }

        if (eventTime.isEmpty()){
            return null;
        }

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =time.parse(eventDate +" "+ eventTime);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        long start = cal.getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, start);

        values.put(CalendarContract.Events.TITLE, event.getEventName());
        values.put(CalendarContract.Events.EVENT_LOCATION, event.getEventLocation());
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.DESCRIPTION,event.getEventDetails());

        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
                CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        Uri uri =
                mContext.getContentResolver().
                        insert(CalendarContract.Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());

       // id.add(eventId);

        values.clear();
        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, MINUTES_BEFORE);
        mContext.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, values);

        return id;
    }

    private long getCalendarId() {
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection =
                CalendarContract.Calendars.ACCOUNT_TYPE +
                        " = 'com.google' ";
        Cursor cursor =
                mContext.getContentResolver().
                        query(
                                CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                selection,
                                null,
                                null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }




}
