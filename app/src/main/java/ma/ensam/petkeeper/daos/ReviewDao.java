package ma.ensam.petkeeper.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import ma.ensam.petkeeper.entities.Review;

@Dao
public interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Review review);

    @Insert
    void insertAll(Review... reviews);

    @Update
    void update(Review review);

    @Delete
    void delete(Review review);

    @Query("SELECT * FROM reviews WHERE revieweeProfileId = :reviewerId AND reviewerProfileId = :revieweeId")
    LiveData<Review> findByIds(long reviewerId, long revieweeId);

    @Query("REPLACE INTO reviews(revieweeProfileId, reviewerProfileid, rating, body) VALUES(" +
            ":temp_current_profile_id," +
            ":temp_self_profile_id," +
            ":rating," +
            "(SELECT body FROM reviews WHERE revieweeProfileId = :temp_current_profile_id AND reviewerProfileId = :temp_self_profile_id)" +
            ")")
    void updateRatingByIds(long temp_current_profile_id, long temp_self_profile_id, int rating);

    @Query("REPLACE INTO reviews(revieweeProfileId, reviewerProfileid, rating, body) VALUES(" +
            ":temp_current_profile_id," +
            ":temp_self_profile_id," +
            "(SELECT rating FROM reviews WHERE revieweeProfileId = :temp_current_profile_id AND reviewerProfileId = :temp_self_profile_id)," +
            ":body" +
            ")")
    void updateBodyByIds(long temp_current_profile_id, long temp_self_profile_id, String body);
}
