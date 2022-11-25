package ma.ensam.petkeeper.models;

import ma.ensam.petkeeper.models.compositeKeys.ReviewKey;

public class ReviewProfile {
    private ReviewKey id;
    private String profileUrl;
    private String userName;
    private String reviewText;
    private int reviewStars;

    public ReviewProfile(ReviewKey id, String profileUrl, String userName, String reviewText, int reviewStars) {
        this.id = id;
        this.profileUrl = profileUrl;
        this.userName = userName;
        this.reviewText = reviewText;
        this.reviewStars = reviewStars;
    }

    public ReviewKey getId() {
        return id;
    }

    public void setId(ReviewKey id) {
        this.id = id;
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