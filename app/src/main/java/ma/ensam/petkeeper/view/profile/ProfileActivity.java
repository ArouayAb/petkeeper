package ma.ensam.petkeeper.view.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.utils.BitmapUtility;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    ActivityResultLauncher<Intent> activityResultLauncher;
    private final String[] galleryPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Map<String, Drawable> drawableMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawableMap.put("star_full", ContextCompat.getDrawable(this, R.drawable.star_full));
        drawableMap.put("star_empty", ContextCompat.getDrawable(this, R.drawable.star_empty));

        setContentView(R.layout.activity_profile);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        LinearLayout cardContainer = findViewById(R.id.card_container);
        ConstraintLayout card = (ConstraintLayout) layoutInflater.inflate(
                R.layout.card_layout,
                cardContainer,
                false
        );

        ConstraintLayout card2 = (ConstraintLayout) layoutInflater.inflate(
                R.layout.card_layout,
                cardContainer,
                false
        );
        cardContainer.addView(card);
        cardContainer.addView(card2);

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
    }

    @AfterPermissionGranted(101)
    public void onClickUploadImage(View view) {
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            this.activityResultLauncher.launch(intent);
        } else {
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
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case 101:
                this.onClickUploadImage(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        System.out.println("Permission Denied");
    }
}