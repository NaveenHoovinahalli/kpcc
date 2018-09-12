package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madhuri on 12/1/15.
 */
public class Event implements Parcelable {

    private String id;

    @SerializedName("event_name")
    private String eventName;

    @SerializedName("event_date")
    private String eventDate;

    @SerializedName("event_location")
    private String eventLocation;

    @SerializedName("event_time")
    private String eventTime;

    @SerializedName("event_details")
    private String eventDetails;

    @SerializedName("volunteer_flag")
    private String volunteerFlag;

    @SerializedName("remainder_flag")
    private String reminderFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate == null ? "" : eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventTime() {
        return eventTime == null ? "" : eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getReminderFlag() {
        return reminderFlag;
    }

    public void setReminderFlag(String reminderFlag) {
        this.reminderFlag = reminderFlag;
    }

    public String getVolunteerFlag() {
        return volunteerFlag;
    }

    public void setVolunteerFlag(String volunteerFlag) {
        this.volunteerFlag = volunteerFlag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.eventName);
        dest.writeString(this.eventDate);
        dest.writeString(this.eventLocation);
        dest.writeString(this.eventTime);
        dest.writeString(this.eventDetails);
        dest.writeString(this.volunteerFlag);
        dest.writeString(this.reminderFlag);
    }

    public Event() {
    }

    private Event(Parcel in) {
        this.id = in.readString();
        this.eventName = in.readString();
        this.eventDate = in.readString();
        this.eventLocation = in.readString();
        this.eventTime = in.readString();
        this.eventDetails = in.readString();
        this.volunteerFlag = in.readString();
        this.reminderFlag = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
