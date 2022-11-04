package ma.ensam.petkeeper.view.auth;

import android.app.Activity;

import androidx.lifecycle.LiveData;

import ma.ensam.petkeeper.model.entity.User;
import ma.ensam.petkeeper.model.repo.UserRepo;

public class AuthViewModel {
        // Just example :
        // **  start **
            UserRepo userRepo ;
            Activity activity;
            public AuthViewModel(Activity mContext){
                activity = mContext;
                userRepo =  UserRepo.getInstance(activity);
            }

            public LiveData<User> requestALlUser(){
                return  userRepo.getAllUsers();
            }
        // **  end **
}
