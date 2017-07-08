package com.example.progeria.duoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.progeria.duoapp.FirebaseObjets.FirebaseReferences;
import com.example.progeria.duoapp.FirebaseObjets.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = RegisterActivity.class.getCanonicalName();

    public static final int PLACE_PICKER_REQUEST = 1;
    private Spinner gameSpinner, leagueSpinner, divisionSpinner, mainRolSpinner, secundaryRolSpinner, regionSpinner;
    private EditText usernameET, addressET;
    private Button confirmButton;

    private ArrayAdapter<String> gameAdapter;
    private ArrayAdapter<String> leagueAdapter;
    private ArrayAdapter<String> divisionAdapter;
    private ArrayAdapter<String> rolAdapter;
    private ArrayAdapter<String> regionAdapter;

    private DatabaseReference bdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        gameSpinner = (Spinner) findViewById(R.id.registerGameSpinner);
        leagueSpinner = (Spinner) findViewById(R.id.registerLeagueSpinner);
        divisionSpinner = (Spinner) findViewById(R.id.registerDivisionSpinner);
        mainRolSpinner = (Spinner) findViewById(R.id.registerRolSpinner);
        secundaryRolSpinner = (Spinner) findViewById(R.id.registerRol2Spinner);
        regionSpinner = (Spinner) findViewById(R.id.registerRegionSpinner);

        usernameET = (EditText) findViewById(R.id.registerUsernameED);
        addressET = (EditText) findViewById(R.id.registerLocationEditText);

        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(RegisterActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });




        confirmButton = (Button) findViewById(R.id.confirmRegisterButton);

        //Obtener adaptadores con datos correspondientes
        prepareArrayAdapters();
        //Setear calores a spinners
        configureSpinner(gameSpinner, gameAdapter);
        configureSpinner(leagueSpinner, leagueAdapter);
        configureSpinner(divisionSpinner, divisionAdapter);
        configureSpinner(mainRolSpinner, rolAdapter);
        configureSpinner(secundaryRolSpinner, rolAdapter);
        configureSpinner(regionSpinner, regionAdapter);

        //FIREBASE DATABASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bdRef = database.getReference(FirebaseReferences.BD_REFERENCE);

    }

    private void configureSpinner(final Spinner spinner, ArrayAdapter<String> adapter) {
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                usernameET.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void prepareArrayAdapters() {
        gameAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, getResources()
                .getStringArray(R.array.gameList));

        leagueAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, getResources()
                .getStringArray(R.array.leagueList));

        divisionAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, getResources()
                .getStringArray(R.array.divisionList));

        rolAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, getResources()
                .getStringArray(R.array.rolList));

        regionAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout, getResources()
                .getStringArray(R.array.regionList));

    }

    public void onClickRegister(View view) {

        if (usernameET.getText() == null){
            usernameET.setError(getString(R.string.register_username_error_text));
        } else {

            User newUser = new User(divisionSpinner.getSelectedItem().toString(),
                    gameSpinner.getSelectedItem().toString(),
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    leagueSpinner.getSelectedItem().toString(),
                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                    regionSpinner.getSelectedItem().toString(),
                    mainRolSpinner.getSelectedItem().toString(),
                    secundaryRolSpinner.getSelectedItem().toString(),
                    usernameET.getText().toString());
            bdRef.child(FirebaseReferences.USERS_REFERENCE).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser);
            goMainActivity();

        }

    }
    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

}
