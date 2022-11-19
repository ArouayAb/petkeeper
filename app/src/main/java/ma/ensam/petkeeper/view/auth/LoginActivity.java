package ma.ensam.petkeeper.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ma.ensam.petkeeper.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    AuthViewModel authViewModel;

    Button signup_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialisation of AuthViewModel :
        authViewModel = new AuthViewModel(this);

        signup_btn = findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }
}