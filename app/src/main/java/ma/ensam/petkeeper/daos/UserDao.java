package ma.ensam.petkeeper.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ma.ensam.petkeeper.entities.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Insert
    void insertAll(User... users);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE id = :id")
    User findById(long id);
}
