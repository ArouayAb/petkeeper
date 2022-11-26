package ma.ensam.petkeeper.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profiles")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    private long profileId;

    private String fullName;

    private String phoneNumber;

    private String about;

    private String profilePicUrl;

    private String country;

    private String city;

    private long userId;

    public Profile() {

    }

    public Profile(String fullName, String phoneNumber, String about,
                   String profilePicUrl, String country, String city, long userId) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.profilePicUrl = profilePicUrl;
        this.country = country;
        this.city = city;
        this.userId = userId;
    }

    public Profile(String fullName, String phoneNumber, String country, String city) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.city = city;
    }

    public Profile(long profileId, String fullName, String phoneNumber, String about, String profilePicUrl, String country, String city, long userId) {
        this.profileId = profileId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.profilePicUrl = profilePicUrl;
        this.country = country;
        this.city = city;
        this.userId = userId;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public long getId() {
        return profileId;
    }

    public void setId(long id) {
        this.profileId = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
