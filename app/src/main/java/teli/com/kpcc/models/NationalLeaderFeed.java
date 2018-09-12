package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by naveen on 8/1/15.
 */
public class NationalLeaderFeed implements Parcelable ,Serializable {

    @SerializedName("leader_id")
    private String leaderId;

    @SerializedName("leader_name")
    private String leaderName;

    @SerializedName("designation")
    private String designation;

    @SerializedName("leader_thumbnail_image")
    private String leaderThumbnailImage;

    @SerializedName("leader_header_image")
    private String leaderHeaderImage;

    @SerializedName("leader_type")
    private String leaderType;

    @SerializedName("description")
    private String description;

    @SerializedName("facebook_link")
    private String  facebookLink;

    @SerializedName("twitter_link")
    private String twitterLink;

    @SerializedName("youtube_link")
    private String youtubeLink;

    @SerializedName("website_link")
    private String websiteLink;

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public static Creator<NationalLeaderFeed> getCreator() {
        return CREATOR;
    }

    @SerializedName("images")
    ArrayList<InnerImage> images=new ArrayList<InnerImage>();

    @SerializedName("videos")
    ArrayList<InnerVideos> videos=new ArrayList<InnerVideos>();

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLeaderThumbnailImage() {
        return leaderThumbnailImage;
    }

    public void setLeaderThumbnailImage(String leaderThumbnailImage) {
        this.leaderThumbnailImage = leaderThumbnailImage;
    }

    public String getLeaderHeaderImage() {
        return leaderHeaderImage;
    }

    public void setLeaderHeaderImage(String leaderHeaderImage) {
        this.leaderHeaderImage = leaderHeaderImage;
    }

    public String getLeaderType() {
        return leaderType;
    }

    public void setLeaderType(String leaderType) {
        this.leaderType = leaderType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public ArrayList<InnerImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<InnerImage> images) {
        this.images = images;
    }

    public ArrayList<InnerVideos> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<InnerVideos> videos) {
        this.videos = videos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.leaderId);
        dest.writeString(this.leaderName);
        dest.writeString(this.designation);
        dest.writeString(this.leaderThumbnailImage);
        dest.writeString(this.leaderHeaderImage);
        dest.writeString(this.leaderType);
        dest.writeString(this.description);
        dest.writeString(this.facebookLink);
        dest.writeString(this.twitterLink);
        dest.writeString(this.youtubeLink);
        dest.writeSerializable(this.images);
        dest.writeSerializable(this.videos);
        dest.writeString(this.websiteLink);
}
    public NationalLeaderFeed() {
    }

    private NationalLeaderFeed(Parcel in) {
        this.leaderId = in.readString();
        this.leaderName = in.readString();
        this.designation = in.readString();
        this.leaderThumbnailImage = in.readString();
        this.leaderHeaderImage = in.readString();
        this.leaderType = in.readString();
        this.description = in.readString();
        this.facebookLink = in.readString();
        this.twitterLink = in.readString();
        this.youtubeLink = in.readString();
        this.images = (ArrayList<InnerImage>) in.readSerializable();
        this.videos = (ArrayList<InnerVideos>) in.readSerializable();
this.websiteLink=in.readString();
}
    public static final Parcelable.Creator<NationalLeaderFeed> CREATOR = new Parcelable.Creator<NationalLeaderFeed>() {
        public NationalLeaderFeed createFromParcel(Parcel source) {
            return new NationalLeaderFeed(source);
        }

        public NationalLeaderFeed[] newArray(int size) {
            return new NationalLeaderFeed[size];
        }
    };
}
