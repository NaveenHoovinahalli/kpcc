package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by madhuri on 6/1/15.
 */
public class InnerImage implements Parcelable,Serializable {

    @SerializedName("image_id")
    private String imageId;

    @SerializedName("image_file")
    private String imageFile;

    @SerializedName("image_name")
    private String imageName;

    @SerializedName("image_description")
    private String imageDescription;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageId);
        dest.writeString(this.imageFile);
        dest.writeString(this.imageName);
        dest.writeString(this.imageDescription);
    }

    public InnerImage() {
    }

    private InnerImage(Parcel in) {
        this.imageId = in.readString();
        this.imageFile = in.readString();
        this.imageName = in.readString();
        this.imageDescription = in.readString();
    }

    public static final Parcelable.Creator<InnerImage> CREATOR = new Parcelable.Creator<InnerImage>() {
        public InnerImage createFromParcel(Parcel source) {
            return new InnerImage(source);
        }

        public InnerImage[] newArray(int size) {
            return new InnerImage[size];
        }
    };
}
