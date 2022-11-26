package ma.ensam.petkeeper.views.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.config.app.AppConfig;
import ma.ensam.petkeeper.entities.Review;
import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.entities.relations.ProfileWithReviewsOnIt;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;
import ma.ensam.petkeeper.models.compositeKeys.ReviewKey;
import ma.ensam.petkeeper.models.OfferProfile;
import ma.ensam.petkeeper.models.ReviewProfile;
import ma.ensam.petkeeper.utils.BitmapUtility;
import ma.ensam.petkeeper.utils.PathUtility;
import ma.ensam.petkeeper.viewmodels.ProfileViewModel;
import ma.ensam.petkeeper.viewmodels.ReviewViewModel;
import ma.ensam.petkeeper.views.offer.NewOfferActivity;
import ma.ensam.petkeeper.views.offer.OfferKeeperActivity;
import ma.ensam.petkeeper.views.offer.OfferOwnerActivity;
import ma.ensam.petkeeper.views.profile.adapters.ProfileAdapterOffer;
import ma.ensam.petkeeper.views.profile.adapters.ProfileAdapterReview;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ProfileAdapterOffer cardsAdapter;
    private ProfileAdapterReview reviewsAdapter;
    private ProfileViewModel profileViewModel;
    private ReviewViewModel reviewViewModel;
    private View writeReviewRootView;
    private PopupWindow writeReviewWindow;

    private List<ReviewProfile> reviewProfiles = new ArrayList<>();
    private List<OfferProfile> offerProfiles = new ArrayList<>();

    private long current_profile_id;
    private long self_profile_id;
    private boolean isVisitingSelfProfile;

    private Runnable lastAction = () -> {

    };

    private final Map<String, Drawable> drawableMap = new HashMap<>();

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        this.drawableMap.put("star_full", ContextCompat.getDrawable(this, R.drawable.star_full));
        this.drawableMap.put("star_empty", ContextCompat.getDrawable(this, R.drawable.star_empty));

        this.current_profile_id = AppConfig.DEBUG_MODE ? 1L : getIntent().getLongExtra("currentProfileId", 0L);
        this.self_profile_id = AppConfig.DEBUG_MODE ? 1L : getIntent().getLongExtra("selfProfileId", 0L);
        this.isVisitingSelfProfile = this.current_profile_id == this.self_profile_id;

        this.profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        this.reviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        this.writeReviewRootView = layoutInflater.inflate(R.layout.write_review_layout, null);

        ensureReviewEligibility();
        toggleOfferCreation();

        initProfileCardRecyclerView(this.offerProfiles);
        initProfileReviewRecyclerView(this.reviewProfiles);

        this.profileViewModel.findProfileWithUserById(current_profile_id).observe(
                this,
                this::onProfileInfoChange
        );
        this.profileViewModel.findProfileWithOffersById(current_profile_id).observe(
                this,
                this::onProfileOffersChange
        );
        this.profileViewModel.findProfilesWithReviewsOnIt(current_profile_id).observe(
                this,
                this::onProfileReviewsChange
        );
        this.reviewViewModel.findReviewByIds(current_profile_id, self_profile_id).observe(
                this,
                this::onProfileStarsChange
        );

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Uri uri = Objects.requireNonNull(data).getData();
                        String filePath = null;try {
                            filePath = PathUtility.getPath(ProfileActivity.this, uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        profileViewModel.updateProfilePicUrlById(current_profile_id, filePath);
                    }
                }
        );
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout inflated = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.review_layout, null);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            inflated.setGravity(Gravity.CENTER);
        } else {
            inflated.setGravity(Gravity.START);
        }
    }


    public void ensureReviewEligibility() {
        if(this.isVisitingSelfProfile) {
            LinearLayout reviewSection = findViewById(R.id.review_section);
            FlexboxLayout starContainer = findViewById(R.id.star_container);
            TextView writeReview = findViewById(R.id.write_review);

            reviewSection.removeView(starContainer);
            reviewSection.removeView(writeReview);
        }
    }

    public void toggleOfferCreation() {
        if(!this.isVisitingSelfProfile) {
            FloatingActionButton addOfferButton = findViewById(R.id.add_offer_button);
            ConstraintLayout rootView = findViewById(R.id.root_view);

            rootView.removeView(addOfferButton);
        }
    }

    public void initProfileCardRecyclerView(List<OfferProfile> profileOffers) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewCardList = findViewById(R.id.recycler_cards);
        recyclerViewCardList.setLayoutManager(linearLayoutManager);

        this.cardsAdapter = new ProfileAdapterOffer(
                profileOffers,
                (offerProfile) -> {
                    Class<?> targetActivity = offerProfile.getOfferType().equals(OfferType.KEEPER.toString()) ?
                            OfferKeeperActivity.class : OfferOwnerActivity.class;
                    Intent offerActivityIntent = new Intent(this, targetActivity);
                    offerActivityIntent.putExtra("offerId", offerProfile.getId());
                    offerActivityIntent.putExtra("currentProfileId", this.self_profile_id);
                    this.activityResultLauncher.launch(offerActivityIntent);
                });

        recyclerViewCardList.setAdapter(this.cardsAdapter);
    }

    public void initProfileReviewRecyclerView(List<ReviewProfile> reviewProfiles) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewCardList = findViewById(R.id.review_container);
        recyclerViewCardList.setLayoutManager(linearLayoutManager);

        this.reviewsAdapter = new ProfileAdapterReview(
                reviewProfiles,
                reviewProfile -> {
                    if (AppConfig.DEBUG_MODE)
                        Toast.makeText(ProfileActivity.this, reviewProfile.getId().toString() + " clicked", Toast.LENGTH_SHORT)
                                .show();
                });

        recyclerViewCardList.setAdapter(this.reviewsAdapter);
    }

    public void onClickUploadImage(View view) {
        if (EasyPermissions.hasPermissions(this, AppConfig.galleryPermissions)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            this.activityResultLauncher.launch(intent);
        } else {
            lastAction = () -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                this.activityResultLauncher.launch(intent);
            };
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, AppConfig.galleryPermissions);
        }
    }

    public void onClickWriteReviewPopup(View view) {
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        this.writeReviewWindow = new PopupWindow(this.writeReviewRootView, width, height, focusable);
        this.writeReviewWindow.setElevation(40);
        this.writeReviewWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_shadow));
        this.writeReviewWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    public void onClickSubmitReview(View view) {
        TextInputEditText reviewBody = this.writeReviewRootView.findViewById(R.id.review_text_edit);

        if(reviewBody == null) return;
        String reviewText = Objects.requireNonNull(reviewBody.getText()).toString();
        this.reviewViewModel.updateBodyByIds(
                current_profile_id,
                self_profile_id,
                reviewText
        );
        this.writeReviewWindow.dismiss();
    }

    public void onClickUpdateStars(View view) {
        String starTag = (String) view.getTag();

        int starIndex = Integer.parseInt(
                starTag.chars()
                .mapToObj(ch -> (char) ch)
                .filter(Character::isDigit)
                .map(Object::toString)
                .collect(Collectors.joining())
        );

        this.reviewViewModel.updateRatingByIds(current_profile_id, self_profile_id, starIndex);
    }

    public void onClickRedirectToNewOffer(View view) {
        Intent offerActivityIntent = new Intent(this, NewOfferActivity.class);
        offerActivityIntent.putExtra("currentProfileId", self_profile_id);
        this.activityResultLauncher.launch(offerActivityIntent);
    }

    public void onClickReturnToPreviousActivity(View view) {
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        this.lastAction.run();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Permission Denied");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void onProfileInfoChange(UserAndProfile userAndProfile) {
        if (userAndProfile == null) return;

        ConstraintLayout upperInfo = findViewById(R.id.upper_info);
        ConstraintLayout generalInfo = findViewById(R.id.general_info);

        ShapeableImageView profilePictureView = (ShapeableImageView) upperInfo.getViewById(R.id.profile_picture);
        TextView profileNameView = (TextView) upperInfo.getViewById(R.id.profile_name);
        TextView profilePhoneNumberView = (TextView) upperInfo.getViewById(R.id.profile_phone_number);
        TextView profileCountry = (TextView) generalInfo.getViewById(R.id.country_value);
        TextView profileCity = (TextView) generalInfo.getViewById(R.id.city_value);
        TextView profileEmail = (TextView) generalInfo.getViewById(R.id.email_value);

        @SuppressLint("NotifyDataSetChanged")
        Runnable updateProfilePic = () -> {
            Bitmap bm = BitmapUtility.extractFromPath(userAndProfile.profile.getProfilePicUrl());
            profilePictureView.setImageBitmap(bm);
            this.cardsAdapter.notifyDataSetChanged();
            this.reviewsAdapter.notifyDataSetChanged();
        };

        if (EasyPermissions.hasPermissions(ProfileActivity.this, AppConfig.galleryPermissions)) {
            updateProfilePic.run();
        } else {
            lastAction = updateProfilePic;
            EasyPermissions.requestPermissions(ProfileActivity.this, "Access for storage",
                    101, AppConfig.galleryPermissions);
        }

        profileNameView.setText(userAndProfile.profile.getFullName());
        profilePhoneNumberView.setText(userAndProfile.profile.getPhoneNumber());
        profileCountry.setText(userAndProfile.profile.getCountry());
        profileCity.setText(userAndProfile.profile.getCity());
        profileEmail.setText(userAndProfile.user.getEmail());
    }

    private void onProfileOffersChange(ProfileWithOffers profileWithOffers) {
        if (profileWithOffers == null) return;

        this.offerProfiles = profileWithOffers.offers.stream()
                .map(offer ->
                        new OfferProfile(
                                offer.getId(),
                                offer.getType().toString(),
                                profileWithOffers.profile.getProfilePicUrl(),
                                profileWithOffers.profile.getFullName(),
                                offer.getCreationDate(),
                                offer.getTitle(),
                                offer.getPet().toString(),
                                offer.getFromDate(),
                                offer.getToDate()))
                .collect(Collectors.toList());
        this.cardsAdapter.updateProfileOffers(this.offerProfiles);
    }

    private void onProfileReviewsChange(List<ProfileWithReviewsOnIt> profileWithReviewList) {
        if (profileWithReviewList == null) return;

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

        double averageStars = this.reviewProfiles.stream()
                .mapToDouble(ReviewProfile::getReviewStars)
                .average()
                .orElse(0D);

        ((RatingBar) findViewById(R.id.profile_rating)).setRating((float) averageStars);
        this.reviewsAdapter.updateProfileReviews(this.reviewProfiles);
    }

    private void onProfileStarsChange(Review review) {
        if (this.isVisitingSelfProfile) return;
        if (review == null) {
            FlexboxLayout starContainer = findViewById(R.id.star_container);
            for (int i = 0; i < starContainer.getChildCount(); i++) {
                ImageButton star = (ImageButton) starContainer.getChildAt(i);
                star.setImageDrawable(drawableMap.get("star_empty"));
            }
            return;
        }

        FlexboxLayout starContainer = findViewById(R.id.star_container);
        for (int i = 0; i < review.getRating(); i++) {
            ImageButton star = (ImageButton) starContainer.getChildAt(i);
            star.setImageDrawable(drawableMap.get("star_full"));
        }
        for (int i = review.getRating(); i < starContainer.getChildCount(); i++) {
            ImageButton star = (ImageButton) starContainer.getChildAt(i);
            star.setImageDrawable(drawableMap.get("star_empty"));
        }
    }
}