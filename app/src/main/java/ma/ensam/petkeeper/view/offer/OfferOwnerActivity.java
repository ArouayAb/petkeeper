package ma.ensam.petkeeper.view.offer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ma.ensam.petkeeper.R;

public class OfferOwnerActivity extends AppCompatActivity {
    FloatingActionButton btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_owner);

        btnCall = findViewById(R.id.btnCall);

        btnCall.setOnClickListener(view -> {
            String number = "0669696969";
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
        });
    }
}