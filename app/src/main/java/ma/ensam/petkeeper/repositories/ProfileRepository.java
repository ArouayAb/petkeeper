package ma.ensam.petkeeper.repositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.ProfileDao;
import ma.ensam.petkeeper.daos.RelationDao;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.Review;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;

public class ProfileRepository {
    private final ProfileDao profileDao;
    public final RelationDao relationDao;
    private LiveData<List<Profile>> allProfiles;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Application application;

    public ProfileRepository(Application application) {
        this.application = application;
        AppDatabase db = AppDatabase.getInstance(application);
        this.profileDao = db.profileDao();
        this.relationDao = db.relationDao();
        this.allProfiles = this.profileDao.findAll();
    }

    public long insert(Profile profile) {
        CompletableFuture<Long> id = CompletableFuture.supplyAsync(
                () -> profileDao.insert(profile)
        );
        try {
            return id.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void update(Profile profile) {
        this.executor.execute(() -> {
            this.profileDao.update(profile);
        });
    }

    public void updateProfilePicUrlById(long id, String profilePicUrl) {
        this.executor.execute(() -> {
            this.profileDao.updateProfilePicUrlById(id, profilePicUrl);
        });
    }

    public void delete(Profile profile){
        this.executor.execute(() -> {
            this.profileDao.delete(profile);
        });
    }

    public LiveData<Profile> findById(long id) {
        try {
            CompletableFuture<LiveData<Profile>> profile = CompletableFuture.supplyAsync(
                    () -> profileDao.findById(id)
            );

            return profile.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfiles;
    }

    public LiveData<UserAndProfile> findProfileWithUserById(long id) {
        try {
            CompletableFuture<LiveData<UserAndProfile>> userAndProfile = CompletableFuture.supplyAsync(
                    () -> relationDao.findProfileWithUserById(id)
            );

            return userAndProfile.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<ProfileWithOffers> findProfileWithOffersById(long id) {
        try {
            CompletableFuture<LiveData<ProfileWithOffers>> profileWithOffers = CompletableFuture.supplyAsync(
                    () -> relationDao.findProfileWithOffersById(id)
            );

            return profileWithOffers.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<ProfileWithReviewsOnIt>> findProfilesWithReviewsOnIt(long id) {
        try {
            CompletableFuture<LiveData<List<ProfileWithReviewsOnIt>>> profileWithReviews = CompletableFuture.supplyAsync(
                    () -> relationDao.findProfilesWithReviewsOnIt(id)
            );

            return profileWithReviews.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
