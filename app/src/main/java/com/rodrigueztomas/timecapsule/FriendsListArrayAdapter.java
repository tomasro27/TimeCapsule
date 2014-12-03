package com.rodrigueztomas.timecapsule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tomasrodriguez on 11/28/14.
 */
public class FriendsListArrayAdapter extends BaseAdapter {

    private static class ViewHolder {
        CircleImageView profilePic;
        TextView friendName;
        Button accept;
        Button deny;
        TextView pending;
    }

    Context context;
    LayoutInflater inflater;
    List<ParseObject> friends;

    public FriendsListArrayAdapter(Context context, LayoutInflater inflater, List<ParseObject> friends){

        Log.d("friends", "adapter constructor: friends: " + friends.size());
        this.context = context;
        this.inflater = inflater;
        this.friends = friends;

    }

    @Override
    public int getCount() {
        return friends.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.d("friends", "FriendsListArrayAdapter: position: " + position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.friend_item, null);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.friendName);
            viewHolder.profilePic = (CircleImageView) convertView.findViewById(R.id.friendPP);
            viewHolder.pending = (TextView) convertView.findViewById(R.id.pending);

            viewHolder.accept = (Button) convertView.findViewById(R.id.acceptFriend);
            viewHolder.deny = (Button) convertView.findViewById(R.id.denyFriend);

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /**
        *   Retrieve from Parse friends name and profile picture.
        **/

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(friends.get(position).getString("fromUser").equals(currentUser.getObjectId()))
        {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(friends.get(position).getString("toUser"), new GetCallback<ParseUser>()
            {
                public void done(ParseUser friend, ParseException e)
                {
                    viewHolder.friendName.setText(friend.getString("name"));

                    ParseFile profilePictureFile = (ParseFile) friend.get("profilePicture");
                    if(profilePictureFile != null)
                    {
                        profilePictureFile.getDataInBackground(new GetDataCallback()
                        {
                            public void done(byte[] data, ParseException e)
                            {
                                if (e == null)
                                {

                                    // data has the bytes for the resume
                                    Bitmap bitmap;
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inMutable = true;
                                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                                    //Canvas canvas = new Canvas(bitmap);
                                    viewHolder.profilePic.setImageBitmap(bitmap);
                                } else {
                                    // something went wrong
                                }
                            }
                        });
                    }

                }
            });


            if(friends.get(position).getString("status").equals("Pending")) {
                viewHolder.accept.setVisibility(View.GONE);
                viewHolder.deny.setVisibility(View.GONE);
                viewHolder.pending.setVisibility(View.VISIBLE);
            }
        }
        else if(friends.get(position).getString("toUser").equals(currentUser.getObjectId()))
        {
            //TODO: repeating code in both if statements... get user outside, and write the code block once
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(friends.get(position).getString("fromUser"), new GetCallback<ParseUser>()
            {
                public void done(ParseUser friend, ParseException e)
                {
                    viewHolder.friendName.setText(friend.getString("name"));

                    ParseFile profilePictureFile = (ParseFile) friend.get("profilePicture");
                    if(profilePictureFile != null)
                    {
                        profilePictureFile.getDataInBackground(new GetDataCallback()
                        {
                            public void done(byte[] data, ParseException e)
                            {
                                if (e == null)
                                {

                                    // data has the bytes for the resume
                                    Bitmap bitmap;
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inMutable = true;
                                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                                    //Canvas canvas = new Canvas(bitmap);
                                    viewHolder.profilePic.setImageBitmap(bitmap);
                                } else {
                                    // something went wrong
                                }
                            }
                        });
                    }

                }
            });

            if(friends.get(position).getString("status").equals("Pending"))
            {
                viewHolder.pending.setVisibility(View.GONE);
                viewHolder.accept.setVisibility(View.VISIBLE);
                viewHolder.deny.setVisibility(View.VISIBLE);

                viewHolder.accept.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ParseObject friendRequest = friends.get(position);
                        friendRequest.put("status", "Accepted");
                        friendRequest.saveInBackground(new SaveCallback()
                        {
                            @Override
                            public void done(ParseException e)
                            {
                                if(e == null)
                                {
                                    Toast.makeText(context, "Friend request accepted!", Toast.LENGTH_LONG).show();
                                    viewHolder.deny.setVisibility(View.GONE);
                                    viewHolder.accept.setVisibility(View.GONE);
                                }
                                else
                                    Toast.makeText(context, e.toString() , Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });

                viewHolder.deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseObject friendRequest = friends.get(position);
                        friendRequest.put("status", "Denied");
                        friendRequest.saveInBackground(new SaveCallback()
                        {
                            @Override
                            public void done(ParseException e)
                            {
                                if(e == null)
                                {
                                    Toast.makeText(context, "Friend request denied!", Toast.LENGTH_LONG).show();
                                    viewHolder.deny.setVisibility(View.GONE);
                                    viewHolder.accept.setVisibility(View.GONE);
                                }
                                else
                                    Toast.makeText(context, e.toString() , Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
            }

        }


        // Populate the data into the template view using the data object
        //ParseUser userFriend = ParseUser.getQuery(friends.get(position).getString("fromUser"))
        //String sFriendName = friends.get(position)


        // Return the completed view to render on screen
        return convertView;
    }
}
