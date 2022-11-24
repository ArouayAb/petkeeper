package ma.ensam.petkeeper.views.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.models.PetCategory;
import ma.ensam.petkeeper.models.PostHome;
import ma.ensam.petkeeper.viewmodels.HomeViewModel;
import ma.ensam.petkeeper.views.auth.LoginActivity;
import ma.ensam.petkeeper.views.home.adapters.HomeAdapterCategory;
import ma.ensam.petkeeper.views.home.adapters.HomeAdapterPost;

public class HomeActivity extends AppCompatActivity {

    static int tabIndex = 1;
    TextView keeperTab, ownerTab ;
    ImageView search_button ;
    EditText search_edit_text;
    //ConstraintLayout home_layout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        keeperTab = findViewById(R.id.keepers_tab_id);
        ownerTab  = findViewById(R.id.owners_tab_id);
        search_button = findViewById(R.id.seach_button_id);
        search_edit_text=findViewById(R.id.search_edit_text_id);
       /* home_layout = findViewById(R.id.home_layout_id);

        home_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_edit_text.setBackgroundResource(R.drawable.search_bg_style);
            }
        });*/

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = String.valueOf(search_edit_text.getText());
                if (searchText.length() >= 3) {
                    ShowToast(searchText);
                    search_edit_text.setBackgroundResource(R.drawable.search_bg_style);
                } else {
                    search_edit_text.setBackgroundResource(R.drawable.search_bg_style_error);
                    ShowToast("min character for search : 3..");
                }
            }
        });

        keeperTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeActivity.tabIndex ==1){
                    keeperTab.setBackgroundResource(R.drawable.tab_style_active);
                    ownerTab.setBackgroundResource(R.drawable.tab_style_inactive);
                    HomeActivity.tabIndex=0;
                }

            }
        });

        ownerTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeActivity.tabIndex ==0){
                    ownerTab.setBackgroundResource(R.drawable.tab_style_active);
                    keeperTab.setBackgroundResource(R.drawable.tab_style_inactive);
                    HomeActivity.tabIndex=1;
                }
            }
        });

        RecyclerViewCategory();
        RecyclerViewPost();
    }

    private void RecyclerViewPost() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewCategoryList = findViewById(R.id.recycler_post_id);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        RecyclerView.Adapter<HomeAdapterPost.ViewHolder> adapter = new HomeAdapterPost(HomeViewModel.getAllPosts(), new HomeAdapterPost.ItemClickedListener() {
            @Override
            public void onClickItem(PostHome postHome) {
                ShowToast(postHome.getUserName()+" said : "+postHome.getDescription());
            }

            @Override
            public void onCLickSeePost(PostHome postHome) {
                ShowToast("See Post Clicked");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);

            }

            @Override
            public void onClickSeeProfile(String ownerProfileOrOwnerProfileId) {
                ShowToast("See profile Clicked");
            }

        });

        recyclerViewCategoryList.setAdapter(adapter);
    }

    private void RecyclerViewCategory() {
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewCategoryList = findViewById(R.id.recycler_category_id);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        RecyclerView.Adapter<HomeAdapterCategory.ViewModel> adapter = new HomeAdapterCategory(HomeViewModel.getAllCategories(), new HomeAdapterCategory.ItemClickedListener() {
            @Override
            public void onItemClick(PetCategory petCategory) {
                if(petCategory.isActive()){
                    int lengthOfImage = petCategory.getImg().length();
                    petCategory.setImg( petCategory.getImg().substring(0,lengthOfImage-7) );
                }else{
                    petCategory.setImg(petCategory.getImg()+"_active") ;
                }
                petCategory.setActive(!petCategory.isActive());

            }
        });

        recyclerViewCategoryList.setAdapter(adapter);
    }

    private void ShowToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}