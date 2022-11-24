package ma.ensam.petkeeper.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.Review;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;
import ma.ensam.petkeeper.repositories.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel {
    private final ProfileRepository repository;
    private LiveData<List<Profile>> allProfiles;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProfileRepository(application);
        allProfiles = this.repository.getAllProfiles();
    }

    public void insert(Profile profile) {
        this.repository.insert(profile);
    }

    public void update(Profile profile) {
        this.repository.update(profile);
    }
    public void updateProfilePicUrlById(long id, String profilePicUrl) {
        this.repository.updateProfilePicUrlById(id, profilePicUrl);
    }


    public void delete(Profile profile) {
        this.repository.delete(profile);
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return this.repository.getAllProfiles();
    }

    public LiveData<Profile> findById(long id) {
        return this.repository.findById(id);
    }

    public LiveData<UserAndProfile> findProfileWithUserById(long id) {
        MutableLiveData<UserAndProfile> ld = new MutableLiveData<>();
        return this.repository.findProfileWithUserById(id);
    }

    public LiveData<ProfileWithOffers> findProfileWithOffersById(long id) {
        return this.repository.findProfileWithOffersById(id);
    }

    public LiveData<List<ProfileWithReviewsOnIt>> findProfilesWithReviewsOnIt(long id) {
        return this.repository.findProfilesWithReviewsOnIt(id);
    }
}
