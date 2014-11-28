package com.rodrigueztomas.timecapsule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tomasrodriguez on 11/28/14.
 */
public class FriendsListArrayAdapter extends BaseAdapter {

    private static class ViewHolder {
        CircleImageView profilePic;
        TextView friendName;
    }

    Context context;
    LayoutInflater inflater;
    List<ParseObject> friends;

    public FriendsListArrayAdapter(Context context, LayoutInflater inflater, List<ParseObject> friends){
        this.context = context;
        this.inflater = inflater;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.friend_item, null);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.friendName);
            viewHolder.profilePic = (CircleImageView) convertView.findViewById(R.id.friendPP);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        if (friends.get(position).getString("name") != null )
            viewHolder.friendName.setText(friends.get(position).getString("title"));
        else
            viewHolder.friendName.setText("NULL");


        // Return the completed view to render on screen
        return convertView;
    }
}
