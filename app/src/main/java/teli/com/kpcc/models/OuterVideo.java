package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by naveen on 8/1/15.
 */
public class OuterVideo implements Parcelable {

    private  String id;

    @SerializedName("main_head")
    private String mainHead;

    @SerializedName("sub_head")
    private String subhead;

    @SerializedName("insert_datetime")
    private String insertDateTime;

    @SerializedName("videos")
    ArrayList<InnerVideos> videos=new ArrayList<InnerVideos>();

    public ArrayList<InnerVideos> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<InnerVideos> videos) {
        this.videos = videos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainHead() {
        return mainHead;
    }

    public void setMainHead(String mainHead) {
        this.mainHead = mainHead;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getInsertDateTime() {
        return insertDateTime;
    }

    public void setInsertDateTime(String insertDateTime) {
        this.insertDateTime = insertDateTime;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mainHead);
        dest.writeString(this.subhead);
        dest.writeString(this.insertDateTime);
        dest.writeSerializable(this.videos);
    }

    public OuterVideo() {
    }

    private OuterVideo(Parcel in) {
        this.id = in.readString();
        this.mainHead = in.readString();
        this.subhead = in.readString();
        this.insertDateTime = in.readString();
        this.videos = (ArrayList<InnerVideos>) in.readSerializable();
    }

    public static final Parcelable.Creator<OuterVideo> CREATOR = new Parcelable.Creator<OuterVideo>() {
        public OuterVideo createFromParcel(Parcel source) {
            return new OuterVideo(source);
        }

        public OuterVideo[] newArray(int size) {
            return new OuterVideo[size];
        }
    };
}
