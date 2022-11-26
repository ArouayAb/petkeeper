package ma.ensam.petkeeper.views.offer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.config.app.AppConfig;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.models.ReviewProfile;
import ma.ensam.petkeeper.models.compositeKeys.ReviewKey;
import ma.ensam.petkeeper.utils.BitmapUtility;
import ma.ensam.petkeeper.viewmodels.OfferViewModel;
import ma.ensam.petkeeper.viewmodels.ProfileViewModel;
import ma.ensam.petkeeper.viewmodels.ReviewViewModel;
import ma.ensam.petkeeper.views.offer.adapters.OfferKeeperAdapterReview;
import ma.ensam.petkeeper.views.profile.ProfileActivity;
import ma.ensam.petkeeper.views.profile.adapters.ProfileAdapterReview;

public class OfferKeeperActivity extends AppCompatActivity {
    OfferViewModel offerViewModel;
    private OfferKeeperAdapterReview reviewsAdapter;

    long offerId;
    String phoneNumber;
    Long currentProfileId;
    Long offerCreatorProfileId;

    ImageView profileImg_iv;
    TextView profileName_tv;
    TextView createdDate_tv;
    TextView offerDescription_tv;
    TextView offerTitle_tv;
    TextView pet_tv;
    TextView address_tv;
    TextView fromDate_tv;
    TextView toDate_tv;
    TextView duration_tv;
    TextView rating_tv;
    RatingBar ratingBar_rb;
    TextView totalReviewers;
    TextView tv1, tv2, tv3, tv4, tv5;
    ProgressBar progress1,progress2,progress3,progress4,progress5;
    Button back_btn;
    FloatingActionButton call_btn;

    private ProfileViewModel profileViewModel;
    private ReviewViewModel reviewViewModel;

    private List<ReviewProfile> reviewProfiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_keeper);

        this.profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        this.reviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);

        bindViews();

        offerViewModel = ViewModelProviders.of(OfferKeeperActivity.this)
                .get(OfferViewModel.class);

        offerId = getIntent().getLongExtra("offerId", 1);
        currentProfileId = getIntent().getLongExtra("currentProfileId", 1);

        if (offerId == 0 || currentProfileId == 0) {
            Toast.makeText(
                    OfferKeeperActivity.this,
                    "Error retrieving offer details",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            fetchOfferAndUpdateActivity();
        }

        back_btn.setOnClickListener(view -> OfferKeeperActivity.this.finish());

        profileImg_iv.setOnClickListener(view -> goToProfile());
        profileName_tv.setOnClickListener(view -> goToProfile());

        call_btn.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))));

        initProfileReviewRecyclerView(this.reviewProfiles);

        this.profileViewModel.findProfilesWithReviewsOnIt(currentProfileId).observe(
                this,
                this::onProfileReviewsChange
        );

    }

    private void goToProfile() {
        Intent intent = new Intent(OfferKeeperActivity.this, ProfileActivity.class);
        intent.putExtra("selfProfileId", currentProfileId);
        intent.putExtra("currentProfileId", offerCreatorProfileId);
        startActivity(intent);
    }

    private void fetchOfferAndUpdateActivity() {
        try {
            offerViewModel.findProfileAndOfferByOfferId(offerId).observe(
                    OfferKeeperActivity.this,
                    pwo -> {
                        if (pwo != null) {
                            this.offerCreatorProfileId = pwo.profile.getId();
                            this.phoneNumber = pwo.profile.getPhoneNumber();
                            updateView(pwo);
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(
                    OfferKeeperActivity.this,
                    "Error : " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void bindViews() {
        profileImg_iv = findViewById(R.id.profile_picture);
        profileName_tv = findViewById(R.id.profile_name_tv);
        createdDate_tv = findViewById(R.id.created_date_tv);
        offerDescription_tv = findViewById(R.id.offerDescription_tv);
        offerTitle_tv = findViewById(R.id.offerTitle_tv);
        pet_tv = findViewById(R.id.pet_tv);
        address_tv = findViewById(R.id.address_tv);
        fromDate_tv = findViewById(R.id.fromDate_tv);
        toDate_tv = findViewById(R.id.toDate_tv);
        duration_tv = findViewById(R.id.duration_tv);
        rating_tv = findViewById(R.id.rating_tv);
        ratingBar_rb = findViewById(R.id.ratingBar_rb);
        totalReviewers = findViewById(R.id.totalReviewers_tv);
        progress1 = findViewById(R.id.progress1);
        progress2 = findViewById(R.id.progress2);
        progress3 = findViewById(R.id.progress3);
        progress4 = findViewById(R.id.progress4);
        progress5 = findViewById(R.id.progress5);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        back_btn = findViewById(R.id.back_btn);
        call_btn = findViewById(R.id.call_btn);
    }

    @SuppressLint("SetTextI18n")
    public void updateView(ProfileAndOffer pwo) {
        profileImg_iv.setImageBitmap(BitmapUtility.extractFromPath(pwo.profile.getProfilePicUrl()));
        profileName_tv.setText(pwo.profile.getFullName());
        createdDate_tv.setText(AppConfig.dateFormat.format(pwo.offer.getCreationDate()));
        offerTitle_tv.setText(pwo.offer.getTitle());
        offerDescription_tv.setText(pwo.offer.getDescription());
        pet_tv.setText(pwo.offer.getPet().name());
        address_tv.setText(pwo.profile.getCity() + ", " + pwo.profile.getCountry());
        fromDate_tv.setText(AppConfig.dateFormat.format(pwo.offer.getFromDate()));
        toDate_tv.setText(AppConfig.dateFormat.format(pwo.offer.getToDate()));

        float daysBetween = (pwo.offer.getToDate().getTime()
                - pwo.offer.getFromDate().getTime()) / (1000 * 60 * 60 * 24);
        duration_tv.setText(daysBetween + " days");
    }

    @SuppressLint("SetTextI18n")
    private void onProfileReviewsChange(List<ProfileWithReviewsOnIt> profileWithReviewList) {
        if (profileWithReviewList == null) return;
        int[] stars = {0,0,0,0,0};

        this.reviewProfiles = profileWithReviewList.stream()
                .map((profileWithReview) ->
                        new ReviewProfile(
                                new ReviewKey(
                                        profileWithReview.review.getRevieweeProfileId(),
                                        profileWithReview.review.getReviewerProfileId()
                                ),
                                profileWithReview.profile.getProfilePicUrl(),
                                profileWithReview.profile.getFullName(),
                                profileWithReview.review.getBody(),
                                profileWithReview.review.getRating()
                        ))
                .collect(Collectors.toList());

        profileWithReviewList.forEach((profileWithReview)->{
            stars[profileWithReview.review.getRating()-1]++;
        });
        double averageStars = this.reviewProfiles.stream()
                .mapToDouble(ReviewProfile::getReviewStars)
                .average()
                .orElse(0D);

        long totalReviews = this.reviewProfiles.stream()
                .mapToDouble(ReviewProfile::getReviewStars)
                .count();

        tv1.setText(Integer.toString(stars[0]));
        tv2.setText(Integer.toString(stars[1]));
        tv3.setText(Integer.toString(stars[2]));
        tv4.setText(Integer.toString(stars[3]));
        tv5.setText(Integer.toString(stars[4]));
        progress1.setProgress((int)((stars[0]*100)/totalReviews));
        progress2.setProgress((int)((stars[1]*100)/totalReviews));
        progress3.setProgress((int)((stars[2]*100)/totalReviews));
        progress4.setProgress((int)((stars[3]*100)/totalReviews));
        progress5.setProgress((int)((stars[4]*100)/totalReviews));
        totalReviewers.setText(Long.toString(totalReviews));
        rating_tv.setText(Double.toString(averageStars));
        ratingBar_rb.setRating((float) averageStars);
        this.reviewsAdapter.updateProfileReviews(this.reviewProfiles);
    }


    public void initProfileReviewRecyclerView(List<ReviewProfile> reviewProfiles) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewCardList = findViewById(R.id.review_container);
        recyclerViewCardList.setLayoutManager(linearLayoutManager);

        this.reviewsAdapter = new OfferKeeperAdapterReview(
                reviewProfiles,
                reviewProfile -> {
                    Toast.makeText(OfferKeeperActivity.this, reviewProfile.getId().toString() + " clicked", Toast.LENGTH_SHORT)
                            .show();
                });

        recyclerViewCardList.setAdapter(this.reviewsAdapter);
    }
}