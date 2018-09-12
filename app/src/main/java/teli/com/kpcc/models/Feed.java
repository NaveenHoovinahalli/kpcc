package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by madhuri on 5/1/15.
 */
public class Feed implements Parcelable {

    @SerializedName("news_head")
    private String newsHead;

    @SerializedName("publish_date")
    private String publishDate;

    @SerializedName("news_description")
    private String newsDescription;

    @SerializedName("news_feed_images")
    private ArrayList<FeedImage> images = new ArrayList<FeedImage>();

    @SerializedName("news_feed_image_links")
    private ArrayList<FeedImageLink> imageLinks = new ArrayList<FeedImageLink>();

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsHead() {
        return newsHead;
    }

    public void setNewsHead(String newsHead) {
        this.newsHead = newsHead;
    }

    public ArrayList<FeedImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<FeedImage> images) {
        this.images = images;
    }

    public ArrayList<FeedImageLink> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ArrayList<FeedImageLink> imageLinks) {
        this.imageLinks = imageLinks;
    }



    public Feed() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.newsHead);
        dest.writeString(this.publishDate);
        dest.writeString(this.newsDescription);
        dest.writeSerializable(this.images);
        dest.writeSerializable(this.imageLinks);
    }

    private Feed(Parcel in) {
        this.newsHead = in.readString();
        this.publishDate = in.readString();
        this.newsDescription = in.readString();
        this.images = (ArrayList<FeedImage>) in.readSerializable();
        this.imageLinks = (ArrayList<FeedImageLink>) in.readSerializable();
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        public Feed createFromParcel(Parcel source) {
            return new Feed(source);
        }

        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}
