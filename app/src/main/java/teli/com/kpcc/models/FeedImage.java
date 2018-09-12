package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by madhuri on 5/1/15.
 */
public class FeedImage implements Serializable {
    @SerializedName("image")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
