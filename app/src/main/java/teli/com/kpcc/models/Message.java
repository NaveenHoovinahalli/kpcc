package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by madhuri on 14/1/15.
 */
public class Message implements Parcelable {

    private String id;

    @SerializedName("message_head")
    private String messageHead;

    @SerializedName("message_description")
    private String messageDescription;

    @SerializedName("insert_date")
    private String insertDate;

    @SerializedName("insert_time")
    private String insertTime;

    @SerializedName("images")
    private ArrayList<InnerImage> innerImages = new ArrayList<InnerImage>();

    @SerializedName("videos")
    private ArrayList<InnerVideos> innerVideos = new ArrayList<InnerVideos>();

    @SerializedName("party_worker")
    private String partyWorker;

    @SerializedName("constituency")
    private String constituency;

    @SerializedName("profile_pic")
    private String profilePic;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPartyWorker() {
        return partyWorker;
    }

    public void setPartyWorker(String partyWorker) {
        this.partyWorker = partyWorker;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public static Creator<Message> getCreator() {
        return CREATOR;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageHead() {
        return messageHead;
    }

    public void setMessageHead(String messageHead) {
        this.messageHead = messageHead;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public ArrayList<InnerImage> getInnerImages() {
        return innerImages;
    }

    public void setInnerImages(ArrayList<InnerImage> innerImages) {
        this.innerImages = innerImages;
    }

    public ArrayList<InnerVideos> getInnerVideos() {
        return innerVideos;
    }

    public void setInnerVideos(ArrayList<InnerVideos> innerVideos) {
        this.innerVideos = innerVideos;
    }


    public Message() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.messageHead);
        dest.writeString(this.messageDescription);
        dest.writeString(this.insertDate);
        dest.writeString(this.insertTime);
        dest.writeSerializable(this.innerImages);
        dest.writeSerializable(this.innerVideos);
        dest.writeString(this.partyWorker);
        dest.writeString(this.constituency);
        dest.writeString(this.profilePic);
    }

    private Message(Parcel in) {
        this.id = in.readString();
        this.messageHead = in.readString();
        this.messageDescription = in.readString();
        this.insertDate = in.readString();
        this.insertTime = in.readString();
        this.innerImages = (ArrayList<InnerImage>) in.readSerializable();
        this.innerVideos = (ArrayList<InnerVideos>) in.readSerializable();
        this.partyWorker = in.readString();
        this.constituency = in.readString();
        this.profilePic = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
