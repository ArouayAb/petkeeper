package ma.ensam.petkeeper.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.OfferDao;
import ma.ensam.petkeeper.daos.RelationDao;
import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;

public class OfferRepository {
    private final OfferDao offerDao;
    private final RelationDao relationDao;

    private LiveData<List<Offer>> allOffers;

    public OfferRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        offerDao = appDatabase.offerDao();
        this.relationDao = appDatabase.relationDao();
        allOffers = offerDao.findAll();
    }

    public LiveData<List<ProfileAndOffer>> findAllOffersWithProfileByType(OfferType type){
        CompletableFuture<LiveData<List<ProfileAndOffer>>> offer = CompletableFuture.supplyAsync(
                () -> this.relationDao.findAllOffersWithProfileByType(type)
        );
        try {

            return offer.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<Offer>> getAllOffers() {
        return allOffers;
    }

    public LiveData<Offer> findById(long id) {
        CompletableFuture<LiveData<Offer>> offer = CompletableFuture.supplyAsync(
                () -> offerDao.findById(id)
        );
        try {
            return offer.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<ProfileAndOffer> findProfileAndOfferByOfferId(long id) {
        CompletableFuture<LiveData<ProfileAndOffer>> pwo = CompletableFuture.supplyAsync(
                () -> offerDao.findOfferAndProfileByOfferId(id)
        );
        try {
            return pwo.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long insert(Offer offer) {
        CompletableFuture<Long> id = CompletableFuture.supplyAsync(
                () -> offerDao.insert(offer)
        );
        try {
            return id.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
