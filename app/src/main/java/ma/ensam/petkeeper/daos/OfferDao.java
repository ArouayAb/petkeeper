package ma.ensam.petkeeper.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ma.ensam.petkeeper.entities.Offer;

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
}
