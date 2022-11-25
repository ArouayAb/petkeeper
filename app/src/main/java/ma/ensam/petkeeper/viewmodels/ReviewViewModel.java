package ma.ensam.petkeeper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.Review;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;
import ma.ensam.petkeeper.repositories.ProfileRepository;
import ma.ensam.petkeeper.repositories.ReviewRepository;

public class ReviewViewModel extends AndroidViewModel {
    private final ReviewRepository repository;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ReviewRepository(application);
    }

    public void insert(Review review) {
        this.repository.insert(review);
    }

    public void update(Review review) {
        this.repository.update(review);
    }


    public void delete(Review review) {
        this.repository.delete(review);
    }

    public LiveData<Review> findReviewByIds(long temp_current_profile_id, long temp_self_profile_id) {
        return this.repository.findReviewByIds(temp_current_profile_id, temp_self_profile_id);
    }

    public void updateRatingByIds(long temp_current_profile_id, long temp_self_profile_id, int rating) {
        this.repository.updateRatingByIds(temp_current_profile_id, temp_self_profile_id, rating);
    }

    public void updateBodyByIds(long temp_current_profile_id, long temp_self_profile_id, String body) {
        this.repository.updateBodyByIds(temp_current_profile_id, temp_self_profile_id, body);
    }
}
