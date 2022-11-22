package ma.ensam.petkeeper.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.ProfileDao;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;

public class ProfileRepository {
    private ProfileDao profileDao;

    private LiveData<List<Profile>> allProfiles;

    public ProfileRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        profileDao = appDatabase.profileDao();
        allProfiles = profileDao.findAll();
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfiles;
    }

    public LiveData<Profile> findById(long id) {
        CompletableFuture<LiveData<Profile>> profile = CompletableFuture.supplyAsync(
                () -> profileDao.findById(id)
        );
        try {
            return profile.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<UserAndProfile> findProfileAndUserByProfileId(long id) {
        CompletableFuture<LiveData<UserAndProfile>> profile = CompletableFuture.supplyAsync(
                () -> profileDao.findProfileAndUserByProfileId(id)
        );
        try {
            return profile.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Profile Profile) {
        new Thread(() -> {
            profileDao.insert(Profile);
        }).start();
    }
}
