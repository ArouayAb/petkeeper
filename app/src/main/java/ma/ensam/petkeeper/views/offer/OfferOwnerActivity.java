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
import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.entities.relations.ProfileAndOffer;
import ma.ensam.petkeeper.entities.relations.ProfileWithOffers;
import ma.ensam.petkeeper.utils.BitmapUtility;
import ma.ensam.petkeeper.viewmodels.OfferViewModel;

public class OfferOwnerActivity extends AppCompatActivity {
    OfferViewModel offerViewModel;
    long offerId;

    ImageView ivProfilePic;
    TextView tvProfileName;
    TextView tvOfferDate;
    TextView tvOfferTitle;
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

        bindViews();

        offerViewModel = ViewModelProviders.of(OfferOwnerActivity.this)
                .get(OfferViewModel.class);
        offerId = getIntent().getLongExtra("offerId", 0);
        if (offerId == 0) {
            Toast.makeText(
                    OfferOwnerActivity.this,
                    "Error retrieving offer details",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            fetchOfferAndUpdateActivity();
        }

        btnCall.setOnClickListener(view -> {
            String number = "0669696969";
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
        });
    }

    private void fetchOfferAndUpdateActivity() {
        try {
            offerViewModel.findProfileAndOfferByOfferId(offerId).observe(
                    OfferOwnerActivity.this,
                    this::updateView);
        } catch (Exception e) {
            Toast.makeText(
                    OfferOwnerActivity.this,
                    "Error : " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void bindViews() {
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvOfferDate = findViewById(R.id.tvOfferDate);
        tvOfferTitle = findViewById(R.id.tvOfferTitle);
        tvOfferDescr = findViewById(R.id.tvOfferDescr);
        ivPetImage = findViewById(R.id.ivPetImage);
        tvPetResp = findViewById(R.id.tvPetResp);
        tvAddressResp = findViewById(R.id.tvAddressResp);
        tvFromResp = findViewById(R.id.tvFromResp);
        tvToResp = findViewById(R.id.tvToResp);
        tvDurationResp = findViewById(R.id.tvDurationResp);
        btnCall = findViewById(R.id.btnCall);
    }

    public void updateView(ProfileAndOffer pwo) {
        ivProfilePic.setImageBitmap(BitmapUtility.extractFromPath(pwo.profile.getProfilePicUrl()));
        tvProfileName.setText(pwo.profile.getFullName());
        tvOfferDate.setText(pwo.offer.getCreationDate().toString());
        tvOfferTitle.setText(pwo.offer.getTitle());
        tvOfferDescr.setText(pwo.offer.getDescription());
        ivPetImage.setImageBitmap(BitmapUtility.extractFromPath(pwo.offer.getImage_url()));
        tvPetResp.setText(pwo.offer.getPet().name());
        tvAddressResp.setText(pwo.profile.getCity() + ", " + pwo.profile.getCountry());
        tvFromResp.setText(pwo.offer.getFromDate().toString());
        tvToResp.setText(pwo.offer.getToDate().toString());

        float daysBetween = (pwo.offer.getToDate().getTime()
                - pwo.offer.getFromDate().getTime()) / (1000 * 60 * 60 * 24);
        tvDurationResp.setText(daysBetween + " days");
    }


}