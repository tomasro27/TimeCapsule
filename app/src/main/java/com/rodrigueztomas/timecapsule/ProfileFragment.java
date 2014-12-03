package com.rodrigueztomas.timecapsule;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileFragment extends Fragment {

    private CircleImageView profilePicture;
    private TextView name;
    private TextView email;
    private Button logOut;

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
//    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200; don't need this one here
    public static final int MEDIA_TYPE_IMAGE = 1;
//    public static final int MEDIA_TYPE_VIDEO = 2; don't need this one here

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private byte[] imageInByte;




    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePicture = (CircleImageView) v.findViewById(R.id.profile_image);
        name = (TextView) v.findViewById(R.id.pName);
        email = (TextView) v.findViewById(R.id.pEmail);
        logOut = (Button) v.findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.logOut();
                Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();

        String parseName = (String) currentUser.get("name");
        if(parseName != null)
        {
            name.setText(parseName);
        }

        String parseEmail = (String) currentUser.get("email");
        if(parseEmail != null)
        {
            email.setText(parseEmail);
        }

        ParseFile profilePictureFile = (ParseFile) currentUser.get("profilePicture");
        if(profilePictureFile != null)
        {
                profilePictureFile.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        Bitmap bitmap;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inMutable = true;
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        //Canvas canvas = new Canvas(bitmap);
                        profilePicture.setImageBitmap(bitmap);
                    } else {
                        // something went wrong
                    }
                }
            });
        }



        profilePicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                captureImage();
                //saveProfilePictureToParse();

                return false;
            }
        });


        return v;
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


    /*
*  Save profilePicture to Parse.  return true if succesfull, false otherwise.
*/
    private boolean saveProfilePictureToParse()
    {
        Log.d("Parse", "getting current user to save capsule to Parse.");
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null)
        {
            Log.d("Parse", "currentUser = " + currentUser.getUsername());


            if(imageInByte != null)
            {
                ParseFile file = new ParseFile("image.png", imageInByte);
                currentUser.put("profilePicture", file);


                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null)
                            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            }

        }
        else
        {
            Log.d("Parse", "currentUser is null");
            // show the signup or login screen
            return false;
        }

        return true;
    }


    public void previewCapturedImage()
    {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            profilePicture.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();



            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            // bitmap
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap = CameraHelper.RotateBitmap(bitmap, -90);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);

            imageInByte = stream1.toByteArray();

            saveProfilePictureToParse();
            profilePicture.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }



}
