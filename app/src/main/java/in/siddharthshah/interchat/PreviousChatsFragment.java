package in.siddharthshah.interchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class PreviousChatsFragment extends Fragment {

    View view;
    ListViewCompat listViewCompat;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> mAdapter;

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
        ChatDataSource dataSource = new ChatDataSource(getActivity());
        dataSource.open();
        List<Chat> chats = dataSource.getAllChatsSorted();
        dataSource.close();

        final ArrayList<String> chatIds = new ArrayList<>();
        final ArrayAdapter<String> chatsAdapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, chatIds);
        listViewCompat.setAdapter(chatsAdapter);

        // firebase testing code
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance("https://fir-interchat.firebaseio.com").getReference();

        DatabaseReference myRef = mFirebaseDatabaseReference.child("1234");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatIds.add(dataSnapshot.getKey());
                Log.d("TAG",chatIds.toString());
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

        /*String key = mFirebaseDatabaseReference.child("1234").child("4444").push().getKey();
        Log.d("KEY",key);
        ChatMessage cm = new ChatMessage("Hello", 1234, new Date().getTime());
        Map<String, Object> postValues = cm.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/1234/4444/" + key, postValues);

        mFirebaseDatabaseReference.updateChildren(childUpdates);*/

    }
}
