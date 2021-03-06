package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApplicationsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String token = localStorage.getString("token", "unknown").trim();
        String url = "http://192.168.0.108:4000/api/1.0/projects?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/projects?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
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
        queue.start();
    }

    public void fillList(String JSONstr) {
        String names_apps[];
        int id_apps[];
        try {
            JSONObject resultResp = new JSONObject(JSONstr);
            JSONArray responseArr = resultResp.getJSONArray("projects");
            int count = 0;
            for (int i = 0; i < responseArr.length(); i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                if(jsonValue.getString("type").equals("app"))
                {
                    count++;
                }
            }
            names_apps = new String[count];
            id_apps = new int[count];
            for (int i = 0, j = 0; i < responseArr.length() & j < count; i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                if(jsonValue.getString("type").equals("app"))
                {
                    names_apps[j] = jsonValue.getString("name");
                    id_apps[j] = jsonValue.getInt("id");
                    j++;
                }
            }

            // Находим список
            ListView appsList = findViewById(R.id.lvMain);
            // Режим выбора пунктов списка (последний нажатый пункт)
            appsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Создание адаптера
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names_apps);
            //Присваивание адаптера списку
            appsList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            //TextView debugText = findViewById(R.id.textView6);
            //debugText.setText(e.toString());
        }
    }

    public void measurePerfomanse (View view) {
        Intent intent = new Intent(this, PerformanceActivity.class);
        startActivity(intent);
    }

    public void showMenu (View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
