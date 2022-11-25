package ma.ensam.petkeeper.views.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.entities.Profile;
import ma.ensam.petkeeper.entities.User;
import ma.ensam.petkeeper.viewmodels.AuthViewModel;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private AuthViewModel authViewModel;

    Button login_btn;
    EditText firstname_et;
    EditText lastname_et;
    EditText country_et;
    EditText city_et;
    EditText email_et;
    EditText phoneNumber_et;
    EditText password_et;
    EditText confirm_password_et;
    Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // initialisation of AuthViewModel :
        this.authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);


        login_btn = findViewById(R.id.login_btn);
        firstname_et = findViewById(R.id.firstname_et);
        lastname_et = findViewById(R.id.lastname_et);
        country_et = findViewById(R.id.country_et);
        city_et = findViewById(R.id.city_et);
        email_et = findViewById(R.id.email_et);
        phoneNumber_et = findViewById(R.id.phoneNumber_et);
        password_et = findViewById(R.id.password_et);
        confirm_password_et = findViewById(R.id.confirm_password_et);
        signup_btn = findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(view ->{
            User user = new User(email_et.getText().toString(),password_et.getText().toString());
            Profile profile = new Profile(firstname_et.getText().toString()+" "+lastname_et.getText().toString(),
                    phoneNumber_et.getText().toString(), country_et.getText().toString(), city_et.getText().toString());
            if (user.getEmail().isEmpty() || user.getPassword().isEmpty() || profile.getCity().isEmpty() || profile.getCountry().isEmpty() || profile.getPhoneNumber().isEmpty() || confirm_password_et.getText().toString().isEmpty()){
                Toast.makeText(this,"must fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!user.getPassword().equals(confirm_password_et.getText().toString())){
                Toast.makeText(this,"password not matched", Toast.LENGTH_SHORT).show();
            } else {
                if(this.authViewModel.register(user,profile)){
                    Toast.makeText(this,"You are successfully registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                else{
                    Toast.makeText(this,"Something went wrong! please try again", Toast.LENGTH_SHORT).show();
                }
            }

        });

        login_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        this.finish();
    }
}