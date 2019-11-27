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

import java.util.ArrayList;

public class JobsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_JOBS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String token = localStorage.getString("token", "unknown").trim();
        String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/jobs?access_token=" + token;

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
        String TAG = "Tag_for_count_of_jobs_request";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        queue.start();
    }

    public void fillList(String JSONstr) {
        String names_jobs[];
        String states_jobs[];
        String time_jobs[];
        int id_jobs[];

        ArrayList<JobsModelForList> jobsModelArrayList = new ArrayList<>();
        try {
            JSONObject resultResp = new JSONObject(JSONstr);
            JSONArray responseArr = resultResp.getJSONArray("jobs");
            names_jobs = new String[responseArr.length()];
            states_jobs = new String[responseArr.length()];
            time_jobs = new String[responseArr.length()];
            id_jobs = new int[responseArr.length()];
            for (int i = 0; i < responseArr.length(); i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                names_jobs[i] = jsonValue.getString("name");
                states_jobs[i] = jsonValue.getString("state");
                time_jobs[i] = jsonValue.getString("last_modify_time");
                id_jobs[i] = jsonValue.getInt("id");

                JobsModelForList jobModel = new JobsModelForList();
                jobModel.setNameJob("   " + names_jobs[i]);
                jobModel.setStateJob(states_jobs[i]);
                jobsModelArrayList.add(jobModel);
            }
            /*String str = "";
            for(int i = 0; i < names_jobs.length; i++)
            {
                str += "name: " + names_jobs[i] + ", state: " + states_jobs[i] + ", time: " + time_jobs[i] + ", id: " + id_jobs[i] + "; ";
            }*/

            // Находим список
            ListView jobList = findViewById(R.id.lvMain);
            // Режим выбора пунктов списка (последний нажатый пункт)
            jobList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Создание адаптера
            JobsAdapter adapter = new JobsAdapter(this,jobsModelArrayList);
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info_jobs);
            //Присваивание адаптера списку
            jobList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            //TextView debugText = findViewById(R.id.textView6);
            //debugText.setText(e.toString());
        }
    }

    public void logOut (View view) {
        // Очистка данных
        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        localStorage.edit().remove("user_id").clear().apply();
        localStorage.edit().remove("token").clear().apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showMenu (View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
