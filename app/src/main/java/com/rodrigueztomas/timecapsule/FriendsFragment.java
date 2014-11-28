package com.rodrigueztomas.timecapsule;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FriendsFragment extends Fragment {

    private Button bAdd;
    private ListView lvFriends;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(com.rodrigueztomas.timecapsule.R.layout.fragment_friends, container, false);

        bAdd = (Button) v.findViewById(R.id.addFriend);
        lvFriends = (ListView)v.findViewById(R.id.friendsList);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return v;
    }


}
