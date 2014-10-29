package com.rodrigueztomas.timecapsule;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class CameraFragment extends Fragment {
    // TODO: change variable names............
    private Button bt;
    private ListView lv;
    private ImageView iv;
    private Button btSaveCapsule;
    private EditText etCapsuleName;

    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rodrigueztomas.timecapsule.R.layout.fragment_camera, container, false);

        // Inflate the layout for this fragment
        iv = (ImageView) rootView.findViewById(R.id.imageView);
        bt = (Button)rootView.findViewById(R.id.takePhoto);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

            }
        });

        etCapsuleName = (EditText) rootView.findViewById(R.id.capsuleTittle);


        btSaveCapsule = (Button) rootView.findViewById(R.id.saveCapsule);
        btSaveCapsule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(etCapsuleName.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Name Cannot be Empty!", Toast.LENGTH_LONG).show();
                }
                else if(MainActivity.capsuleList.contains(etCapsuleName.getText().toString()))
                {
                    Toast.makeText(getActivity(), "Capsule Already exists", Toast.LENGTH_LONG).show();
                }
                else
                {
                    MainActivity.capsuleList.add(etCapsuleName.getText().toString());
                    etCapsuleName.setText("");
                    Toast.makeText(getActivity(), "Capsule Saved!", Toast.LENGTH_LONG).show();

                    if (MainActivity.map == null) {
                        //Toast.makeText(getActivity(), "MAP NULL!", Toast.LENGTH_LONG).show();
                        MainActivity.map = MapContainerFragment.fragment.getMap();
                    }

                    MarkerOptions marker = new MarkerOptions();
                    BitmapDescriptor capsule_icon = BitmapDescriptorFactory.fromResource(R.drawable.capsule_icon);
                    marker.icon(capsule_icon);
                    MainActivity.map.addMarker(marker.position(new LatLng(MainActivity.map.getMyLocation().getLatitude(),MainActivity.map.getMyLocation().getLongitude())));
                }
            }
        });


        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0) {
            Bitmap theImage = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(theImage);
        }
    }



}
