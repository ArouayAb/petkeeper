package ma.ensam.petkeeper.views.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.utils.BitmapUtility;
import ma.ensam.petkeeper.viewmodels.ProfileViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ProfileViewModel profileViewModel;
    private ConstraintLayout upperInfo;
    private ConstraintLayout generalInfo;
    private LinearLayout cardContainer;
    private LayoutInflater layoutInflater;

    private final String[] galleryPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Runnable lastAction = () -> {

    };

    private final Map<String, Drawable> drawableMap = new HashMap<>();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.layoutInflater = LayoutInflater.from(this);
        this.cardContainer = findViewById(R.id.card_container);

        drawableMap.put("star_full", ContextCompat.getDrawable(this, R.drawable.star_full));
        drawableMap.put("star_empty", ContextCompat.getDrawable(this, R.drawable.star_empty));

        long temp_id = 1L;
        this.profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        this.profileViewModel.findProfileWithUserById(temp_id).observe(this, (userAndProfile) -> {
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

        this.profileViewModel.findProfileWithOffersById(temp_id).observe(this, (profileWithOffers) -> {
            if (profileWithOffers == null) return;

            for(Offer offer: profileWithOffers.offers) {
                ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(
                        R.layout.card_layout,
                        cardContainer,
                        false
                );
                FrameLayout cardProfileNameFrame = (FrameLayout) card.getChildAt(0);
                ConstraintLayout cardInner = (ConstraintLayout) cardProfileNameFrame.getChildAt(0);

                TextView cardProfileName = (TextView) cardInner.getViewById(R.id.card_profile_name);
                TextView cardTitle = (TextView) cardInner.getViewById(R.id.card_title);
                TextView cardProfileDate = (TextView) cardInner.getViewById(R.id.card_profile_date);
                TextView cardPetValue = (TextView) cardInner.getViewById(R.id.card_pet_value);
                TextView cardFromValue = (TextView) cardInner.getViewById(R.id.card_from_value);
                TextView cardToValue = (TextView) cardInner.getViewById(R.id.card_to_value);
                ShapeableImageView cardProfilePicture = (ShapeableImageView) cardInner.getViewById(R.id.card_profile_picture);

                Bitmap bm = BitmapUtility.extractFromPath(profileWithOffers.profile.getProfilePicUrl());

                cardProfilePicture.setImageBitmap(bm);
                cardProfileName.setText(profileWithOffers.profile.getFullName());
                cardTitle.setText(offer.getTitle());
                cardProfileDate.setText(this.dateFormat.format(offer.getCreationDate()));
                cardPetValue.setText(offer.getPet().toString());
                cardFromValue.setText(this.dateFormat.format(offer.getFromDate()));
                cardToValue.setText(this.dateFormat.format(offer.getToDate()));

                cardContainer.addView(card);
            }
            profileViewModel.findProfileWithOffersById(temp_id).removeObservers(this);
        });

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        ShapeableImageView profilePicture = findViewById(R.id.profile_picture);

                        Bitmap bitmap = BitmapUtility.extractFromUri(
                                this,
                                Objects.requireNonNull(data).getData()
                        );


                        profilePicture.setImageBitmap(bitmap);
                        for (int i = 0; i < cardContainer.getChildCount(); i++) {
                            ConstraintLayout constraintLayout = (ConstraintLayout) cardContainer.getChildAt(i);
                            FrameLayout frameLayout = (FrameLayout) constraintLayout.getChildAt(0);
                            ConstraintLayout innerConstraintLayout = (ConstraintLayout) frameLayout.getChildAt(0);
                            ShapeableImageView cardProfilePicture = (ShapeableImageView) innerConstraintLayout.getChildAt(1);

                            cardProfilePicture.setImageBitmap(bitmap);
                        }
                    }
                }
        );

        LinearLayout reviewContainer = findViewById(R.id.review_container);
        ConstraintLayout review = (ConstraintLayout) layoutInflater.inflate(
                R.layout.review_layout,
                reviewContainer,
                false
        );

        ConstraintLayout review2 = (ConstraintLayout) layoutInflater.inflate(
                R.layout.review_layout,
                reviewContainer,
                false
        );
        reviewContainer.addView(review);
        reviewContainer.addView(review2);
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