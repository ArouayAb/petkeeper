package ma.ensam.petkeeper.repositories;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import ma.ensam.petkeeper.entities.User;

public class UserRepository {

    private static UserRepository instance;
    private UserRepository(){

    }

    public static UserRepository getInstance(Activity mContext){
        if (instance == null){
            instance = new UserRepository();
        }
        return instance;
    }

    public MutableLiveData<User> getAllUsers(){
        final MutableLiveData<User> data = new MutableLiveData<>();
        // code here to bring Data
        return data;
    }
}
