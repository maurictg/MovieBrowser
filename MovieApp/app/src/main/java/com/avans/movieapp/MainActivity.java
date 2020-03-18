package com.avans.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.avans.movieapp.helpers.BinaryData;
import com.avans.movieapp.helpers.ICallback;
import com.avans.movieapp.network.NetworkTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Example of how to use NetworkTask and ICallback. This is of course just to show how my class works
        NetworkTask nt = new NetworkTask(new ICallback() {
            @Override
            public void callback(Object data, boolean success) {
                //data is of type BinaryData
                BinaryData r = (BinaryData)data;
                String result = r.toString();
            }
        });
        nt.execute("https://helloacm.com/api/random/?n=128");

        //You can also do this with a lambda:
        NetworkTask nt2 = new NetworkTask((data, success) -> {
            if(success) { //success indicates if there is no network error
                BinaryData r = (BinaryData)data;
                String result = r.toString();

                //If result (data) is an image, use:
                //Drawable d = r.toDrawable();
            }
        });
        nt2.execute("https://helloacm.com/api/random/?n=128");

        //We can also use ICallback ourselves:
        testfunctie((data, success) -> {
            String result = (String)data;
        });

    }

    private void testfunctie(ICallback callback) {
        callback.callback("Dit is een string", true);
    }
}
