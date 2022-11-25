package ma.ensam.petkeeper.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;

@Dao
public interface OfferDao {

    @Insert
    void insert(Offer offer);

    @Insert
    void insertAll(Offer... offers);

    @Update
    void update(Offer offer);

    @Delete
    void delete(Offer offer);

    @Query("SELECT * FROM offers WHERE id = :id")
    Offer findById(long id);

    @Query("SELECT * FROM offers WHERE type = :type")
    LiveData<List<Offer>> findOffersByType(String type);

}
