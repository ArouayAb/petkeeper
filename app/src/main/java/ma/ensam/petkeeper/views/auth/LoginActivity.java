package ma.ensam.petkeeper.views.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.entities.User;
import ma.ensam.petkeeper.viewmodels.AuthViewModel;
import ma.ensam.petkeeper.views.home.HomeActivity;
import ma.ensam.petkeeper.views.profile.ProfileActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private AuthViewModel authViewModel;

    EditText email_et;
    EditText password_et;
    Button login_btn;
    Button signup_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialisation of AuthViewModel :
        this.authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);

        login_btn.setOnClickListener(view -> {
            User user = new User(email_et.getText().toString(),password_et.getText().toString());
            if (user.getEmail().isEmpty() || user.getPassword().isEmpty()){
                Toast.makeText(this,"must fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                LiveData<User> userLiveData = this.authViewModel.login(user);

                    userLiveData.observe(this, loggedUser -> {
                        if (loggedUser != null){
                            Intent goToProfile = new Intent(this, HomeActivity.class);
                            goToProfile.putExtra("userId",loggedUser.getId());
                            startActivity(goToProfile);
                        }
                        else
                            Toast.makeText(this,"Invalid email or password", Toast.LENGTH_SHORT).show();
                    });

            }

        });

        signup_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }
}