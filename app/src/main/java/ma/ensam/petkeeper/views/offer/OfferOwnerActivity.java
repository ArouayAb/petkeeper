package ma.ensam.petkeeper.views.offer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.daos.OfferDao;

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