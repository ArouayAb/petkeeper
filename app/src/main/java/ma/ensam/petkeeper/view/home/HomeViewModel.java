package ma.ensam.petkeeper.view.home;

import java.util.ArrayList;
import java.util.List;

import ma.ensam.petkeeper.model.entity.PetCategory;
import ma.ensam.petkeeper.model.entity.PostHome;

public class HomeViewModel {

    public  static ArrayList<PetCategory> getAllCategories (){
        ArrayList<PetCategory> categories = new ArrayList<PetCategory>() ;

        categories.add(new PetCategory("All","all_active",true));
        categories.add(new PetCategory("Cat","cat",false));
        categories.add(new PetCategory("Dog","dog",false));
        categories.add(new PetCategory("Fish","fish",false));
        categories.add(new PetCategory("Bird","bird",false));
        categories.add(new PetCategory("Turtle","turtle",false));
        return categories;
    }

    public static ArrayList<PostHome> getAllPosts (){
        ArrayList<PostHome> posts = new ArrayList<PostHome>();
        posts.add(new PostHome("Hamza Daiz","dog","canish","22-Nov-2022","25-Nov-2022",
                "Looking for someone who will keep my dog Looking for someone who will keep my dog","female","3 days" ));
        posts.add(new PostHome("Rabia Jomala","cat","ross","11-Des-2022","21-des-2022",
                "Please someone to keep my cat","male","10 days" ));
        posts.add(new PostHome("Fouad Khalil","fish","golden","08-Des-2022","22-des-2022",
                "Please someone to keep my fish","female","2 weeks" ));

        return  posts;
    }
}
