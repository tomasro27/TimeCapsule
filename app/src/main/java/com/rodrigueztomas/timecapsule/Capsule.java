package com.rodrigueztomas.timecapsule;

import android.widget.ImageView;

import com.parse.ParseObject;

/**
 * Created by tomasrodriguez on 11/12/14.
 */
public class Capsule extends ParseObject {
    public ImageView image;
    public String description;
    public String name;

    Capsule (String name, String description, ImageView image)
    {
        this.name = name;
        this.description = description;
        this.image = image;

    }

}
