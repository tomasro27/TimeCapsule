package com.rodrigueztomas.timecapsule;



import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapContainerFragment extends Fragment {

    private MapFragment fragment;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            map.addMarker(new MarkerOptions().position(new LatLng(30.286144, -97.736880\)));
        }
    }
}