package ma.ensam.petkeeper.repositories;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import ma.ensam.petkeeper.entities.User;

public class UserRepo {

    private static UserRepo instance;
    private UserRepo(){

    }

    public static UserRepo getInstance(Activity mContext){
        if (instance == null){
            instance = new UserRepo();
        }
        return instance;
    }

    public MutableLiveData<User> getAllUsers(){
        final MutableLiveData<User> data = new MutableLiveData<>();
        // code here to bring Data
        return data;
    }
}
