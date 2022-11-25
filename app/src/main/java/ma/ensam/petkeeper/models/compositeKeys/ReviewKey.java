package ma.ensam.petkeeper.models.compositeKeys;

import androidx.annotation.NonNull;

public class ReviewKey {
    private long revieweeId;
    private long reviewerId;

    public ReviewKey(long revieweeId, long reviewerId) {
        this.revieweeId = revieweeId;
        this.reviewerId = reviewerId;
    }

    public long getRevieweeId() {
        return revieweeId;
    }

    public void setRevieweeId(long revieweeId) {
        this.revieweeId = revieweeId;
    }

    public long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(long reviewerId) {
        this.reviewerId = reviewerId;
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + "Reviewee Id: " + revieweeId + ", Reviewer Id: " + reviewerId + "]";
    }
}
