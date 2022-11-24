package ma.ensam.petkeeper.views.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.config.app.AppConfig;
import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.models.PostHome;
import ma.ensam.petkeeper.models.PostProfile;
import ma.ensam.petkeeper.models.ReviewProfile;
import ma.ensam.petkeeper.utils.BitmapUtility;
import ma.ensam.petkeeper.utils.PathUtil;
import ma.ensam.petkeeper.viewmodels.HomeViewModel;
import ma.ensam.petkeeper.viewmodels.ProfileViewModel;
import ma.ensam.petkeeper.views.auth.LoginActivity;
import ma.ensam.petkeeper.views.home.HomeActivity;
import ma.ensam.petkeeper.views.home.adapters.HomeAdapterPost;
import ma.ensam.petkeeper.views.profile.adapters.ProfileAdapterPost;
import ma.ensam.petkeeper.views.profile.adapters.ProfileAdapterReview;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private RecyclerView.Adapter<ProfileAdapterPost.ViewHolder> cardsAdapter;
    private RecyclerView.Adapter<ProfileAdapterReview.ViewHolder> reviewsAdapter;
    private ProfileViewModel profileViewModel;
    private ConstraintLayout upperInfo;
    private ConstraintLayout generalInfo;
    private LayoutInflater layoutInflater;

    private List<ReviewProfile> reviewProfiles = new ArrayList<>();
    private List<PostProfile> postProfiles = new ArrayList<>();

    private final long temp_current_profile_id = 1L;
    private final long temp_self_profile_id = 2L;

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
        if(temp_self_profile_id == temp_current_profile_id) {
            LinearLayout reviewSection = findViewById(R.id.review_section);
            LinearLayout starContainer = findViewById(R.id.star_container);
            TextView writeReview = findViewById(R.id.write_review);
            RecyclerView recyclerView = findViewById(R.id.review_container);

            reviewSection.removeView(starContainer);
            reviewSection.removeView(writeReview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.layoutInflater = LayoutInflater.from(this);
//        this.cardContainer = findViewById(R.id.card_container);

        drawableMap.put("star_full", ContextCompat.getDrawable(this, R.drawable.star_full));
        drawableMap.put("star_empty", ContextCompat.getDrawable(this, R.drawable.star_empty));

        ensureReviewEligibility();

        this.profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

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
                            profileWithOffers.profile.getProfilePicUrl(),
                            profileWithOffers.profile.getFullName(),
                            offer.getCreationDate(),
                            offer.getTitle(),
                            offer.getPet().toString(),
                            offer.getFromDate(),
                            offer.getToDate()))
                .collect(Collectors.toList());

            updateProfileCardRecyclerView(this.postProfiles);
        });
        this.profileViewModel.findProfileWithReviewsOnIt(temp_current_profile_id).observe(this, (profileWithReviews -> {
            if (profileWithReviews == null) return;

            this.reviewProfiles = profileWithReviews.reviews.stream()
                    .map(review ->
                            new ReviewProfile(
                                    profileWithReviews.profile.getProfilePicUrl(),
                                    profileWithReviews.profile.getFullName(),
                                    review.getBody(),
                                    review.getRating()
                            ))
                    .collect(Collectors.toList());
            updateProfileReviewRecyclerView(this.reviewProfiles);
        }));


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

    public void updateProfileCardRecyclerView(List<PostProfile> profilePosts) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewCardList = findViewById(R.id.recycler_cards);
        recyclerViewCardList.setLayoutManager(linearLayoutManager);

        cardsAdapter = new ProfileAdapterPost(
                profilePosts,
                view -> {
                    Toast.makeText(ProfileActivity.this, view.getId() + " clicked", Toast.LENGTH_SHORT)
                            .show();
                });

        recyclerViewCardList.setAdapter(cardsAdapter);
    }

    public void updateProfileReviewRecyclerView(List<ReviewProfile> reviewProfiles) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerViewCardList = findViewById(R.id.review_container);
        recyclerViewCardList.setLayoutManager(linearLayoutManager);

        reviewsAdapter = new ProfileAdapterReview(
                reviewProfiles,
                view -> {
                    Toast.makeText(ProfileActivity.this, view.getId() + " clicked", Toast.LENGTH_SHORT)
                            .show();
                });

        recyclerViewCardList.setAdapter(reviewsAdapter);
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
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupView = layoutInflater.inflate(R.layout.write_review_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setElevation(40);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_shadow));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void onClickUpdateStars(View view) {
        String starTag = (String) view.getTag();
        LinearLayout starContainer = findViewById(R.id.star_container);

        int starIndex = Integer.parseInt(
                starTag.chars()
                .mapToObj(ch -> (char) ch)
                .filter(Character::isDigit)
                .map(Object::toString)
                .collect(Collectors.joining())
        );

        for (int i = 0; i < starIndex; i++) {
            ImageButton star = (ImageButton) starContainer.getChildAt(i);
            star.setImageDrawable(drawableMap.get("star_full"));
        }
        for (int i = starIndex; i < starContainer.getChildCount(); i++) {
            ImageButton star = (ImageButton) starContainer.getChildAt(i);
            star.setImageDrawable(drawableMap.get("star_empty"));
        }
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