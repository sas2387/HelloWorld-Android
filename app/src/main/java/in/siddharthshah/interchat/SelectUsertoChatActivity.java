package in.siddharthshah.interchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class SelectUsertoChatActivity extends AppCompatActivity {

    Spinner spinnerInterest;
    ListView listView;
    UserListAdapter userListAdapter;
    ArrayList<User> userList = new ArrayList<>();
    String myID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newusertochat);
        spinnerInterest = (Spinner) findViewById(R.id.spinner_interest);
        String[] interests = new String[] {"Education", "Dance", "Music", "Arts", "Politics", "Careers", "Movies", "Hackathon", "Fashion", "Sports"};
        ArrayAdapter<String> interestsAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, interests);
        interestsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterest.setAdapter(interestsAdapter);
        userListAdapter = new UserListAdapter(this, userList);

        SharedPreferences sp = getSharedPreferences(getString(R.string.my_details),Context.MODE_PRIVATE);
        myID = sp.getString(getString(R.string.my_id), "");

        listView = (ListView) findViewById(R.id.lv_personsinterested);
        listView.setAdapter(userListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(SelectUsertoChatActivity.this, ChatActivity.class);
                User user = userList.get(pos);
                intent.putExtra("myID",myID);
                intent.putExtra("senderID", user.getUid());
                intent.putExtra("senderName", user.getName());
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("InterChat");
        setSupportActionBar(toolbar);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void findPersons(View v){
        userList.clear();
        String selectedInterest  = (String)spinnerInterest.getSelectedItem();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://interchat-backend.appspot.com/user/get?interests="+selectedInterest;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PERSON", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                User user = new User(jsonObject.getString("name"), jsonObject.getString("uid"));
                                userList.add(user);
                            }
                            userListAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
