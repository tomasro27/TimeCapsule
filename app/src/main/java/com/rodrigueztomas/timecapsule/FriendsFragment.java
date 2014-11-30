package com.rodrigueztomas.timecapsule;



import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FriendsFragment extends Fragment {

    private Button bAdd;
    private ListView lvFriends;


    //FriendsListArrayAdapter adapter;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(com.rodrigueztomas.timecapsule.R.layout.fragment_friends, container, false);

        bAdd = (Button) v.findViewById(R.id.addFriend);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendDialogFragment addFriendDialog = new AddFriendDialogFragment();
                addFriendDialog.show(getFragmentManager(), "Diag");

            }
        });

        final List<ParseObject> allFriends;

        lvFriends = (ListView)v.findViewById(R.id.friendsList);

        allFriends = new ArrayList<ParseObject>();



        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("fromUser", currentUser.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null && !parseObjects.isEmpty())
                {
                    allFriends.addAll(parseObjects);
                }

            }
        });

        query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("toUser", currentUser.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null && !parseObjects.isEmpty())
                {
                    allFriends.addAll(parseObjects);

                    Log.d("friends", "allFriends length: " + allFriends.size());
                    Log.d("friends", "friends(0):name: " + allFriends.get(0).getString("status"));


                }
                Log.d("friends", "allFriends length2: " + allFriends.size());
                FriendsListArrayAdapter adapter = new FriendsListArrayAdapter(
                        getActivity().getApplicationContext(),
                        getActivity().getLayoutInflater(),
                        allFriends);
                Log.d("friends", "allFriends length2: " + allFriends.size());


                ListView lvFriends = (ListView) v.findViewById(R.id.friendsList);
                lvFriends.setAdapter(adapter);


            }
        });

//        Log.d("friends", "allfriends size2: " + allFriends.size());
//
//
//
//        Log.d("friends", "allfriends size3: " + allFriends.size());
//




        return v;
    }

}
