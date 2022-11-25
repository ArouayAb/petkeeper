package ma.ensam.petkeeper.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.ProfileDao;
import ma.ensam.petkeeper.daos.RelationDao;
import ma.ensam.petkeeper.daos.ReviewDao;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.Review;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;

public class ReviewRepository {
    private final ReviewDao reviewDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Application application;


    public ReviewRepository(Application application) {
        this.application = application;
        AppDatabase db = AppDatabase.getInstance(application);
        this.reviewDao = db.reviewDao();

    }

    public void insert(Review review) {
        this.executor.execute(() -> {
            this.reviewDao.insert(review);
        });
    }

    public void update(Review review) {
        this.executor.execute(() -> {
            this.reviewDao.update(review);
        });
    }

    public void delete(Review review){
        this.executor.execute(() -> {
            this.reviewDao.delete(review);
        });
    }

    public LiveData<Review> findReviewByIds(long temp_current_profile_id, long temp_self_profile_id) {
        try {
            CompletableFuture<LiveData<Review>> review = CompletableFuture.supplyAsync(
                    () -> reviewDao.findByIds(temp_current_profile_id, temp_self_profile_id)
            );

            return review.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateRatingByIds(long temp_current_profile_id, long temp_self_profile_id, int rating) {
        this.executor.execute(() -> {
            this.reviewDao.updateRatingByIds(temp_current_profile_id, temp_self_profile_id, rating);
        });
    }

    public void updateBodyByIds(long temp_current_profile_id, long temp_self_profile_id, String body) {
        this.executor.execute(() -> {
            this.reviewDao.updateBodyByIds(temp_current_profile_id, temp_self_profile_id, body);
        });
    }
}
