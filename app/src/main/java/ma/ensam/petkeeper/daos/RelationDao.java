package ma.ensam.petkeeper.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;

@Dao
public interface RelationDao {
    @Transaction
    @Query("SELECT * FROM profiles JOIN users ON users.id = profiles.userId WHERE users.id = :id")
    LiveData<UserAndProfile> findProfileWithUserById(long id);

    @Query("SELECT * FROM offers JOIN profiles ON profiles.id = offers.profileCreatorId WHERE profiles.id = :id")
    LiveData<ProfileWithOffers> findProfileWithOffersById(long id);

    @Query("SELECT * FROM reviews JOIN profiles ON profiles.id = reviews.reviewerProfileId WHERE reviews.revieweeProfileId = :id")
    LiveData<List<ProfileWithReviewsOnIt>> findProfilesWithReviewsOnIt(long id);
}