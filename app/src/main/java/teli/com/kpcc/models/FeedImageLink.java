package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by madhuri on 5/1/15.
 */
public class FeedImageLink implements Serializable {

    @SerializedName("image_link")
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
