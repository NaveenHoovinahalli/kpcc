package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 4/2/15.
 */
public class Designation {

 @SerializedName("id")
   private String id;

  @SerializedName("designation")
    private String designation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
