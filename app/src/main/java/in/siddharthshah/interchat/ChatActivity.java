package in.siddharthshah.interchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class ChatActivity extends AppCompatActivity {

    String senderID;
    String myID;
    ListViewCompat listViewCompat;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        senderID = intent.getStringExtra("senderID");
        myID = intent.getStringExtra("myID");
        listViewCompat = (ListViewCompat) findViewById(R.id.listview_chat);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG",senderID);
        Log.d("TAG",myID);

        dbReference = FirebaseDatabase.getInstance("https://fir-interchat.firebaseio.com").getReference();

        DatabaseReference myRef = dbReference.child(myID).child(senderID);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG", dataSnapshot.getKey());
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
    }


}
