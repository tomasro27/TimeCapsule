package com.rodrigueztomas.timecapsule;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class CapsulesListFragment extends Fragment {

   private ListView lvCapsuleList;
   private SharedPreferences sharedPrefs;


    public CapsulesListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(sharedPrefs.contains("capsuleList")){
            Log.d("debug", "contains");
            Gson gson = new Gson();
            String json = sharedPrefs.getString("capsuleList", null);
            Type type = new TypeToken<ArrayList<Object>>() {}.getType();
            ArrayList<Object> arrayList = gson.fromJson(json, type);

            //MainActivity.capsuleList = new ArrayList<String>();
            for (Object object : arrayList) {
                MainActivity.capsuleList.add(object != null ? object.toString() : null);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.capsules_fragment, container, false);

        if(MainActivity.capsuleList.isEmpty())
        {
            for (int i = 1; i < 3; ++i) {
                MainActivity.capsuleList.add("Test Capsule#" + i);
            }
        }

        lvCapsuleList = (ListView) rootView.findViewById(R.id.myListView);

        CapsulesListArrayAdapter capsulesListArrayAdapter = new CapsulesListArrayAdapter(getActivity(), getActivity().getLayoutInflater(), MainActivity.capsuleList);

        lvCapsuleList.setAdapter(capsulesListArrayAdapter);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("ONPAUSE", "on pause");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(MainActivity.capsuleList);
        editor.putString("capsuleList", json);
        editor.commit();

        MainActivity.capsuleList = new ArrayList<String>();
        Log.d("ONPAUSE", "on pause2");
    }
}
