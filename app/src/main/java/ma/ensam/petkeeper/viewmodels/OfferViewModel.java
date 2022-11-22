package ma.ensam.petkeeper.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.relations.OfferWithCreator;
import ma.ensam.petkeeper.repositories.OfferRepository;

public class OfferViewModel extends AndroidViewModel {

    private OfferRepository offerRepository;

    private LiveData<List<Offer>> allOffers;

    public OfferViewModel(Application application) {
        super(application);
        offerRepository = new OfferRepository(application);
        allOffers = offerRepository.getAllOffers();
    }

    public LiveData<List<Offer>> getAllOffers() {
        return allOffers;
    }

    public void insert(Offer offer) {
        offerRepository.insert(offer);
    }

    public LiveData<Offer> findById(Long id) {
        return offerRepository.findById(id);
    }

    public LiveData<OfferWithCreator> findOfferAndCreatorByOfferId(Long id) {
        return offerRepository.findOfferAndCreatorByOfferId(id);
    }

}
