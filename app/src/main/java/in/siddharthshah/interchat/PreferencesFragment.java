package in.siddharthshah.interchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.SortedList;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by siddharthshah on 9/8/17.
 */

public class PreferencesFragment extends Fragment implements View.OnClickListener {

    View view;
    AppCompatSpinner spinner;
    Button saveButton;
    ListViewCompat listViewCompat;
    List<String> finalInterestList;

    HashMap<String, String> langToCode = new HashMap<>();
    ArrayList<String> languages = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Locale[] locales = Locale.getAvailableLocales();
        for(Locale locale : locales) {
            languages.add(locale.getDisplayName());
            langToCode.put(locale.getDisplayName(), locale.toLanguageTag());
        }
        Collections.sort(languages);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preferences, container, false);
        spinner = view.findViewById(R.id.spinner_language);
        listViewCompat = view.findViewById(R.id.listview_interests);
        saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayAdapter<String> languagesAdapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(languagesAdapter);

        String[] interests = new String[] {"Education", "Dance", "Music", "Arts", "Politics", "Careers", "Movies", "Hackathon", "Fashion", "Sports"};
        finalInterestList = Arrays.asList(interests);
        ArrayAdapter<String> interestsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, interests);
        listViewCompat.setAdapter(interestsAdapter);

        SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
        boolean prefSet = sp.getBoolean(getString(R.string.preferences_set), true);

        if(prefSet) {
            String languageTag = sp.getString(getString(R.string.default_language), "");
            Set<String> interestSet = sp.getStringSet(getString(R.string.interests), new HashSet<String>());
            List<String> interestList =new ArrayList(interestSet);

            String displayName = Locale.forLanguageTag(languageTag).getDisplayName();
            int pos = languages.indexOf(displayName);
            spinner.setSelection(pos);

            for(String interest : interestList) {
                listViewCompat.setItemChecked(finalInterestList.indexOf(interest), true);
            }
        }

    }

    @Override
    public void onClick(View view) {
        final String languageSelected = spinner.getSelectedItem().toString();
        final Set<String> interestSet = new HashSet<>();
        listViewCompat.getCheckedItemPositions();
        final MainActivity mainActivity = (MainActivity) getActivity();

        for (int i = 0; i < listViewCompat.getCount(); i++) {
            if (listViewCompat.isItemChecked(i)) {
                // Do whatever you need to in here to get data from
                // the item at index i in the ListView
                interestSet.add(finalInterestList.get(i));
            }
        }

        if(interestSet.size() > 0) {

            // send to server
            final String langCode = langToCode.get(languageSelected);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url ="https://interchat-backend.appspot.com/user/add";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // save to local
                            SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(getString(R.string.default_language), langCode);
                            editor.putStringSet(getString(R.string.interests), interestSet);

                            // set preferences as saved
                            editor.putBoolean(getString(R.string.preferences_set), true);
                            editor.commit();

                            //move to new fragment
                            mainActivity.openChatFragment();
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
                        jsonObject.put("uid", mainActivity.uid);
                        jsonObject.put("email", mainActivity.email);

                        JSONObject innerJsonObject = new JSONObject();
                        innerJsonObject.put("name", mainActivity.name);
                        innerJsonObject.put("pref-lang", langCode);
                        JSONArray jsonArray = new JSONArray();
                        for(String interest: interestSet){
                            jsonArray.put(interest);
                        }
                        innerJsonObject.put("interests", jsonArray);

                        jsonObject.put("data",innerJsonObject);

                        return jsonObject.toString().getBytes();
                    } catch (JSONException e){
                        e.printStackTrace();
                        return "{ }".getBytes();
                    }
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            Toast.makeText(getActivity(), "Please select atleast one interest", Toast.LENGTH_SHORT).show();
        }
    }

}
