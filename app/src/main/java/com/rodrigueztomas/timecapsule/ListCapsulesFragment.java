package com.rodrigueztomas.timecapsule;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class ListCapsulesFragment extends Fragment {

    private ListView myListView;
    private String[] strListView;


    public ListCapsulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.rodrigueztomas.timecapsule.R.layout.fragment_that, container, false);
        myListView = (ListView) rootView.findViewById(R.id.myListView);
        strListView = getResources().getStringArray(R.array.my_data_list);
        ArrayAdapter<String> objAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, strListView);
        myListView.setAdapter(objAdapter);

        return rootView;
    }


}
