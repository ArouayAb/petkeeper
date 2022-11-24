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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.models.compositeKeys.ReviewKey;
import ma.ensam.petkeeper.models.PostProfile;
import ma.ensam.petkeeper.models.ReviewProfile;
import ma.ensam.petkeeper.utils.BitmapUtility;
import ma.ensam.petkeeper.utils.PathUtil;
import ma.ensam.petkeeper.viewmodels.ProfileViewModel;
import ma.ensam.petkeeper.viewmodels.ReviewViewModel;
import ma.ensam.petkeeper.views.profile.adapters.ProfileAdapterPost;
import ma.ensam.petkeeper.views.profile.adapters.ProfileAdapterReview;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ProfileAdapterPost cardsAdapter;
    private ProfileAdapterReview reviewsAdapter;
    private ProfileViewModel profileViewModel;
    private ReviewViewModel reviewViewModel;
    private ConstraintLayout upperInfo;
    private ConstraintLayout generalInfo;
    private LayoutInflater layoutInflater;
    private View writeReviewRootView;
    private PopupWindow writeReviewWindow;

    private List<ReviewProfile> reviewProfiles = new ArrayList<>();
    private List<PostProfile> postProfiles = new ArrayList<>();

    private final long temp_current_profile_id = 1L;
    private final long temp_self_profile_id = 2L;

    private final boolean isVisitingSelfProfile = temp_self_profile_id == temp_current_profile_id;

    private final String[] galleryPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Runnable lastAction = () -> {

    };

    private final Map<String, Drawable> drawableMap = new HashMap<>();

    public ProfileActivity() {
    }

    public void ensureReviewEligibility() {
        if(this.isVisitingSelfProfile) {
            LinearLayout reviewSection = findViewById(R.id.review_section);
            LinearLayout starContainer = findViewById(R.id.star_container);
            TextView writeReview = findViewById(R.id.write_review);

            reviewSection.removeView(starContainer);
            reviewSection.removeView(writeReview);
        }
    }

    public void togglePostCreation() {
        if(!this.isVisitingSelfProfile) {
            FloatingActionButton addPostButton = findViewById(R.id.add_post_button);
            ConstraintLayout rootView = findViewById(R.id.root_view);

            rootView.removeView(addPostButton);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawableMap.put("star_full", ContextCompat.getDrawable(this, R.drawable.star_full));
        drawableMap.put("star_empty", ContextCompat.getDrawable(this, R.drawable.star_empty));

        setContentView(R.layout.activity_profile);
        this.layoutInflater = LayoutInflater.from(this);
        this.writeReviewRootView = layoutInflater.inflate(R.layout.write_review_layout, null);

        this.profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        this.reviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);

        ensureReviewEligibility();
        togglePostCreation();

        initProfileCardRecyclerView(this.postProfiles);
        initProfileReviewRecyclerView(this.reviewProfiles);

        this.profileViewModel.findProfileWithUserById(temp_current_profile_id).observe(this, (userAndProfile) -> {
            if (userAndProfile == null) return;

            this.upperInfo = findViewById(R.id.upper_info);
            this.generalInfo = findViewById(R.id.general_info);

            ShapeableImageView profilePictureView = (ShapeableImageView) this.upperInfo.getViewById(R.id.profile_picture);
            TextView profileNameView = (TextView) this.upperInfo.getViewById(R.id.profile_name);
            TextView profilePhoneNumberView = (TextView) this.upperInfo.getViewById(R.id.profile_phone_number);
            TextView profileCountry = (TextView) this.generalInfo.getViewById(R.id.country_value);
            TextView profileCity = (TextView) this.generalInfo.getViewById(R.id.city_value);
            TextView profileEmail = (TextView) this.generalInfo.getViewById(R.id.email_value);

            Runnable updateProfilePic = () -> {
                Bitmap bm = BitmapUtility.extractFromPath(userAndProfile.profile.getProfilePicUrl());
                profilePictureView.setImageBitmap(bm);
            };

            if (EasyPermissions.hasPermissions(ProfileActivity.this, galleryPermissions)) {
                updateProfilePic.run();
            } else {
                lastAction = updateProfilePic;
                EasyPermissions.requestPermissions(ProfileActivity.this, "Access for storage",
                        101, galleryPermissions);
            }

            profileNameView.setText(userAndProfile.profile.getFullName());
            profilePhoneNumberView.setText(userAndProfile.profile.getPhoneNumber());
            profileCountry.setText(userAndProfile.profile.getCountry());
            profileCity.setText(userAndProfile.profile.getCity());
            profileEmail.setText(userAndProfile.user.getEmail());
        });
        this.profileViewModel.findProfileWithOffersById(temp_current_profile_id).observe(this, (profileWithOffers) -> {
            if (profileWithOffers == null) return;

            this.postProfiles = profileWithOffers.offers.stream()
                    .map(offer ->
                            new PostProfile(
                                    offer.getId(),
                                    profileWithOffers.profile.getProfilePicUrl(),
                                    profileWithOffers.profile.getFullName(),
                                    offer.getCreationDate(),
                                    offer.getTitle(),
                                    offer.getPet().toString(),
                                    offer.getFromDate(),
                                    offer.getToDate()))
                    .collect(Collectors.toList());
            this.cardsAdapter.updateProfilePosts(this.postProfiles);
        });
        this.profileViewModel.findProfilesWithReviewsOnIt(temp_current_profile_id).observe(this, profileWithReviewList -> {
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
        });
        this.reviewViewModel.findReviewByIds(temp_current_profile_id, temp_self_profile_id).observe(this, (review) -> {
            if(this.isVisitingSelfProfile) return;
            if (review == null) {
                LinearLayout starContainer = findViewById(R.id.star_container);
                for (int i = 0; i < starContainer.getChildCount(); i++) {
                    ImageButton star = (ImageButton) starContainer.getChildAt(i);
                    star.setImageDrawable(drawableMap.get("star_empty"));
                }
                return;
            }

            LinearLayout starContainer = findViewById(R.id.star_container);
            for (int i = 0; i < review.getRating(); i++) {
                ImageButton star = (ImageButton) starContainer.getChildAt(i);
                star.setImageDrawable(drawableMap.get("star_full"));
            }
            for (int i = review.getRating(); i < starContainer.getChildCount(); i++) {
                ImageButton star = (ImageButton) starContainer.getChildAt(i);
                star.setImageDrawable(drawableMap.get("star_empty"));
            }
        });


        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Uri uri = Objects.requireNonNull(data).getData();
                        String filePath = null;
                        try {
                            filePath = PathUtil.getPath(ProfileActivity.this, uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        profileViewModel.updateProfilePicUrlById(temp_current_profile_id, filePath);
                    }
                }
        );
    }

    public void initProfileCardRecyclerView(List<PostProfile> profilePosts) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewCardList = findViewById(R.id.recycler_cards);
        recyclerViewCardList.setLayoutManager(linearLayoutManager);

        this.cardsAdapter = new ProfileAdapterPost(
                profilePosts,
                (postProfile) -> {
                    Toast.makeText(ProfileActivity.this, postProfile.getId() + " clicked", Toast.LENGTH_SHORT)
                            .show();
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
                    Toast.makeText(ProfileActivity.this, reviewProfile.getId().toString() + " clicked", Toast.LENGTH_SHORT)
                            .show();
                });

        recyclerViewCardList.setAdapter(this.reviewsAdapter);
    }

    public void onClickUploadImage(View view) {
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
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
                    101, galleryPermissions);
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
                temp_current_profile_id,
                temp_self_profile_id,
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

        this.reviewViewModel.updateRatingByIds(temp_current_profile_id, temp_self_profile_id, starIndex);
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
        System.out.println("Permission Denied");
    }
}