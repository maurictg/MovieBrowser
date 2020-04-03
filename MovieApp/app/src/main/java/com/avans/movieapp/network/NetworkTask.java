package com.avans.movieapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.avans.movieapp.helpers.BinaryData;
import com.avans.movieapp.helpers.RequestMethod;
import com.avans.movieapp.interfaces.ICallback;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * NetworkTask.java (c) maurictg 2020
 */
public class NetworkTask extends AsyncTask<String, Void, BinaryData> {
    private final String TAG = NetworkTask.class.getSimpleName();
    private final ICallback callback;

    private HashMap<String, String> headers;
    private HashMap<String, String> parameters;
    private HashMap<String, String> formData;
    private RequestMethod requestMethod;

    /**
     * Create new NetworkTask. Default requestMethod set to GET
     *
     * @param callback
     */
    public NetworkTask(ICallback callback) {
        this(RequestMethod.GET, callback);
    }

    /**
     * Create new NetworkTask.
     *
     * @param method   The HTTPMethod like GET, POST, PUT etc.
     * @param callback The callback method. You can use a lambda here:
     *                 <code>new NetworkTask((data, success) -> { your code here })</code>
     */
    public NetworkTask(RequestMethod method, ICallback callback) {
        this.callback = callback;
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();
        this.formData = new HashMap<>();
        this.requestMethod = method;
    }

    //Add headers, parameters or HTTP form data
    public void addHeader(String title, String value) {
        this.headers.put(title, value);
    }

    public void addParameter(String title, String value) {
        this.parameters.put(title, value);
    }

    public void addFormData(String title, String value) {
        this.formData.put(title, value);
    }

    /**
     * Set request method
     *
     * @param requestMethod HTTP method like GET, POST e.d.
     */
    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Executing onPreExecute");
        super.onPreExecute();
    }

    @Override
    protected BinaryData doInBackground(String... strings) {
        Log.d(TAG, "Executing doInBackground");

        StringBuilder url = new StringBuilder(strings[0]);
        if (parameters.size() > 0) {
            parameters.forEach((k, v) -> {
                try {
                    url.append("&").append(URLEncoder.encode(k, "UTF-8")).append("=").append(URLEncoder.encode(v, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Failed to encode URL data");
                    e.printStackTrace();
                }
            });
        }

        StringBuilder form_data = new StringBuilder();
        if (formData.size() > 0) {
            formData.forEach((k, v) -> {
                try {
                    form_data.append("&").append(URLEncoder.encode(k, "UTF-8")).append("=").append(URLEncoder.encode(v, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Failed to URLEncode data");
                    e.printStackTrace();
                }
            });
        }

        try {
            Log.d(TAG, "Opening connection to given url (" + url + ") Http-" + requestMethod.get());
            HttpURLConnection connection = (HttpURLConnection) new URL(url.toString().replaceFirst("&", "?")).openConnection();

            //Add headers to connection
            headers.forEach(connection::addRequestProperty);
            connection.setRequestMethod(requestMethod.get());

            //Write request if needed
            if (formData.size() > 0) {
                byte[] postData = new BinaryData(form_data.toString().replaceFirst("&", "")).getData();
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.addRequestProperty("charset", "utf-8");
                connection.addRequestProperty("Content-Length", Integer.toString(postData.length));
                connection.setUseCaches(false);

                //Write data using OutputStream
                try (DataOutputStream ws = new DataOutputStream(connection.getOutputStream())) {
                    ws.write(postData);
                }
            }

            //Read response
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            Log.d(TAG, "Flushed binary data. Returning BinaryData object");

            //Create and return new BinaryData object. See BinaryData.java
            return new BinaryData(buffer.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to get data: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(BinaryData binaryData) {
        Log.d(TAG, "Executing onPostExecute. Calling callback.");
        super.onPostExecute(binaryData);

        if (binaryData != null) {
            callback.callback(binaryData, true);
        } else {
            callback.callback(null, false);
        }
    }

    @Override
    protected void onCancelled() {
        Log.d(TAG, "Executing onCancelled. Calling callback.");
        super.onCancelled();
        callback.callback(null, false);
    }
}
