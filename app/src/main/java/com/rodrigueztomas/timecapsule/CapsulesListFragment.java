package com.rodrigueztomas.timecapsule;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class CapsulesListFragment extends Fragment {

   private ListView lvCapsuleList;


    public CapsulesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.capsules_fragment, container, false);

        if(MainActivity.capsuleList.isEmpty())
        {
            for (int i = 1; i < 10; ++i) {
                MainActivity.capsuleList.add("Test Capsule#" + i);
            }
        }

        lvCapsuleList = (ListView) rootView.findViewById(R.id.myListView);

        CapsulesListArrayAdapter capsulesListArrayAdapter = new CapsulesListArrayAdapter(getActivity(), getActivity().getLayoutInflater(), MainActivity.capsuleList);

        lvCapsuleList.setAdapter(capsulesListArrayAdapter);

        return rootView;
    }


}
