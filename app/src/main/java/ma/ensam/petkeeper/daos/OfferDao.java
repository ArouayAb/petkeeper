package ma.ensam.petkeeper.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;

@Dao
public interface OfferDao {

    @Insert
    long insert(Offer offer);

    @Insert
    void insertAll(Offer... offers);

    @Update
    void update(Offer offer);

    @Delete
    void delete(Offer offer);

    @Query("SELECT * FROM offers WHERE offerId = :id")
    LiveData<Offer> findById(long id);

    @Query("SELECT * FROM offers")
    LiveData<List<Offer>> findAll();

    @Transaction
    @Query("SELECT * FROM offers JOIN profiles ON offers.profileCreatorId = profiles.profileId WHERE offers.offerId = :id")
    LiveData<ProfileAndOffer> findOfferAndProfileByOfferId(long id);
}