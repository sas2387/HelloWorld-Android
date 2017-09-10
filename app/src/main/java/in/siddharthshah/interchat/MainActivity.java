package in.siddharthshah.interchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siddharthshah on 9/8/17.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    boolean isPreferencesSet = false;
    FragmentManager fragmentManager;
    String name, email, uid;
    FloatingActionButton fab;
    boolean loaded = false;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        fragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        uid = intent.getStringExtra("uid");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SelectUsertoChatActivity.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("InterChat");
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!loaded) {
            SharedPreferences sp = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE);
            isPreferencesSet = sp.getBoolean(getString(R.string.preferences_set), false);
            if (!isPreferencesSet) {
                // show preferences fragment
                fragmentManager.beginTransaction().replace(R.id.ll_container, new PreferencesFragment()).commit();
            } else {
                // show default chat screen
                fragmentManager.beginTransaction().replace(R.id.ll_container, new PreviousChatsFragment()).commit();
            }
            loaded = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                fragmentManager.beginTransaction().replace(R.id.ll_container, new PreferencesFragment()).commit();
                break;
            case R.id.signout:
                signOut();
                break;
        }
        return true;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                    }
                });
    }

    protected void openChatFragment(){
        Log.d("TAG","OPEN CHAT");
        fragmentManager.beginTransaction().replace(R.id.ll_container, new PreviousChatsFragment()).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
