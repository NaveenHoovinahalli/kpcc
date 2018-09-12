package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madhuri on 5/1/15.
 */
public class Quote {

    @SerializedName("quote_text")
    private String qoute;

    @SerializedName("quoted_by")
    private String quotedBy;

    public String getQoute() {
        return qoute;
    }

    public void setQoute(String qoute) {
        this.qoute = qoute;
    }

    public String getQuotedBy() {
        return quotedBy;
    }

    public void setQuotedBy(String quotedBy) {
        this.quotedBy = quotedBy;
    }
}
