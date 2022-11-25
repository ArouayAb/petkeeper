package ma.ensam.petkeeper.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.RelationDao;
import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.relations.OfferWithProfile;

public class OfferRepo {
    private final RelationDao relationDao;

    public OfferRepo(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.relationDao = db.relationDao();
    }

    public MutableLiveData<JSONObject> getAllPost(){
        final MutableLiveData<JSONObject> newsData = new MutableLiveData<>();
        // code here to bring Data
        return newsData;
    }

    public LiveData<List<OfferWithProfile>> getOfferByType(OfferType type){
        CompletableFuture<LiveData<List<OfferWithProfile>>> offer = CompletableFuture.supplyAsync(
                () -> this.relationDao.findOfferWithProfileByType(type)
        );
        try {

            return offer.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


}
