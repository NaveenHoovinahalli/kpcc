package teli.com.kpcc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madhuri on 4/1/15.
 */
public class User implements Parcelable {
    private String name;

    @SerializedName("mobile_number")
    private String mobileNo;

    @SerializedName("date_of_birth")
    private String dob;

    private String constituency;

    private String email;

    @SerializedName("mobile_make")
    private String mobileMake;

    @SerializedName("mobile_model")
    private String mobileModel;

    @SerializedName("push_notification_id")
    private String pushNotificationId;

    @SerializedName("os_type")
    private String osType;

    @SerializedName("imei_number")
    private String imeiNumber;

    @SerializedName("profile_picture")
    private String profilePicture;

    @SerializedName("designation")
    private String designation;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public String getEmail() {
        return email == null? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileMake() {
        return mobileMake;
    }

    public void setMobileMake(String mobileMake) {
        this.mobileMake = mobileMake;
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }

    public String getPushNotificationId() {
        return pushNotificationId;
    }

    public void setPushNotificationId(String pushNotificationId) {
        this.pushNotificationId = pushNotificationId;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getProfilePicture() {
        return profilePicture == null? "" : profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mobileNo);
        dest.writeString(this.dob);
        dest.writeString(this.constituency);
        dest.writeString(this.email);
        dest.writeString(this.mobileMake);
        dest.writeString(this.mobileModel);
        dest.writeString(this.pushNotificationId);
        dest.writeString(this.osType);
        dest.writeString(this.imeiNumber);
        dest.writeString(this.profilePicture);
        dest.writeString(this.designation);
    }

    public User() {
    }

    private User(Parcel in) {
        this.name = in.readString();
        this.mobileNo = in.readString();
        this.dob = in.readString();
        this.constituency = in.readString();
        this.email = in.readString();
        this.mobileMake = in.readString();
        this.mobileModel = in.readString();
        this.pushNotificationId = in.readString();
        this.osType = in.readString();
        this.imeiNumber = in.readString();
        this.profilePicture = in.readString();
        this.designation=in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
