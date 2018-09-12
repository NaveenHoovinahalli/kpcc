package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madhuri on 30/12/14.
 */
public class Constituency {

    @SerializedName("constituency_name")
    private String constituencyName;

    public String getConstituencyName() {
        return constituencyName;
    }

    public void setConstituencyName(String constituencyName) {
        this.constituencyName = constituencyName;
    }
}
