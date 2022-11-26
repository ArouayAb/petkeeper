package ma.ensam.petkeeper.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.enums.PetSpecies;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;

@Dao
public interface RelationDao {
    @Transaction
    @Query("SELECT * FROM profiles JOIN users ON users.id = profiles.userId WHERE profiles.profileId = :id")
    LiveData<UserAndProfile> findProfileWithUserById(long id);

    @Query("SELECT * FROM offers JOIN profiles ON profiles.profileId = offers.profileCreatorId WHERE profiles.profileId = :id")
    LiveData<ProfileWithOffers> findProfileWithOffersById(long id);

    @Query("SELECT * FROM reviews JOIN profiles ON profiles.profileId = reviews.reviewerProfileId WHERE reviews.revieweeProfileId = :id")
    LiveData<List<ProfileWithReviewsOnIt>> findProfilesWithReviewsOnIt(long id);

    @Transaction
    @Query("SELECT * FROM offers o JOIN profiles p ON p.profileId = o.profileCreatorId WHERE o.type = :type  ")
    LiveData<List<ProfileAndOffer>> findAllOffersWithProfileByType(OfferType type);
}
