package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by madhuri on 5/1/15.
 */
public class HomeImages {

    @SerializedName("leaders")
    ArrayList<EventImage> leaderImages = new ArrayList<EventImage>();

    @SerializedName("events")
    ArrayList<EventImage> eventImages = new ArrayList<EventImage>();

    public ArrayList<EventImage> getLeaderImages() {
        return leaderImages;
    }

    public void setLeaderImages(ArrayList<EventImage> leaderImages) {
        this.leaderImages = leaderImages;
    }

    public ArrayList<EventImage> getEventImages() {
        return eventImages;
    }

    public void setEventImages(ArrayList<EventImage> eventImages) {
        this.eventImages = eventImages;
    }
}
