package com.rodrigueztomas.timecapsule;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class CapsulesListFragment extends Fragment {

   private ListView lvCapsuleList;
   private SharedPreferences sharedPrefs;
   GPSTracker gps;


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
        final View rootView = inflater.inflate(R.layout.capsules_fragment, container, false);

        if(MainActivity.capsuleList.isEmpty())
        {
            for (int i = 1; i < 3; ++i) {
                MainActivity.capsuleList.add("Test Capsule#" + i);
            }
        }

        lvCapsuleList = (ListView) rootView.findViewById(R.id.myListView);

//        CapsulesListArrayAdapter capsulesListArrayAdapter = new CapsulesListArrayAdapter(getActivity(), getActivity().getLayoutInflater(), MainActivity.capsuleList);
//
//        lvCapsuleList.setAdapter(capsulesListArrayAdapter);

        //ParseUser user = ParseUser.getCurrentUser();
        gps = new GPSTracker(getActivity().getApplicationContext());


        // accept if GPS enabled
        if(gps.canGetLocation()) {

            ParseUser currentUser = ParseUser.getCurrentUser();

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            final ParseGeoPoint currentLocation = new ParseGeoPoint(latitude, longitude);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("capsule");
            query.whereNear("location", currentLocation);
            query.whereEqualTo("usernameObjectId", currentUser.getObjectId());
            query.setLimit(100);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> results, ParseException e) {
                    if (e != null) {
                        // There was an error
                        Toast.makeText(getActivity().getApplicationContext(),
                                e.toString(), Toast.LENGTH_LONG).show();
                    } else {

                        // results have all the Posts the current user liked.
                        CapsulesListArrayAdapter adapter = new CapsulesListArrayAdapter(getActivity().getApplicationContext(), getActivity().getLayoutInflater(), results, currentLocation);
                        ListView listView = (ListView) rootView.findViewById(R.id.myListView);
                        listView.setAdapter(adapter);
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                            }
//                        });
                    }
                }
            });
        }




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
