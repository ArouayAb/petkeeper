package ma.ensam.petkeeper.repositories;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

public class PostRepository {

    public MutableLiveData<JSONObject> getAllPost(){
        final MutableLiveData<JSONObject> newsData = new MutableLiveData<>();
        // code here to bring Data
        return newsData;
    }

}
