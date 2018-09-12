package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by naveen on 8/1/15.
 */
public class InnerVideos implements Parcelable ,Serializable {

    @SerializedName("video_id")
    private String videoid;

    @SerializedName("video_file")
    private String videofile;

    @SerializedName("video_name")
    private String videoname;

    @SerializedName("video_description")
    private String videoDescription;

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getVideofile() {
        return videofile;
    }

    public void setVideofile(String videofile) {
        this.videofile = videofile;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoid);
        dest.writeString(this.videofile);
        dest.writeString(this.videoname);
        dest.writeString(this.videoDescription);
    }

    public InnerVideos() {
    }

    private InnerVideos(Parcel in) {
        this.videoid = in.readString();
        this.videofile = in.readString();
        this.videoname = in.readString();
        this.videoDescription = in.readString();
    }

    public static final Parcelable.Creator<InnerVideos> CREATOR = new Parcelable.Creator<InnerVideos>() {
        public InnerVideos createFromParcel(Parcel source) {
            return new InnerVideos(source);
        }

        public InnerVideos[] newArray(int size) {
            return new InnerVideos[size];
        }
    };
}
