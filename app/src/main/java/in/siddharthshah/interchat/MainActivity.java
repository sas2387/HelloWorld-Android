package in.siddharthshah.interchat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by siddharthshah on 9/8/17.
 */

public class MainActivity extends AppCompatActivity {

    boolean isPreferencesSet = false;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences(getString(R.string.app_preferences), MODE_PRIVATE);
        isPreferencesSet = sp.getBoolean(getString(R.string.preferences_set), false);
        if(!isPreferencesSet){
            ChatDataSource dataSource = new ChatDataSource(getBaseContext());
            dataSource.open();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            dataSource.insertChat(1, "Kunal Baweja", "kunal.baweja@columbia.edu", sdf.format(date));
            dataSource.insertChat(2, "Akshay Nagpal", "akshay.nagpal@columbia.edu", sdf.format(date));
            dataSource.close();

            // show preferences fragment
            fragmentManager.beginTransaction().replace(R.id.ll_container, new PreferencesFragment()).commit();
        } else {
            // show default chat screen
            Log.d("OPEN","THIS");
            fragmentManager.beginTransaction().replace(R.id.ll_container, new PreviousChatsFragment()).commit();
        }
    }

    protected void openChatFragment(){
        Log.d("TAG","OPEN CHAT");
        fragmentManager.beginTransaction().replace(R.id.ll_container, new PreviousChatsFragment()).commit();

    }
}
