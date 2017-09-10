package in.siddharthshah.interchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class ChatActivity extends AppCompatActivity {

    String senderID;
    String myID;
    String senderName;
    ListViewCompat listViewCompat;
    EditText etChat;
    DatabaseReference dbReference;
    ArrayList<ChatMessage> listChatMessages = new ArrayList<>();
    boolean loaded = false;

    ActionBar mActionBarToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        senderID = intent.getStringExtra("senderID");
        myID = intent.getStringExtra("myID");
        senderName = intent.getStringExtra("senderName");
        listViewCompat = (ListViewCompat) findViewById(R.id.listview_chat);
        etChat = (EditText) findViewById(R.id.et_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(senderName);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!loaded) {
            dbReference = FirebaseDatabase.getInstance("https://fir-interchat.firebaseio.com").getReference();
            DatabaseReference myRef = dbReference.child(myID).child(senderID);

            final ChatAdapter chatAdapter = new ChatAdapter(this, listChatMessages, myID, senderID);
            listViewCompat.setAdapter(chatAdapter);

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("TAG", dataSnapshot.getKey());
                    ChatMessage cm = dataSnapshot.getValue(ChatMessage.class);
                    listChatMessages.add(cm);
                    chatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            etChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    boolean handled = false;
                    String message = etChat.getText().toString().trim();
                    if(!message.equals("")){
                        storeMessage(message);
                        sendMessage(message);
                        etChat.setText("");
                        etChat.clearFocus();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etChat.getWindowToken(), 0);
                    }
                    handled = true;
                    return handled;
                }
            });
            loaded = true;
        }
    }

    public void storeMessage(String message){
        String key = dbReference.child(myID).child(senderID).push().getKey();
        Log.d("KEY",key);
        ChatMessage cm = new ChatMessage(message, myID, new Date().getTime());
        Map<String, Object> postValues = cm.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+myID+"/"+senderID+"/" + key, postValues);

        dbReference.updateChildren(childUpdates);
    }

    public void sendMessage(final String message){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://interchat-backend.appspot.com/send";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ChatActivity.this, "Sent", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("senderId", myID);
                    jsonObject.put("receiverId", senderID);
                    jsonObject.put("msg", message);
                    jsonObject.put("timestamp", new Date().getTime());

                    return jsonObject.toString().getBytes();
                } catch (JSONException e){
                    e.printStackTrace();
                    return "{ }".getBytes();
                }
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
