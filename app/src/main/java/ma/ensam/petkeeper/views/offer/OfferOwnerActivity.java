package ma.ensam.petkeeper.views.offer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.relations.OfferWithCreator;
import ma.ensam.petkeeper.entities.relations.UserAndProfile;
import ma.ensam.petkeeper.viewmodels.OfferViewModel;
import ma.ensam.petkeeper.viewmodels.ProfileViewModel;

public class OfferOwnerActivity extends AppCompatActivity {
    OfferViewModel offerViewModel;
    ProfileViewModel profileViewModel;
    long offerId;

    ImageView ivProfilePic;
    TextView tvProfileName;
    TextView tvOfferDate;
    TextView tvOfferDescr;
    ImageView ivPetImage;
    TextView tvPetResp;
    TextView tvAddressResp;
    TextView tvFromResp;
    TextView tvToResp;
    TextView tvDurationResp;
    FloatingActionButton btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_owner);

        offerId = 1; // TEST-ONLY

        try {
//            offerViewModel = ViewModelProviders.of(OfferOwnerActivity.this)
//                    .get(OfferViewModel.class);

            profileViewModel = ViewModelProviders.of(OfferOwnerActivity.this)
                    .get(ProfileViewModel.class);

            profileViewModel.findProfileAndUserByProfileId(offerId).observe(this,
                    userAndProfile -> {
                        tvProfileName.setText(userAndProfile.profile.getFullName());
                        tvOfferDate.setText(userAndProfile.user.getEmail());
                    });

//            offerViewModel.findOfferAndCreatorByOfferId(offerId).observe(
//                    OfferOwnerActivity.this,
//                    this::updateView);
        } catch (Exception e) {
            Toast.makeText(
                    OfferOwnerActivity.this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }

        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvOfferDate = findViewById(R.id.tvOfferDate);
        tvOfferDescr = findViewById(R.id.tvOfferDescr);
        ivPetImage = findViewById(R.id.ivPetImage);
        tvPetResp = findViewById(R.id.tvPetResp);
        tvAddressResp = findViewById(R.id.tvAddressResp);
        tvFromResp = findViewById(R.id.tvFromResp);
        tvToResp = findViewById(R.id.tvToResp);
        tvDurationResp = findViewById(R.id.tvDurationResp);
        btnCall = findViewById(R.id.btnCall);

        btnCall.setOnClickListener(view -> {
            String number = "0669696969";
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
        });
    }

//    public void updateView(OfferWithCreator owc) {
//        // ivProfilePic
//        tvProfileName.setText(owc.creator.getFullName());
//        tvOfferDate.setText(owc.offer.getCreationDate().toString());
//        tvOfferDescr.setText(owc.offer.getDescription());
//        // ivPetImage
//        tvPetResp.setText(owc.offer.getPet().name());
//        tvAddressResp.setText(owc.creator.getAddress());
//        tvFromResp.setText(owc.offer.getFromDate().toString());
//        tvToResp.setText(owc.offer.getToDate().toString());
//        // tvDuration
//    }
}