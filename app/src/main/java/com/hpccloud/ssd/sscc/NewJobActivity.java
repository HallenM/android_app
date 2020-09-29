package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class NewJobActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_NEW_JOB";

    private String token = null;
    private String[] name_apps, name_clusters, id_clusters;
    int id_app, id_clu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        token = localStorage.getString("token", "unknown").trim();

        getAppsName();
        //getClustersName();

        Set<String> local;
        local = localStorage.getStringSet("names_apps", new LinkedHashSet<String>());
        name_apps = local.toArray(new String[local.size()]);

        /*local.clear();
        local = localStorage.getStringSet("names_clusters", new LinkedHashSet<String>());
        name_clusters = local.toArray(new String[local.size()]);
        local.clear();
        local = localStorage.getStringSet("id_clusters", new LinkedHashSet<String>());
        id_clusters = local.toArray(new String[local.size()]);
        SharedPreferences.Editor editor = localStorage.edit();
        editor.remove("names_apps");
        editor.remove("names_clusters");
        editor.remove("id_clusters");
        editor.apply();*/

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, name_apps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Applications");
        // выделяем элемент
        spinner.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_app = position; }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {  }
        });

        /*adapter.clear();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, name_clusters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerClu = findViewById(R.id.spinner2);
        spinnerClu.setAdapter(adapter);
        // заголовок
        spinnerClu.setPrompt("Clusters");
        // выделяем элемент
        spinnerClu.setSelection(0);
        // устанавливаем обработчик нажатия
        spinnerClu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_clu = position; }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {  }
        });*/

    }

    public void getAppsName(){
        String url = "http://192.168.0.108:4000/api/1.0/projects?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/projects?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForApplications = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Set <String> names_apps = new LinkedHashSet<>();
                try {
                    JSONObject resultResp = new JSONObject(response);
                    JSONArray responseArr = resultResp.getJSONArray("projects");
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject jsonValue = responseArr.getJSONObject(i);
                        if(jsonValue.getString("type").equals("app")) {
                            names_apps.add(jsonValue.getString("name"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = localStorage.edit();
                editor.putStringSet("names_apps", names_apps);
                editor.apply();

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

    public void getClustersName() {
        String url = "http://192.168.0.108:4000/api/1.0/clusters/profiles?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/clusters/profiles?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForApplications = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Set <String> names_clu = new LinkedHashSet<>();
                Set <String> id_clu = new LinkedHashSet<>();
                try {
                    JSONObject resultResp = new JSONObject(response);
                    JSONArray responseArr = resultResp.getJSONArray("clusters_profiles");
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject jsonValue = responseArr.getJSONObject(i);
                        names_clu.add(jsonValue.getString("name"));
                        Integer id = jsonValue.getInt("id");
                        id_clu.add(id.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("!11111111", e.getMessage());
                }

                SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = localStorage.edit();
                editor.putStringSet("names_clusters", names_clu);
                editor.putStringSet("id_clusters", id_clu);
                editor.apply();
                Log.d(TAG, "Response was sent successful. Name and id clu was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR!!!!!!", error.toString());
            }
        });

        // Tag the request
        String TAG = "Tag_for_count_of_clusters_request";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        //queue.start();
    }

    public JSONObject setParams() throws JSONException {
        EditText data = findViewById(R.id.editTextNodes);
        String nodes = data.getText().toString();
        data = findViewById(R.id.editTextPPN);
        String ppn = data.getText().toString();
        data = findViewById(R.id.editTextName);
        String nameJob = data.getText().toString();

        JSONObject params = new JSONObject();
        params.put("name", nameJob);
        params.put("type", "OpenPBS");
        params.put("cluster_profile_id", 3);//parseInt(id_clusters[id_clu]));
        params.put("state", "run");

        JSONArray chunk = new JSONArray();
        final JSONArray res = new JSONArray();

        JSONObject resValue = new JSONObject();
        resValue.put("name","ncpus");
        resValue.put("value",nodes);
        res.put(resValue);

        resValue = new JSONObject();
        resValue.put("name","mpiprocs");
        resValue.put("value",ppn);
        res.put(resValue);

        resValue = new JSONObject();
        resValue.put("count",1);
        resValue.put("resources",res);
        chunk.put(resValue);

        params.put("chunks",chunk);

        params.put("walltime", "00:10:00");
        params.put("executable_path", "apps/" + name_apps[id_app] + "/builds/127.0.0.1/headnode/release");
        //params.put("executable_path", "apps/" + name_apps[id_app] + "/builds/hpccloud.ssd.sscc.ru/headnode/release");
        params.put("args", "");
        params.put("env_vars", "");
        params.put("queue_name", "");

        return params;
    }

    public void sendRequest(String url) throws JSONException {

        JSONObject parameters = setParams();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest requestForNewJob = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int id = 0;
                try {
                    id = response.getInt("job_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Response was sent successful. Data was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "ERR!!! " + error.toString());
            }
        });

        // Позволяет избежать Volley отправки данных дважды
        requestForNewJob.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Tag the request
        String TAG = "Tag_for_new_job_request";
        // Set the tag on the request.
        requestForNewJob.setTag(TAG);
        queue.add(requestForNewJob);
        queue.start();
    }

    public void startJob (View view) throws JSONException {
        String url = "http://192.168.0.108:4000/api/1.0/jobs?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/jobs?access_token=" + token;
        sendRequest(url);

        EditText data = findViewById(R.id.editTextName);
        String nameJob = data.getText().toString();

        Toast.makeText(this, "The job " + nameJob + " is running", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, JobsActivity.class);
        startActivity(intent);
    }
}
