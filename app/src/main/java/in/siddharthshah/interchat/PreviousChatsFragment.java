package in.siddharthshah.interchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class PreviousChatsFragment extends Fragment {

    View view;
    ListViewCompat listViewCompat;
    boolean loaded = false;

    private DatabaseReference dbReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_previouschats, container, false);
        listViewCompat = view.findViewById(R.id.listview_previouschats);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!loaded) {

            /* ChatDataSource dataSource = new ChatDataSource(getActivity());
            dataSource.open();
            List<Chat> chats = dataSource.getAllChatsSorted();
            dataSource.close(); */

            final ArrayList<String> chatIds = new ArrayList<>();
            final ArrayAdapter<String> chatsAdapter = new ChatPreviewAdapter(getActivity(), chatIds);
            listViewCompat.setAdapter(chatsAdapter);

            // firebase testing code
            dbReference = FirebaseDatabase.getInstance("https://fir-interchat.firebaseio.com").getReference();

            DatabaseReference myRef = dbReference.child("1234");

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    chatIds.add(dataSnapshot.getKey());
                    Log.d("TAG", chatIds.toString());
                    chatsAdapter.notifyDataSetChanged();
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

            listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    //open new activity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("myID","1234");
                    intent.putExtra("senderID", chatIds.get(pos));
                    startActivity(intent);
                }
            });

            /*String key = dbReference.child("1234").child("4444").push().getKey();
            Log.d("KEY",key);
            ChatMessage cm = new ChatMessage("Hello", 1234, new Date().getTime());
            Map<String, Object> postValues = cm.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/1234/4444/" + key, postValues);

            dbReference.updateChildren(childUpdates);*/
            loaded = true;
        }

    }
}
