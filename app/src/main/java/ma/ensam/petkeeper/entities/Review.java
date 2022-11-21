package ma.ensam.petkeeper.entities;

import androidx.room.Entity;

@Entity(tableName = "reviews", primaryKeys = {"reviewerProfileId", "revieweeProfileId"})
public class Review {

    private long reviewerProfileId;

    private long revieweeProfileId;

    private int rating;

    private String body;

    public Review(long reviewerProfileId, long revieweeProfileId, int rating, String body) {
        this.reviewerProfileId = reviewerProfileId;
        this.revieweeProfileId = revieweeProfileId;
        this.rating = rating;
        this.body = body;
    }

    public long getReviewerProfileId() {
        return reviewerProfileId;
    }

    public void setReviewerProfileId(long reviewerProfileId) {
        this.reviewerProfileId = reviewerProfileId;
    }

    public long getRevieweeProfileId() {
        return revieweeProfileId;
    }

    public void setRevieweeProfileId(long revieweeProfileId) {
        this.revieweeProfileId = revieweeProfileId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
