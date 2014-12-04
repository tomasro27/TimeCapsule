package com.rodrigueztomas.timecapsule;

/**
 * Created by tomasrodriguez on 10/29/14.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class CapsulesListArrayAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView title;
        TextView distance;
    }

    Context context;
    LayoutInflater inflater;
    List<ParseObject> capsules;
    ParseGeoPoint currentLocation;

    public CapsulesListArrayAdapter(Context context, LayoutInflater inflater, List<ParseObject> capsules, ParseGeoPoint currentLocation){
        this.context = context;
        this.inflater = inflater;
        this.capsules = capsules;
        this.currentLocation = currentLocation;
    }

    @Override
    public int getCount() {
        return capsules.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.capsule_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.rTitle);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
            if (capsules.get(position).getString("title") != null )
                viewHolder.title.setText(capsules.get(position).getString("title"));
            else
                viewHolder.title.setText("NULL");

        ParseGeoPoint itemLocation = (ParseGeoPoint) capsules.get(position).get("location");


        Double distance = MainActivity.distFrom(itemLocation.getLatitude(), itemLocation.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude());
        distance = (double) Math.round(distance * 100) / 100;

        viewHolder.distance.setText(distance.toString() + " Km");

        // Return the completed view to render on screen
        return convertView;
    }
}