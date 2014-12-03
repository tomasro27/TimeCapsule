package com.rodrigueztomas.timecapsule;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private String[] titles;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    public static ArrayList<String> capsuleList;
    public static GoogleMap map;
    public static GoogleMapOptions mapOptions;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.rodrigueztomas.timecapsule.R.layout.activity_main);
        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(com.rodrigueztomas.timecapsule.R.id.content_frame, new MapContainerFragment()).commit();
        }




        titles = new String[]{"Map", "Profile", "Friends", "CameraFragment", "Capsules"};
        drawerLayout = (DrawerLayout) findViewById(com.rodrigueztomas.timecapsule.R.id.drawer_layout);
        drawerListView = (ListView) findViewById(com.rodrigueztomas.timecapsule.R.id.left_drawer);
        drawerListView.setAdapter(new ArrayAdapter<String>(this, com.rodrigueztomas.timecapsule.R.layout.drawer_list_item, titles));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, com.rodrigueztomas.timecapsule.R.drawable.ic_launcher, com.rodrigueztomas.timecapsule.R.string.hello_world, com.rodrigueztomas.timecapsule.R.string.app_name);

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        capsuleList = new ArrayList<String>();

    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    private void selectItem(int position)
    {
        switch(position)
        {
            case 0://This
                Fragment frg0 = getFragmentManager().findFragmentByTag("Map");
                if (frg0 == null)
                {
                    frg0 = new MapContainerFragment();
                }
                FragmentManager fragmentManager0 = getFragmentManager();
                fragmentManager0.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg0, "Map").commit();
                break;
            case 1: //That
                Fragment frg1 = new ProfileFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                fragmentManager1.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg1).commit();
                break;
            case 2:
                Fragment frg2 = new FriendsFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg2).commit();
                break;
            case 3:
                Fragment frg3 = getFragmentManager().findFragmentByTag("Camera");
                if (frg3 == null)
                {
                    frg3 = new CameraFragment();
                }
                FragmentManager fragmentManager3 = getFragmentManager();
                fragmentManager3.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg3, "Camera").commit();
                break;

            case 4:
                Fragment frg4 = getFragmentManager().findFragmentByTag("Capsules");
                if (frg4 == null)
                {
                    frg4 = new CapsulesListFragment();
                }
                FragmentManager fragmentManager4 = getFragmentManager();
                fragmentManager4.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg4, "Capsules").commit();
                break;
        }

        drawerListView.setItemChecked(position, true);
        getActionBar().setTitle(titles[position]);
        drawerLayout.closeDrawer(drawerListView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(com.rodrigueztomas.timecapsule.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public void onPause() {
            //Log.d("ONPAUSE", "on pause11111");
            super.onPause();


        }

        @Override
        public void onStop() {
            Log.d("ONSTOP", "ON STOP");
            super.onStop();


        }

        @Override
        public void onResume() {
            Log.d("ONRESUME", "onRESUME");
            super.onResume();

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(com.rodrigueztomas.timecapsule.R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; //kilometers
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }



}
