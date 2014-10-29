package com.rodrigueztomas.timecapsule;

/**
 * Created by tomasrodriguez on 10/29/14.
 */
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CapsulesListArrayAdapter extends BaseAdapter {

    private static class ViewHolder {
        TextView title;
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<String> capsules;

    public CapsulesListArrayAdapter(Context context, LayoutInflater inflater, ArrayList<String> capsules){
        this.context = context;
        this.inflater = inflater;
        this.capsules = capsules;
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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.title.setText(capsules.get(position));

        // Return the completed view to render on screen
        return convertView;
    }
}