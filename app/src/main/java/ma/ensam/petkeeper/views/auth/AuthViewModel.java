package ma.ensam.petkeeper.views.auth;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import ma.ensam.petkeeper.entities.User;
import ma.ensam.petkeeper.repositories.UserRepository;

public class AuthViewModel {
        // Just example :
        // **  start **
            UserRepository userRepo ;
            Activity activity;
            public AuthViewModel(Activity mContext){
                activity = mContext;
                userRepo =  UserRepository.getInstance(activity);
            }

            public LiveData<User> requestALlUser(){
                return  userRepo.getAllUsers();
            }
        // **  end **
}
