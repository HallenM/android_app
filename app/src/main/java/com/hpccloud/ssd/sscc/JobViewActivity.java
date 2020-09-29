package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class JobViewActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_JOBS_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_view);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String token = localStorage.getString("token", "unknown").trim();
        int id_jobs = localStorage.getInt("job_id", 0);
        String name_jobs = localStorage.getString("job_name", "unknown").trim();

        TextView debugText = findViewById(R.id.textView14);
        debugText.setText(name_jobs);

        String url = "http://192.168.0.108:4000/api/1.0/fs/jobs/" + name_jobs + "/" + id_jobs + ".out?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/fs/jobs/" + name_jobs + "/" + id_jobs + ".out?access_token=" + token;
        getJobResult(url);
    }

    public void getJobResult(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForApplications = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resultResp = new JSONObject(response);
                    JSONObject subObj = new JSONObject(resultResp.getString("file"));
                    String result = subObj.getString("body");

                    TextView viewText = findViewById(R.id.textViewInfoView);
                    viewText.setMovementMethod(new ScrollingMovementMethod());
                    viewText.setText(result);

                    SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = localStorage.edit();
                    editor.remove("job_id");
                    editor.remove("job_name");
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                    TextView viewText = findViewById(R.id.textViewInfoView);
                    viewText.setText(e.toString());
                }
                Log.d(TAG, "Response was sent successful. Data was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        // Tag the request
        String TAG = "Tag_for_count_of_jobs_view";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        queue.start();
    }
}
