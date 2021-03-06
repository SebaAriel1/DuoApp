package com.example.progeria.duoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Bundle bundle;
    private String urlPic;
    private ImageView profilePic;
    private TextView userMail;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (AccessToken.getCurrentAccessToken() == null){
            goLoginScreen();
        } else {

            bundle = getIntent().getExtras();
            if (bundle != null) {
                urlPic = bundle.getString(LoginActivity.URL_PICTURE);
            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            profilePic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_imageView);
            userMail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_mail);
            userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);

            //Si viene de facebook, setear imagen de perfil en drawer
            final Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                SetProfilePic reportTask = new SetProfilePic(currentProfile.getId(), profilePic);
                reportTask.execute();
            }

            userMail.setText(bundle.getString(LoginActivity.MAIL_EXTRA));
            userName.setText(bundle.getString(LoginActivity.FIRST_NAME_EXTRA));
        }


    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_camera:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public void logOut(MenuItem item) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    private class SetProfilePic extends AsyncTask<Void, Void, Integer> {

        private String src;
        private Bitmap bitmap;
        private ImageView photo;

        SetProfilePic(String profileId, ImageView picPhoto) {
            this.src = profileId;
            this.photo = picPhoto;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                URL url = new URL("https://graph.facebook.com/" + src + "/picture?width=800");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer response) {
            setImage(bitmap, photo);
        }
    }

    public void setImage(Bitmap bm, ImageView iv) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bm);
        roundedBitmapDrawable.setCircular(true);
        roundedBitmapDrawable.setAntiAlias(true);
        iv.setImageDrawable(roundedBitmapDrawable);
        iv.setVisibility(View.VISIBLE);
    }
}
