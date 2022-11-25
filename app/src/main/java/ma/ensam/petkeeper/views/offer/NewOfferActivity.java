package ma.ensam.petkeeper.views.offer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import ma.ensam.petkeeper.R;
import ma.ensam.petkeeper.config.database.AppDatabase;
import ma.ensam.petkeeper.entities.Offer;
import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.enums.PetSpecies;
import ma.ensam.petkeeper.utils.PathUtility;
import ma.ensam.petkeeper.viewmodels.OfferViewModel;
import pub.devrel.easypermissions.EasyPermissions;

public class NewOfferActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    OfferViewModel offerViewModel;
    ActivityResultLauncher<Intent> activityResultLauncher;
    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;

    TextView newOfferBack;
    Spinner spOfferType;
    EditText etOfferTitle;
    TextView btnUploadImage;
    Spinner spPetType;
    TextView etStartDate;
    TextView etEndDate;
    EditText etOfferDesc;
    MaterialButton btnCancel;
    MaterialButton btnSubmit;

    Uri petImageUri;
    List<OfferType> offerTypes;
    List<PetSpecies> petSpecies;
    int startDay;
    int startMonth;
    int startYear;
    int endDay;
    int endMonth;
    int endYear;
    String petImagePath;

    private final String[] galleryPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Runnable lastAction = () -> {

    };

    long currentProfileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);

        offerViewModel = ViewModelProviders.of(NewOfferActivity.this)
                .get(OfferViewModel.class);

        bindViews();

        populateSpinners();

        init();

        newOfferBack.setOnClickListener(view -> NewOfferActivity.this.finish());

        btnUploadImage.setOnClickListener(view -> {
            Runnable openGallery = () -> {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                this.activityResultLauncher.launch(intent);
            };

            if(EasyPermissions.hasPermissions(NewOfferActivity.this, galleryPermissions)){
                openGallery.run();
            } else {
                this.lastAction = openGallery;
                EasyPermissions.requestPermissions(
                        NewOfferActivity.this,
                        "Access for storage",
                        101,
                        galleryPermissions
                );
            }
        });

        pickDate(etStartDate, startDateListener);

        pickDate(etEndDate, endDateListener);

        btnCancel.setOnClickListener(view -> NewOfferActivity.this.finish());

        btnSubmit.setOnClickListener(view -> {
            Offer offer = new Offer(
                    (OfferType) spOfferType.getSelectedItem(),
                    (PetSpecies) spPetType.getSelectedItem(),
                    etOfferTitle.getText().toString(),
                    etOfferDesc.getText().toString(),
                    petImagePath,
                    new GregorianCalendar(startYear, startMonth, startDay).getTime(),
                    new GregorianCalendar(endYear, endMonth, endDay).getTime(),
                    new Date(),
                    currentProfileId
            );

            long createdOfferId = offerViewModel.insert(offer);

            Intent intent = spOfferType.getSelectedItem().equals(OfferType.OWNER) ?
                    new Intent(NewOfferActivity.this, OfferOwnerActivity.class) :
                    new Intent(NewOfferActivity.this, OfferKeeperActivity.class);
            intent.putExtra("offerId", createdOfferId);
            intent.putExtra("currentProfileId", currentProfileId);
            startActivity(intent);
        });
    }

    private void init() {
        currentProfileId = getIntent().getLongExtra("currentProfileId", 1);
        if (currentProfileId == 0) {
            Toast.makeText(
                    NewOfferActivity.this,
                    "Unable to retrive current profile informations",
                    Toast.LENGTH_SHORT
            ).show();
        }

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        petImageUri =  Objects.requireNonNull(data).getData();
                        btnUploadImage.setText("File chosen");

                        try {
                            petImagePath = PathUtility.getPath(NewOfferActivity.this, petImageUri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        startDateListener = (datePicker, year, month, day) -> {
            int displayedMonth = month +1;
            etStartDate.setText(day + "/" + displayedMonth + "/" + year);
            startDay = day;
            startMonth = month;
            startYear = year;
        };

        endDateListener = (datePicker, year, month, day) -> {
            int displayedMonth = month +1;
            etEndDate.setText(day + "/" + displayedMonth + "/" + year);
            endDay = day;
            endMonth = month;
            endYear = year;
        };
    }


    private void bindViews() {
        newOfferBack = findViewById(R.id.newOfferBack);
        spOfferType = findViewById(R.id.spOfferTypes);
        etOfferTitle = findViewById(R.id.etOfferTitle);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        spPetType = findViewById(R.id.spPetTypes);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etOfferDesc = findViewById(R.id.etOfferDesc);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void populateSpinners() {
        offerTypes = Arrays.asList(OfferType.KEEPER, OfferType.OWNER);
        petSpecies = Arrays.asList(PetSpecies.CAT, PetSpecies.DOG, PetSpecies.BIRD,
                PetSpecies.FISH, PetSpecies.TURTLE);

        ArrayAdapter<OfferType> offerTypeAdapter = new ArrayAdapter<OfferType>(
                NewOfferActivity.this,
                android.R.layout.simple_spinner_item,
                offerTypes
        );
        offerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOfferType.setAdapter(offerTypeAdapter);
        ArrayAdapter<PetSpecies> petSpeciesAdapter = new ArrayAdapter<PetSpecies>(
                NewOfferActivity.this,
                android.R.layout.simple_spinner_item,
                petSpecies
        );
        petSpeciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPetType.setAdapter(petSpeciesAdapter);
    }

    public void pickDate(TextView input, DatePickerDialog.OnDateSetListener dateListener) {
        input.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    NewOfferActivity.this,
                    dateListener,
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        this.lastAction.run();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        System.out.println("Permission denied");
    }
}