package in.siddharthshah.interchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by siddharthshah on 9/8/17.
 */

public class PreferencesFragment extends Fragment implements View.OnClickListener {

    View view;
    AppCompatSpinner spinner;
    Button saveButton;
    ListViewCompat listViewCompat;

    String[] interests;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String[] languages = new String[]{"English", "Hindi", "Gujarati", "Spanish", "Chinese"};
        ArrayAdapter<String> languagesAdapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languages);
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(languagesAdapter);

        interests = new String[]{ "Harry Potter", "Marvel", "FIFA", "Manchester United", "US Politics", "DC"};
        ArrayAdapter<String> interestsAdapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, interests);
        listViewCompat.setAdapter(interestsAdapter);

    }

    @Override
    public void onClick(View view) {
        String languageSelected = spinner.getSelectedItem().toString();
        Set<String> interestSet = new HashSet<>();
        listViewCompat.getCheckedItemPositions();
        for (int i = 0; i < listViewCompat.getCount(); i++) {
            if (listViewCompat.isItemChecked(i)) {
                // Do whatever you need to in here to get data from
                // the item at index i in the ListView
                interestSet.add(interests[i]);
            }
        }
        if(interestSet.size() > 0) {

            Log.d("SAVE", "VIEW");
            Log.d("LANG", languageSelected);
            Log.d("INTERESTS", interestSet.toString());

            // send to server
            // TODO

            // save to local
            SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.app_preferences), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(getString(R.string.default_language), languageSelected);
            editor.putStringSet(getString(R.string.interests), interestSet);

            // set preferences as saved
            editor.putBoolean(getString(R.string.preferences_set), true);
            editor.commit();

            //move to new fragment
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.openChatFragment();
        } else {
            Toast.makeText(getActivity(), "Please select atleast one interest", Toast.LENGTH_SHORT).show();
        }
    }
}
