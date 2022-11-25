package ma.ensam.petkeeper.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.concurrent.atomic.AtomicBoolean;

import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.User;
import ma.ensam.petkeeper.repositories.ProfileRepository;
import ma.ensam.petkeeper.repositories.UserRepository;

public class AuthViewModel extends AndroidViewModel {

    private final UserRepository userRepository ;
    private final ProfileRepository profileRepository;

    public AuthViewModel(Application application){
        super(application);
        userRepository =  new UserRepository(application);
        profileRepository = new ProfileRepository(application);
    }

    public LiveData<User> login(User user){
        return userRepository.login(user);
    }

    public boolean register(User user, Profile profile){
        long userId = 0, profileId = 0;

        userId = userRepository.insert(user);
        if (userId != 0) {
            profile.setUserId(userId);
            profileId = profileRepository.insert(profile);
        }
        return profileId != 0;
    }



}
