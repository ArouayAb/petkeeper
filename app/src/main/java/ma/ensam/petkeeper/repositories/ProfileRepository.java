package ma.ensam.petkeeper.repositories;

import android.app.Application;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.ProfileDao;
import ma.ensam.petkeeper.entities.Profile;

public class ProfileRepository {
    private final ProfileDao profileDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ProfileRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.profileDao = db.profileDao();
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

}