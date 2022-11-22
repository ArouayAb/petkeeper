package ma.ensam.petkeeper.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.OfferDao;
import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.relations.OfferWithCreator;

public class OfferRepository {
    private OfferDao offerDao;

    private LiveData<List<Offer>> allOffers;

    public OfferRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        offerDao = appDatabase.offerDao();
        allOffers = offerDao.findAll();
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

//    public LiveData<OfferWithCreator> findOfferAndCreatorByOfferId(long id) {
//        CompletableFuture<LiveData<OfferWithCreator>> offer = CompletableFuture.supplyAsync(
//                () -> offerDao.findOfferAndCreatorByOfferId(id)
//        );
//        try {
//            return offer.get();
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void insert(Offer offer) {
        new Thread(() -> {
            offerDao.insert(offer);
        }).start();
    }
}
