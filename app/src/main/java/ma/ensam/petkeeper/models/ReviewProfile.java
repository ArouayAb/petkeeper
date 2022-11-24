package ma.ensam.petkeeper.models;

import java.util.Date;

public class ReviewProfile {
    private String profileUrl;
    private String userName;
    private String reviewText;
    private int reviewStars;

    public ReviewProfile(String profileUrl, String userName, String reviewText, int reviewStars) {
        this.profileUrl = profileUrl;
        this.userName = userName;
        this.reviewText = reviewText;
        this.reviewStars = reviewStars;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getReviewStars() {
        return reviewStars;
    }

    public void setReviewStars(int reviewStars) {
        this.reviewStars = reviewStars;
    }
}