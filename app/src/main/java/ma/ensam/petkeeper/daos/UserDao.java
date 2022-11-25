package ma.ensam.petkeeper.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.User;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Insert
    void insertAll(User... users);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<User> findById(long id);

    @Query("SELECT * FROM Users WHERE email LIKE :email AND password LIKE :password")
    LiveData<User> login(String email, String password);

    @Query("SELECT * FROM users")
    LiveData<List<User>> findAll();
}