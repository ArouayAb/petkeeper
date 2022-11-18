package ma.ensam.petkeeper.view.offer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Objects;

import ma.ensam.petkeeper.R;

public class NewOfferActivity extends AppCompatActivity {
    TextView etStartDate;
    TextView etEndDate;
    TextView btnUploadImage;
    MaterialButton btnSubmit;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);

        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnUploadImage = findViewById(R.id.btnUploadImage);

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        System.out.println(Objects.requireNonNull(data).getData());
                        btnUploadImage.setText("File chosen");
                    }
                }
        );

        btnUploadImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            this.activityResultLauncher.launch(intent);
        });

        Calendar calendar = Calendar.getInstance();

        final int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int startMonth = calendar.get(Calendar.MONTH);
        final int startYear = calendar.get(Calendar.YEAR);
        pickDate(etStartDate, startDay, startMonth, startYear);

        final int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int endMonth = calendar.get(Calendar.MONTH);
        final int endYear = calendar.get(Calendar.YEAR);
        pickDate(etEndDate, endDay, endMonth, endYear);


        btnSubmit.setOnClickListener(view -> startActivity(new Intent(NewOfferActivity.this, OfferOwnerActivity.class)));
    }

    public void pickDate(TextView input, int day, int month, int year) {
        input.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    NewOfferActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int pickedYear, int pickedMonth, int pickedDay) {
                            pickedMonth++;
                            String startDate = pickedDay + "/" + pickedMonth + "/" + pickedYear;
                            input.setText(startDate);
                        }
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });
    }
}