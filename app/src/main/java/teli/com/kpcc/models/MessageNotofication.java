package teli.com.kpcc.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naveen on 20/1/15.
 */
public class MessageNotofication {

    @SerializedName("message_text")
    String messageText;

    @SerializedName("message_type")
    String messageType;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {

        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
