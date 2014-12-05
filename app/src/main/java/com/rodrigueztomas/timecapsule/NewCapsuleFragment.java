package com.rodrigueztomas.timecapsule;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
public class NewCapsuleFragment extends Fragment {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private VideoView videoPreview;


    //private Button btNewCapsule;
    private Button btSaveCapsule;
    private EditText etCapsuleName;
    private EditText etDescription;

    private byte[] imageInByte;

    private Switch isPublicSwitch;

    GPSTracker gps;

    public NewCapsuleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rodrigueztomas.timecapsule.R.layout.fragment_new_capsule, container, false);

        // Inflate the layout for this fragment
        imgPreview = (ImageView) rootView.findViewById(R.id.imageView);
        videoPreview = (VideoView) rootView.findViewById(R.id.videoPreview);
        isPublicSwitch = (Switch) rootView.findViewById(R.id.isPublic);
        etDescription = (EditText) rootView.findViewById(R.id.capsuleDescription);


//        btNewCapsule = (Button)rootView.findViewById(R.id.takePhoto);
//        btNewCapsule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                captureImage();
//            }
//        });



        etCapsuleName = (EditText) rootView.findViewById(R.id.capsuleTittle);

        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });


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
                    // create class object
                    gps = new GPSTracker(getActivity().getApplicationContext());

                    // accept if GPS enabled
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        MainActivity.capsuleList.add(etCapsuleName.getText().toString());
                        if(saveCapsuleToParse(etCapsuleName.getText().toString() , etDescription.getText().toString(), latitude, longitude))
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Capsule saved!", Toast.LENGTH_LONG).show();
                            Fragment frg0 = getFragmentManager().findFragmentByTag("Map");
                            if (frg0 == null)
                            {
                                frg0 = new MapContainerFragment();
                            }
                            FragmentManager fragmentManager0 = getFragmentManager();
                            fragmentManager0.beginTransaction().replace(com.rodrigueztomas.timecapsule.R.id.content_frame, frg0, "Map").commit();
                        }
                        else
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Couldn't save capsule.", Toast.LENGTH_LONG).show();

                        }

                        etCapsuleName.setText("");



                        // \n is for new line
                        //Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }








                    //Toast.makeText(getActivity(), "Capsule Saved!", Toast.LENGTH_LONG).show();

//                    if (MainActivity.map == null) {
//                        //Toast.makeText(getActivity(), "MAP NULL!", Toast.LENGTH_LONG).show();
//                        MainActivity.map = MapContainerFragment.fragment.getMap();
//                    }
//
//                    MarkerOptions marker = new MarkerOptions();
//                    BitmapDescriptor capsule_icon = BitmapDescriptorFactory.fromResource(R.drawable.capsule_icon);
//                    marker.icon(capsule_icon);
//                    MainActivity.map.addMarker(marker.position(new LatLng(MainActivity.map.getMyLocation().getLatitude(),MainActivity.map.getMyLocation().getLongitude())));
                }
            }
        });


        return rootView;
    }

    @Override
    public void onPause()
    {
        Log.d("ONPAUSE", "on pause33333");
        super.onPause();
    }



    /*
    * Capturing Camera Image will lauch camera app requrest image capture
    */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    /*
    *  Save Capsule to Parse.  return true if succesfull, false otherwise.
   */
    private boolean saveCapsuleToParse(String capsuleName, String description, double latitude, double longitude)
    {
        Log.d("Parse", "getting current user to save capsule to Parse.");
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            Log.d("Parse", "currentUser = " + currentUser.getUsername());

            ParseObject parseCapsule = new ParseObject("capsule");
            if(imageInByte != null)
            {
                ParseFile file = new ParseFile("image.png", imageInByte);
                parseCapsule.put("image", file);
            }



            parseCapsule.put("usernameObjectId", currentUser.getObjectId());

            //TODO: change description
            if(description == null)
            {
                parseCapsule.put("description", "Empty description.");
            }
            else
            {
                parseCapsule.put("description", description);
            }
            parseCapsule.put("title", capsuleName );
            if( isValidLocation(latitude, longitude) )
            {
                ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
                parseCapsule.put("location", point);
            }

            if(isPublicSwitch.isEnabled())
            {
                parseCapsule.put("privacy", "public");
            }
            else
            {
                parseCapsule.put("privacy", "private");
            }






            parseCapsule.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null)
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            });

        } else {
            Log.d("Parse", "currentUser is null");
            // show the signup or login screen
            return false;
        }



        return true;
    }



    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {


                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getActivity().getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }



    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            final BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            //
            //
            //
            // options.inSampleSize = 8;
            //options.inScaled = false;
            //options.inJustDecodeBounds = true;
            //Bitmap bitmap1 = BitmapFactory.decodeFile(fileUri.getPath(), options);

            //options.inJustDecodeBounds = false;
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            //int sampleSize = CameraHelper.calculateInSampleSize(options, width/2, height/2);

            options.inSampleSize = 12;

            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(fileUri.getPath());
                Log.d("EXIF", "new Exif Interface");
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.d("EXIF", "orientation " + orientation);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = CameraHelper.RotateBitmap(bitmap, 90);
                    Log.d("EXIF", "rotated 90");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = CameraHelper.RotateBitmap(bitmap, 180);
                    Log.d("EXIF", "rotated 180");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = CameraHelper.RotateBitmap(bitmap, 270);
                    break;

            }

            // bitmap
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            //bitmap = CameraHelper.RotateBitmap(bitmap, -90);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            imageInByte = stream1.toByteArray();

            imgPreview.setImageBitmap(bitmap);



        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    boolean isValidLocation(Double latitude, Double longitude)
    {
        return (latitude < 90 && latitude > -90 && longitude > -180 && longitude < 180);
    }

    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }


}
