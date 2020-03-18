package com.avans.movieapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.avans.movieapp.helpers.BinaryData;
import com.avans.movieapp.helpers.ICallback;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * NetworkTask.java (c) maurictg 2020
 */
public class NetworkTask extends AsyncTask<String, Void, BinaryData> {
    private final String TAG = NetworkTask.class.getSimpleName();
    private ICallback callback;

    private HashMap<String, String> headers;

    public NetworkTask(ICallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Executing onPreExecute");
        super.onPreExecute();
    }

    @Override
    protected BinaryData doInBackground(String... strings) {
        Log.d(TAG, "Executing doInBackground");

        String url = strings[0];

        try {
            Log.d(TAG, "Opening inputStream from given url ("+url+").");
            InputStream is = (InputStream)new URL(url).getContent();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            Log.d(TAG, "Flushed binary data. Returning BinaryData object");

            return new BinaryData(buffer.toByteArray());

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "Failed to get data: "+e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(BinaryData binaryData) {
        Log.d(TAG, "Executing onPostExecute. Calling callback.");
        super.onPostExecute(binaryData);

        if(binaryData != null)
            callback.callback(binaryData, true);
        else
            callback.callback(null, false);
    }

    @Override
    protected void onCancelled() {
        Log.d(TAG, "Executing onCancelled. Calling callback.");
        super.onCancelled();
        callback.callback(null, false);
    }
}
