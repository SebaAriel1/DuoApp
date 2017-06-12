package com.example.progeria.duoapp.FirebaseObjets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.progeria.duoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView gameTV, regionTV, usernameTV, leagueTV, divisionTV, mainRolTV, otherRolTV;
    private ProgressBar loadingPB;
    private LinearLayout profileLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gameTV = (TextView) findViewById(R.id.gameTV);
        regionTV = (TextView) findViewById(R.id.regionTV);
        usernameTV = (TextView) findViewById(R.id.usernameTV);
        leagueTV = (TextView) findViewById(R.id.leagueTV);
        divisionTV = (TextView) findViewById(R.id.divisionTV);
        mainRolTV = (TextView) findViewById(R.id.mainRolTV);
        otherRolTV = (TextView) findViewById(R.id.otherRolTV);
        loadingPB = (ProgressBar) findViewById(R.id.loadingPB);
        profileLayout = (LinearLayout) findViewById(R.id.profileLayout);

        loadingPB.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.INVISIBLE);

        //FIREBASE DATABASE
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bdRef = database.getReference(FirebaseReferences.BD_REFERENCE);
        Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        bdRef.child(FirebaseReferences.USERS_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot user : dataSnapshot.getChildren()){
                    if (user.getValue(User.class).getMail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        User actualUser = user.getValue(User.class);
                        gameTV.setText(actualUser.getGame());
                        regionTV.setText(actualUser.getRegion());
                        usernameTV.setText(actualUser.getUsername());
                        leagueTV.setText(actualUser.getLeague());
                        mainRolTV.setText(actualUser.getMainRol());
                        otherRolTV.setText(actualUser.getOtherRol());
                        divisionTV.setText(actualUser.getDivision());
                        loadingPB.setVisibility(View.GONE);
                        profileLayout.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
