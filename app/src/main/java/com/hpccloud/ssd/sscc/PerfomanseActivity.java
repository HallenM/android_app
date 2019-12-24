package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PerfomanseActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_PERFOMANSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfomanse);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String token = localStorage.getString("token", "unknown").trim();
        //String url = /*192.168.0.101*/"http://hpccloud.ssd.sscc.ru:4000/api/1.0/projects?access_token=" + token;

        /*RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForApplications = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fillList(response);
                Log.d(TAG, "Response was sent successful. Data was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        // Tag the request
        String TAG = "Tag_for_count_of_applications_request";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        queue.start();*/
    }

    public void startMeasurePerfomanse (View view) {

        EditText data = findViewById(R.id.editTextNodes1);
        String nodes1 = data.getText().toString();
        data = findViewById(R.id.editTextNodes2);
        String nodes2 = data.getText().toString();
        data = findViewById(R.id.editTextName);
        String nameJobs = data.getText().toString();

       /* for (int i = 0; i < nodes2; i++) {

        }*/

        TextView debugText = findViewById(R.id.textView10);
        debugText.setText("Name: " + nameJobs + " node1: " + nodes1 + " node2: " + nodes2);

        Toast.makeText(this, "Start measure of perfomanse", Toast.LENGTH_SHORT).show();
    }
}
