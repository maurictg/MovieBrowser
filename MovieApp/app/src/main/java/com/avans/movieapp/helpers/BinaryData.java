package com.avans.movieapp.helpers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * BinaryData.java (c) maurictg 2020
 */
public class BinaryData {
    private final String TAG = BinaryData.class.getSimpleName();

    //Private variables
    private byte[] data;

    /**
     * Create new BinaryData instance
     * @param data Binary data, containing Drawable or String
     */
    public BinaryData(byte[] data) { this.data = data; }

    /**
     * Convert Drawable to BinaryData
     * @param drawable The drawable
     */
    public BinaryData(Drawable drawable) {
        if(drawable == null){
            this.data = new byte[0];
            Log.e(TAG, "Drawable is NULL");
            return;
        }

        Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
        this.data = os.toByteArray();
    }

    /**
     * Convert String to BinaryData
     * @param value The string
     */
    public BinaryData(String value) {
        this.data = value.getBytes();
    }

    /**
     * Get String from inner byte[]
     * @return String
     */
    @Override
    public String toString() {
        InputStream is = new ByteArrayInputStream(this.data);
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public int length() {
        return this.data.length;
    }

    /**
     * Get drawable from inner byte[]
     * @return Drawable
     */
    public Drawable toDrawable() {
        Log.d(TAG, "Converting to drawable");

        if(data.length == 0){
            Log.e(TAG, "Data size must be greater than 0");
            return null;
        }

        InputStream is = new ByteArrayInputStream(this.data);
        return Drawable.createFromStream(is, "Useless");
    }

    public byte[] getData() {
        return data;
    }
}
