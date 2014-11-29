package com.rodrigueztomas.timecapsule;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by tomasrodriguez on 11/28/14.
 */
public class AddFriendDialogFragment extends DialogFragment {

    private Button cancel, ok;
    private boolean m_status;
    private EditText etEmailSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_dialog_add_friend, container, false);

        etEmailSearch = (EditText) v.findViewById(R.id.emailSearch);

        Button cancel = (Button) v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        Button ok = (Button) v.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("friendRequest", "ok: button Clicked!");
                String emailInvite = etEmailSearch.getText().toString();
                Log.d("friendRequest", "ok: email: " + emailInvite);
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInvite).matches())
                {
                    etEmailSearch.setError("Inavlid email address.");
                }
                else if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailInvite).matches())
                {
                    Log.d("friendRequest", "ok: valid email address");

                    ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                    userQuery.whereEqualTo("email", emailInvite);
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
//                    query.whereEqualTo("email", emailInvite);
                    Log.d("friendRequest", "ok: query for User with email equal to " + emailInvite);


                    userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            Log.d("friendRequest", "ok: done Querying User");
                            if (e == null && parseUser != null) {
                                Log.d("friendRequest", "ok: user not null");
                                final ParseUser friend;
                                friend = parseUser;
                                final ParseUser currentUser = ParseUser.getCurrentUser();

                                Log.d("friendRequest", "ok: creating query for FriendRequest object");

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
                                query.whereEqualTo("fromUser", friend.getObjectId());
                                query.whereEqualTo("toUser", currentUser.getObjectId());

                                Log.d("friendRequest", "ok: query created");
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> results, ParseException e) {
                                        if (e == null) {
                                            Log.d("friendRequest", "ok: query done for FriendRequest");

                                            if (!results.isEmpty()) {
                                                Log.d("friendRequest", "ok: FriendRequest already exist!");


                                                String status = (String) results.get(0).get("status");
                                                if (status.equals("Pending")) {
                                                    etEmailSearch.setError("Friend request already sent to this user!");
                                                } else if (status.equals("Accepted")) {
                                                    etEmailSearch.setError("You are already friends with this user!");
                                                }

                                            } else {
                                                Log.d("friendRequest", "ok: creating new FriendRequest");

                                                ParseObject friendRequest = new ParseObject("FriendRequest");
                                                friendRequest.put("fromUser", currentUser.getObjectId());
                                                friendRequest.put("toUser", friend.getObjectId());
                                                friendRequest.put("status", "Pending");
                                                friendRequest.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Log.d("friendRequest", "ok: FriendRequest saved!");
                                                            Toast.makeText(getActivity().getApplicationContext(), "Friend request sent!", Toast.LENGTH_LONG).show();
                                                            dismiss();

                                                        } else if (e == null) {
                                                            Log.d("friendRequest", "ok: exception saving FriendRequest: " + e.toString());
                                                            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });


                                            }
                                        } else {
                                            Log.d("friendRequest", "ok: query exception for FriendRequest: " + e.toString());

                                            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                            } else if (parseUser == null && e == null) {
                                Log.d("friendRequest", "user null");
                                etEmailSearch.setError("No user found with that email address.");
                            } else if (e != null) {
                                Log.d("friendRequest", "parse exception: " + e.toString());
                            }
                        }

                    });



                }
                //TODO: show message if friend is already on the friend lists.




            }
        });


        Dialog dialog = getDialog();
        dialog.setTitle("Add a friend");

        return v;
    }


}