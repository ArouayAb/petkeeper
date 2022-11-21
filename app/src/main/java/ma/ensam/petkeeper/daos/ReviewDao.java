package ma.ensam.petkeeper.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ma.ensam.petkeeper.entities.Review;

@Dao
public interface ReviewDao {

    @Insert
    void insert(Review review);

    @Insert
    void insertAll(Review... reviews);

    @Update
    void update(Review review);

    @Delete
    void delete(Review review);

    @Query("SELECT * FROM reviews WHERE reviewerProfileId = :reviewerId AND revieweeProfileId = :revieweeId")
    Review findByIds(long reviewerId, long revieweeId);
}
