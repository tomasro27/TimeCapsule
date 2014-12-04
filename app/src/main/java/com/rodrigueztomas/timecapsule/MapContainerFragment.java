package com.rodrigueztomas.timecapsule;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;


public class MapContainerFragment extends Fragment {

    public static MapFragment fragment;
    GPSTracker gps;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);


//        if(MainActivity.map != null)
//        {
//            Log.d("debug", "MAp is not null");
//
//            gps = new GPSTracker(getActivity().getApplicationContext());
//
//            if(gps.canGetLocation())
//            {
//                Log.d("mapFragment", "moving camera to gps location");
//                MainActivity.map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gps.getLatitude(), gps.getLongitude())));
//            }
//
//            ();
//
//
//
//        }

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MapsInitializer.initialize(getActivity().getApplicationContext());

        FragmentManager fm = getChildFragmentManager();

        if(MainActivity.mapOptions == null)
            MainActivity.mapOptions = new GoogleMapOptions();


        fragment = (MapFragment) fm.findFragmentById(R.id.map);

        if (fragment == null) {
            fragment = MapFragment.newInstance(MainActivity.mapOptions);
            fm.beginTransaction().add(R.id.map, fragment).commit();
        }


        MainActivity.map = fragment.getMap();

        Log.d("HASHMAP", "INITIALIZING... ");




//        if(MainActivity.map != null)
//        {
//            Log.d("debug", "MAp is not null");
//
//            gps = new GPSTracker(getActivity().getApplicationContext());
//
//            if(gps.canGetLocation())
//            {
//                Log.d("mapFragment", "moving camera to gps location");
//                MainActivity.map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gps.getLatitude(), gps.getLongitude())));
//            }
//
//            drawMapCapsules();
//
//
//
//        }





    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.map == null) {
            MainActivity.map = fragment.getMap();
        }

        Log.d("debug", "MAp is not null");


        if(MainActivity.map != null)
        {
            gps = new GPSTracker(getActivity().getApplicationContext());

            if(gps.canGetLocation())
            {
                Log.d("mapFragment", "moving camera to gps location");
                MainActivity.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 15));
            }

            Log.d("drawCapsules", "Drawing capsules inside onResume()");
            drawMapCapsules();
        }




    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//
//        getActivity().getMenuInflater().inflate(R.menu.map_menu, menu);
//
//    }

    void drawMapCapsules()
    {
        gps = new GPSTracker(getActivity().getApplicationContext());


        // accept if GPS enabled
        if(gps.canGetLocation())
        {

            ParseUser currentUser = ParseUser.getCurrentUser();
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            ParseGeoPoint currentLocation = new ParseGeoPoint(latitude, longitude);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("capsule");
            query.whereNear("location", currentLocation);
            query.setLimit(100); // TODO: change this
            query.whereEqualTo("usernameObjectId", currentUser.getObjectId());
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> results, ParseException e) {
                    if (e != null) {
                        // There was an error
                        Toast.makeText(getActivity().getApplicationContext(),
                                e.toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        // results have all the Posts the current user liked.

                        MarkerOptions marker = new MarkerOptions();
                        BitmapDescriptor capsule_icon = BitmapDescriptorFactory.fromResource(R.drawable.time_capsule_marker);
                        marker.icon(capsule_icon);


                        for (int i = 0; i < results.size(); i++) {

                            ParseGeoPoint location = (ParseGeoPoint) results.get(i).get("location");
                            if (location != null) {
                                Marker capsule =  MainActivity.map.addMarker(marker.position(new LatLng(location.getLatitude(),
                                                                                                location.getLongitude())));
                                MainActivity.markerIdToParseId.put(capsule.getId(), results.get(i).getObjectId().toString());
                            }
                        }
                    }
                }
            });


            MainActivity.map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    double distance = MainActivity.distFrom(gps.getLatitude(), gps.getLongitude(), marker.getPosition().latitude, marker.getPosition().longitude);

                    if(distance < .5)
                    {
                        Log.d("CapsuleMarker", "Distance within 500 meters");
                        Bundle args = new Bundle();
                        args.putString("objectId", MainActivity.markerIdToParseId.get(marker.getId()));
                        CapsuleViewFragment fragment = new CapsuleViewFragment ();
                        fragment.setArguments(args);

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.content_frame, fragment, "CapsuleViewFragment").commit();

                        //Toast.makeText(getActivity().getApplicationContext(), "You can open this capsule", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    else
                    {
                        Log.d("CapsuleMarker", "Distance > 500 meters");
                        Toast.makeText(getActivity().getApplicationContext(), "You can't open this capsule. You are too far away!", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });
        }

        MainActivity.map.setMyLocationEnabled(true);
    }




}