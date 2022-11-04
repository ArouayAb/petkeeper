package ma.ensam.petkeeper.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ma.ensam.petkeeper.R;


public class LoginActivity extends AppCompatActivity {

    AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialisation of AuthViewModel :
        authViewModel = new AuthViewModel(this);

        setContentView(R.layout.activity_login);
    }


}