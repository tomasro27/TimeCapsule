package com.rodrigueztomas.timecapsule;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by tomasrodriguez on 11/17/14.
 */
public class CapsuleViewFragment extends Fragment {

    private ParseImageView capsuleImage;
    private TextView capsuleTitle;
    private TextView capsuleDescription;
    private TextView capsuleIsPublic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_capsuleview, container, false);

        Bundle b = getArguments();
        String objectId = b.getString("objectId");

        capsuleImage = (ParseImageView) v.findViewById(R.id.capsuleImageView);
        capsuleTitle = (TextView) v.findViewById(R.id.capsuleTittleText);
        capsuleDescription = (TextView) v.findViewById(R.id.capsuleDescriptionText);
        capsuleIsPublic = (TextView) v.findViewById(R.id.isCapsulePublicText);






        ParseQuery<ParseObject> query = ParseQuery.getQuery("capsule");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject capsule, ParseException e) {
                if (e == null) {

                    capsuleTitle.setText(capsule.getString("title"));
                    capsuleDescription.setText(capsule.getString("description"));
                    capsuleIsPublic.setText(capsuleIsPublic.getText() + capsule.getString("privacy"));
                    // object retrieved succesfully
                    final ParseFile capsuleImageFile = (ParseFile) capsule.get("image");
                    capsuleImageFile.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume
                                Bitmap bitmap;
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inMutable = true;
                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                                //Canvas canvas = new Canvas(bitmap);
                                //capsuleImage.setLayoutParams(new TableRow.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
                                //capsuleImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                capsuleImage.setImageBitmap(bitmap);
                            } else {
                                // something went wrong
                            }
                        }
                    });

                } else {
                    // something went wrong
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("capsule", "ERROR retrieving capsule image: " + e.toString());
                }
            }
        });




        return v;
    }
}
