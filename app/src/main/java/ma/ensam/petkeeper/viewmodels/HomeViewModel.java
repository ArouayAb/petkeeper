package ma.ensam.petkeeper.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;
import java.util.List;

import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.models.PetCategory;
import ma.ensam.petkeeper.repositories.OfferRepo;

public class HomeViewModel  extends AndroidViewModel {
    private final OfferRepo repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new OfferRepo(application);
    }

    public LiveData<List<ProfileAndOffer>> findAllOffersWithProfileByType(OfferType type) {
        return this.repository.findAllOffersWithProfileByType(type);
    }


    public  static ArrayList<PetCategory> getAllCategories (){
        ArrayList<PetCategory> categories = new ArrayList<PetCategory>() ;

        categories.add(new PetCategory("All","all_active"));
        categories.add(new PetCategory("Cat","cat"));
        categories.add(new PetCategory("Dog","dog"));
        categories.add(new PetCategory("Fish","fish"));
        categories.add(new PetCategory("Bird","bird"));
        categories.add(new PetCategory("Turtle","turtle"));
        return categories;
    }

    /*public static ArrayList<HomeOffers> getAllPosts (){
        ArrayList<HomeOffers> posts = new ArrayList<HomeOffers>();
        posts.add(new HomeOffers("Hamza Daiz","dog","canish","22-Nov-2022","25-Nov-2022",
                "Looking for someone who will keep my dog Looking for someone who will keep my dog","female","3 days" ));
        posts.add(new HomeOffers("Rabia Jomala","cat","ross","11-Des-2022","21-des-2022",
                "Please someone to keep my cat","male","10 days" ));
        posts.add(new HomeOffers("Fouad Khalil","fish","golden","08-Des-2022","22-des-2022",
                "Please someone to keep my fish","female","2 weeks" ));

        return  posts;
    }*/

}
