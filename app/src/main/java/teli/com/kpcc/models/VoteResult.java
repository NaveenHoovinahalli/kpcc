package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 14/1/15.
 */
public class VoteResult {

    @SerializedName("vote_count")
    String voteCount;

    @SerializedName("choice")
    String choice;

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
