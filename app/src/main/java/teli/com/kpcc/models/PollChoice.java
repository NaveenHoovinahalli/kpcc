package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by naveen on 12/1/15.
 */
public class PollChoice implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("choice")
    String choice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
