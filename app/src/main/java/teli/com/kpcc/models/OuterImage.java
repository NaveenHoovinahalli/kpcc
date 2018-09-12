package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by madhuri on 6/1/15.
 */
public class OuterImage implements Parcelable {

    private String id;

    @SerializedName("main_head")
    private String mainHead;

    @SerializedName("sub_head")
    private String subHead;

    @SerializedName("insert_datetime")
    private String insertDateTime;

    @SerializedName("images")
    ArrayList<InnerImage> images = new ArrayList<InnerImage>();

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

    public String getSubHead() {
        return subHead;
    }

    public void setSubHead(String subHead) {
        this.subHead = subHead;
    }

    public String getInsertDateTime() {
        return insertDateTime;
    }

    public void setInsertDateTime(String insertDateTime) {
        this.insertDateTime = insertDateTime;
    }

    public ArrayList<InnerImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<InnerImage> images) {
        this.images = images;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mainHead);
        dest.writeString(this.subHead);
        dest.writeString(this.insertDateTime);
        dest.writeSerializable(this.images);
    }

    public OuterImage() {
    }

    private OuterImage(Parcel in) {
        this.id = in.readString();
        this.mainHead = in.readString();
        this.subHead = in.readString();
        this.insertDateTime = in.readString();
        this.images = (ArrayList<InnerImage>) in.readSerializable();
    }

    public static final Parcelable.Creator<OuterImage> CREATOR = new Parcelable.Creator<OuterImage>() {
        public OuterImage createFromParcel(Parcel source) {
            return new OuterImage(source);
        }

        public OuterImage[] newArray(int size) {
            return new OuterImage[size];
        }
    };
}
