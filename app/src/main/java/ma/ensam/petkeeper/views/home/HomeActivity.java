package ma.ensam.petkeeper.views.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.models.HomeOffers;
import ma.ensam.petkeeper.models.PetCategory;
import ma.ensam.petkeeper.viewmodels.HomeViewModel;
import ma.ensam.petkeeper.views.auth.LoginActivity;
import ma.ensam.petkeeper.views.home.adapters.HomeAdapterCategory;
import ma.ensam.petkeeper.views.home.adapters.HomeAdapterPost;
import ma.ensam.petkeeper.views.profile.ProfileActivity;

public class HomeActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    static int tabIndex = 1;
    TextView keeperTab, ownerTab ;
    ImageView search_button ;
    EditText search_edit_text;
    private ArrayList<HomeOffers> ownerOffers = new ArrayList<>();
    private ArrayList<HomeOffers> keeperOffers = new ArrayList<>();
    private HomeAdapterPost recyclerViewOfferAdapter;
    private HomeAdapterCategory recyclerViewCategoryAdapter;
    private List<String> petSpecies= new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        keeperTab = findViewById(R.id.keepers_tab_id);
        ownerTab  = findViewById(R.id.owners_tab_id);
        search_button = findViewById(R.id.seach_button_id);
        search_edit_text=findViewById(R.id.search_edit_text_id);
        petSpecies.add("All");

        this.homeViewModel =  ViewModelProviders.of(this).get(HomeViewModel.class);

        recyclerViewCategory();
        recyclerViewPost(this.ownerOffers);

        this.homeViewModel.findAllOffersWithProfileByType(OfferType.OWNER).observe(this, offersWithProfile -> {
            if(offersWithProfile == null) return;

            this.ownerOffers = (ArrayList<HomeOffers>) offersWithProfile.stream()
                    .map((offerWithProfile) ->
                            new HomeOffers(
                                    offerWithProfile.offer.getId(),
                                    offerWithProfile.profile.getId(),
                                    offerWithProfile.profile.getFullName(),
                                    offerWithProfile.offer.getDescription(),
                                    offerWithProfile.offer.getTitle(),
                                    offerWithProfile.offer.getPet(),
                                    offerWithProfile.offer.getType(),
                                    offerWithProfile.offer.getFromDate(),
                                    offerWithProfile.offer.getToDate()

                            ))
                    .collect(Collectors.toList());
            recyclerViewOfferAdapter.updateRecyclerView(this.ownerOffers);
        } );
        this.homeViewModel.findAllOffersWithProfileByType(OfferType.KEEPER).observe(this, offersWithProfile -> {
            if(offersWithProfile == null) return;

            this.keeperOffers = (ArrayList<HomeOffers>) offersWithProfile.stream()
                    .map((offerWithProfile) ->
                            new HomeOffers(
                                    offerWithProfile.offer.getId(),
                                    offerWithProfile.profile.getId(),
                                    offerWithProfile.profile.getFullName(),
                                    offerWithProfile.offer.getDescription(),
                                    offerWithProfile.offer.getTitle(),
                                    offerWithProfile.offer.getPet(),
                                    offerWithProfile.offer.getType(),
                                    offerWithProfile.offer.getFromDate(),
                                    offerWithProfile.offer.getToDate()

                            ))
                    .collect(Collectors.toList());
        } );

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = String.valueOf(search_edit_text.getText());
                if (searchText.length() >= 3) {
                    search_edit_text.setBackgroundResource(R.drawable.search_bg_style);
                    if(HomeActivity.tabIndex ==0){
                        HomeActivity.this.recyclerViewOfferAdapter.updateRecyclerView(keeperOffers);
                        HomeActivity.this.recyclerViewOfferAdapter.searchRecycleView(searchText);
                    }else{
                        HomeActivity.this.recyclerViewOfferAdapter.updateRecyclerView(ownerOffers);
                        HomeActivity.this.recyclerViewOfferAdapter.searchRecycleView(searchText);
                    }

                } else {
                    search_edit_text.setBackgroundResource(R.drawable.search_bg_style_error);
                    showToast("min characters 3..");
                }
            }
        });

        keeperTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeActivity.tabIndex ==1){
                    String searchText = String.valueOf(search_edit_text.getText());
                    keeperTab.setBackgroundResource(R.drawable.tab_style_active);
                    ownerTab.setBackgroundResource(R.drawable.tab_style_inactive);
                    HomeActivity.tabIndex = 0;
                    HomeActivity.this.recyclerViewOfferAdapter.updateRecyclerView(keeperOffers);
                    HomeActivity.this.recyclerViewOfferAdapter.filterRecycleView(petSpecies);
                    if (searchText.length() >= 3) {
                        HomeActivity.this.recyclerViewOfferAdapter.searchRecycleView(searchText);
                    }
                }
            }
        });

        ownerTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeActivity.tabIndex ==0){
                    String searchText = String.valueOf(search_edit_text.getText());
                    ownerTab.setBackgroundResource(R.drawable.tab_style_active);
                    keeperTab.setBackgroundResource(R.drawable.tab_style_inactive);
                    HomeActivity.tabIndex = 1;
                    HomeActivity.this.recyclerViewOfferAdapter.updateRecyclerView(ownerOffers);
                    HomeActivity.this.recyclerViewOfferAdapter.filterRecycleView(petSpecies);
                    if (searchText.length() >= 3) {
                        HomeActivity.this.recyclerViewOfferAdapter.searchRecycleView(searchText);
                    }
                }
            }
        });
    }

    private void recyclerViewPost(ArrayList<HomeOffers> offers) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewCategoryList = findViewById(R.id.recycler_post_id);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);


         this.recyclerViewOfferAdapter = new HomeAdapterPost(offers, new HomeAdapterPost.ItemClickedListener() {
            @Override
            public void onClickItem(HomeOffers postHome) {
                showToast(" said : "+postHome.getDescription());
            }

            @Override
            public void onCLickSeePost(HomeOffers homeOffer) {
                showToast("See Post Clicked");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.putExtra("lprofileId",homeOffer.getOfferId());
                startActivity(intent);

            }

            @Override
            public void onClickSeeProfile(HomeOffers homeOffer) {
               /* Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("lprofileId",homeOffer.getProfileId());
                intent.putExtra("profileId","myId");
                startActivity(intent);*/
                showToast("See profile Clicked");
            }

        });

        recyclerViewCategoryList.setAdapter(this.recyclerViewOfferAdapter);
    }

    private void recyclerViewCategory() {
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewCategoryList = findViewById(R.id.recycler_category_id);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        this.recyclerViewCategoryAdapter = new HomeAdapterCategory(HomeViewModel.getAllCategories(), new HomeAdapterCategory.ItemClickedListener() {
            @Override
            public void onItemClick(PetCategory petCategory) {
                if(petCategory.getName().equals("All") && !petCategory.isActive()){
                    HomeActivity.this.recyclerViewCategoryAdapter.resetAllCategoriesToInactive();
                    petSpecies = new ArrayList<>();
                    petSpecies.add("All");
                }
                if(petCategory.isActive() && !petCategory.getName().equals("All")){
                    if(petSpecies.contains("All")) petSpecies = new ArrayList<>();
                    if(HomeActivity.this.recyclerViewCategoryAdapter.isOneIsActive(petCategory.getName())){
                        int lengthOfImage = petCategory.getImg().length();
                        if(lengthOfImage<7){
                            // data synchronisation prob..
                        }else{
                        petCategory.setImg( petCategory.getImg().substring(0,lengthOfImage-7) );
                        petSpecies.remove(petCategory.getName().toUpperCase());
                        }
                    }else {
                        HomeActivity.this.recyclerViewCategoryAdapter.resetAllCategoriesToInactive();
                        petSpecies = new ArrayList<>();
                        petSpecies.add("All");
                    }
                }else if(!petCategory.getName().equals("All")){
                    petCategory.setImg(petCategory.getImg()+"_active") ;
                    HomeActivity.this.recyclerViewCategoryAdapter.disableAllCategoryChoice();
                    petSpecies.remove("All");
                    petSpecies.add(petCategory.getName().toUpperCase());

                }
                if(HomeActivity.tabIndex ==1){
                    HomeActivity.this.recyclerViewOfferAdapter.updateRecyclerView(ownerOffers);
                }else{
                    HomeActivity.this.recyclerViewOfferAdapter.updateRecyclerView(keeperOffers);
                }
                HomeActivity.this.recyclerViewOfferAdapter.filterRecycleView(petSpecies);
            }
        });

        recyclerViewCategoryList.setAdapter(this.recyclerViewCategoryAdapter);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void showListToast(List<String> msg){
        Toast.makeText(this,msg.toString(),Toast.LENGTH_SHORT).show();
    }


}