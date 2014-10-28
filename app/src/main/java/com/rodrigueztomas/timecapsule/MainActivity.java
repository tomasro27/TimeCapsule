package com.rodrigueztomas.timecapsule;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    private String[] titles;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.rodrigueztomas.timecapsule.R.layout.activity_main);
        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(com.rodrigueztomas.timecapsule.R.id.content_frame, new ThisFragment()).commit();
        }

        titles = new String[]{"This", "That", "The Other", "Map"};
        drawerLayout = (DrawerLayout) findViewById(com.rodrigueztomas.timecapsule.R.id.drawer_layout);
        drawerListView = (ListView) findViewById(com.rodrigueztomas.timecapsule.R.id.left_drawer);
        drawerListView.setAdapter(new ArrayAdapter<String>(this, com.rodrigueztomas.timecapsule.R.layout.drawer_list_item, titles));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, com.rodrigueztomas.timecapsule.R.drawable.ic_launcher, com.rodrigueztomas.timecapsule.R.string.hello_world, com.rodrigueztomas.timecapsule.R.string.app_name);

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

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
                Fragment frg0 = new ThisFragment();
                FragmentManager fragmentManager0 = getFragmentManager();
                fragmentManager0.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg0).commit();
                break;
            case 1: //That
                Fragment frg1 = new ThatFragment();
                FragmentManager fragmentManager1 = getFragmentManager();
                fragmentManager1.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg1).commit();
                break;
            case 2:
                Fragment frg2 = new TheOtherFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg2).commit();
                break;
            case 3:
                Fragment frg3 = new Map();
                FragmentManager fragmentManager3 = getFragmentManager();
                fragmentManager3.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg3).commit();
                break;
        }

        drawerListView.setItemChecked(position, true);
        getActionBar().setTitle(titles[position]);
        drawerLayout.closeDrawer(drawerListView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.rodrigueztomas.timecapsule.R.menu.main, menu);
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(com.rodrigueztomas.timecapsule.R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
