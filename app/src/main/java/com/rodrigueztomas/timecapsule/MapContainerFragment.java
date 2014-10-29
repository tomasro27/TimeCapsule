package com.rodrigueztomas.timecapsule;



import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapContainerFragment extends Fragment {

    public static MapFragment fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();

        if(MainActivity.mapOptions == null)
            MainActivity.mapOptions = new GoogleMapOptions();


        fragment = (MapFragment) fm.findFragmentById(R.id.map);

        if (fragment == null) {
            fragment = MapFragment.newInstance(MainActivity.mapOptions);
            fm.beginTransaction().add(R.id.map, fragment).commit();

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.map == null) {
            MainActivity.map = fragment.getMap();

        }

        MarkerOptions marker = new MarkerOptions();
        BitmapDescriptor capsule_icon = BitmapDescriptorFactory.fromResource(R.drawable.capsule_icon);
        marker.icon(capsule_icon);
         MainActivity.map.addMarker(marker.position(new LatLng(30.286144, -97.736880)));

        MainActivity.map.addMarker(marker.position(new LatLng(30.286000, -97.736700)));

        MainActivity.map.addMarker(marker.position(new LatLng(30.284000, -97.736600)));

        MainActivity.map.setMyLocationEnabled(true);


    }

}