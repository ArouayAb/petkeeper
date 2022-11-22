package ma.ensam.petkeeper.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;
import ma.ensam.petkeeper.repositories.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository profileRepository;

    private LiveData<List<Profile>> allProfiles;

    public ProfileViewModel(Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
        allProfiles = profileRepository.getAllProfiles();
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfiles;
    }

    public void insert(Profile profile) {
        profileRepository.insert(profile);
    }

    public LiveData<Profile> findById(Long id) {
        return profileRepository.findById(id);
    }

    public LiveData<UserAndProfile> findProfileAndUserByProfileId(Long id) {
        return profileRepository.findProfileAndUserByProfileId(id);
    }

}
