package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by naveen on 12/1/15.
 */
public class PollNotParticipatedFeed implements Parcelable,Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("poll_question")
    String pollQuestion;

    @SerializedName("date")
    String date;

    @SerializedName("poll_choice")
    ArrayList<PollChoice> pollChoices=new ArrayList<PollChoice>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<PollChoice> getPollChoices() {
        return pollChoices;
    }

    public void setPollChoices(ArrayList<PollChoice> pollChoices) {
        this.pollChoices = pollChoices;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.pollQuestion);
        dest.writeString(this.date);
        dest.writeSerializable(this.pollChoices);
    }

    public PollNotParticipatedFeed() {
    }

    private PollNotParticipatedFeed(Parcel in) {
        this.id = in.readString();
        this.pollQuestion = in.readString();
        this.date = in.readString();
        this.pollChoices = (ArrayList<PollChoice>) in.readSerializable();
    }

    public static final Parcelable.Creator<PollNotParticipatedFeed> CREATOR = new Parcelable.Creator<PollNotParticipatedFeed>() {
        public PollNotParticipatedFeed createFromParcel(Parcel source) {
            return new PollNotParticipatedFeed(source);
        }

        public PollNotParticipatedFeed[] newArray(int size) {
            return new PollNotParticipatedFeed[size];
        }
    };
}
