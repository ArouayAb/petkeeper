package ma.ensam.petkeeper.model.repo;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

public class PostRepo {

    public MutableLiveData<JSONObject> getAllPost(){
        final MutableLiveData<JSONObject> newsData = new MutableLiveData<>();
        // code here to bring Data
        return newsData;
    }

}