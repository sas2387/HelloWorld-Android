package in.siddharthshah.interchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class PreviousChatsFragment extends Fragment {

    View view;
    ListViewCompat listViewCompat;
    boolean loaded = false;
    String myID;
    HashMap<String, String> nameToId;

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


            nameToId = new HashMap<>();
            final ArrayList<String> chatNames = new ArrayList<>();
            final ArrayAdapter<String> chatsAdapter = new ChatPreviewAdapter(getActivity(), chatNames);
            listViewCompat.setAdapter(chatsAdapter);

            // firebase testing code
            dbReference = FirebaseDatabase.getInstance("https://fir-interchat.firebaseio.com").getReference();

            SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.my_details),Context.MODE_PRIVATE);
            myID = sp.getString(getString(R.string.my_id), "");
            DatabaseReference myRef = dbReference.child(myID);

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final String key = dataSnapshot.getKey();

                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url ="https://interchat-backend.appspot.com/user/get?uid="+key;

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("RESP", response);
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        String name = jsonArray.getJSONObject(0).getString("name");
                                        chatNames.add(name);
                                        nameToId.put(name, key);
                                        chatsAdapter.notifyDataSetChanged();
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
                    );
                    queue.add(stringRequest);
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
                    intent.putExtra("myID", myID);
                    intent.putExtra("senderName", chatNames.get(pos));
                    intent.putExtra("senderID", nameToId.get(chatNames.get(pos)));
                    startActivity(intent);
                }
            });

            loaded = true;
        }

    }
}
