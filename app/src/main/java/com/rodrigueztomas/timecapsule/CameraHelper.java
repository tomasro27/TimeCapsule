package com.rodrigueztomas.timecapsule;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by tomasrodriguez on 11/27/14.
 */
public class CameraHelper {

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
