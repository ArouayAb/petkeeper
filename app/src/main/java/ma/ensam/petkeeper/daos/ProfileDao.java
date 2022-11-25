package ma.ensam.petkeeper.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;

@Dao
public interface ProfileDao {

    @Insert
    long insert(Profile profile);

    @Insert
    void insertAll(Profile... profiles);

    @Update
    void update(Profile profile);


    @Delete
    void delete(Profile profile);

    @Query("SELECT * FROM profiles WHERE id = :id")
    LiveData<Profile> findById(long id);

    @Query("SELECT * FROM profiles")
    LiveData<List<Profile>> findAll();
}