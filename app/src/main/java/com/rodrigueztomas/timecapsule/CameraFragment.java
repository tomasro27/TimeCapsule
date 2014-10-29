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
import android.widget.ImageView;
import android.widget.ListView;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class CameraFragment extends Fragment {
    private Button bt;
    private ListView lv;
    private ImageView iv;

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
