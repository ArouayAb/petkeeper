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
import ma.ensam.petkeeper.daos.UserDao;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.User;

public class UserRepository {
    private final UserDao userDao;
    private LiveData<List<User>> allUsers;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Application application;


    public UserRepository(Application application) {
        this.application = application;
        AppDatabase db = AppDatabase.getInstance(application);
        this.userDao = db.userDao();
        this.allUsers = this.userDao.findAll();
    }

    public void update(User user) {
        this.executor.execute(() -> {
            this.userDao.update(user);
        });
    }

    public void delete(User user) {
        this.executor.execute(() -> {
            this.userDao.delete(user);
        });
    }

    public long insert(User user) {
        CompletableFuture<Long> id = CompletableFuture.supplyAsync(
                () -> userDao.insert(user)
        );
        try {
            return id.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public LiveData<User> login(User user){
        return this.userDao.login(user.getEmail(),user.getPassword());
    }

    public LiveData<User> findById(long id) {
        try {
            CompletableFuture<LiveData<User>> profile = CompletableFuture.supplyAsync(
                    () -> userDao.findById(id)
            );

            return profile.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
