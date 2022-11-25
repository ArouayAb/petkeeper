package ma.ensam.petkeeper.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
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

    public long insert(Offer offer) {
        return offerRepository.insert(offer);
    }

    public LiveData<Offer> findById(long id) {
        return offerRepository.findById(id);
    }

    public LiveData<ProfileAndOffer> findProfileAndOfferByOfferId(long id) {
        return offerRepository.findProfileAndOfferByOfferId(id);
    }

}
