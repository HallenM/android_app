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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class PerfomanceActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_PERFOMANSE";

    private String token = null;
    private String[] name_apps, name_clusters, id_clusters;
    int id_app, id_clu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfomance);

        Button b = findViewById(R.id.button6);
        b.setClickable(false);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        token = localStorage.getString("token", "unknown").trim();

        getAppsName();

        Set<String> local;
        local = localStorage.getStringSet("names_apps", new LinkedHashSet<String>());
        name_apps = local.toArray(new String[local.size()]);

        SharedPreferences.Editor editor = localStorage.edit();
        editor.remove("id_job");
        editor.remove("names_apps");
        editor.apply();

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

                Log.d(TAG, "Response was sent successful. AppNames was obtained.");
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

    public JSONObject setParams(int nodes, String name) throws JSONException {

        JSONObject params = new JSONObject();
        params.put("name", name);
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
        resValue.put("value",1);
        res.put(resValue);

        resValue = new JSONObject();
        resValue.put("count",1);
        resValue.put("resources",res);
        chunk.put(resValue);

        params.put("chunks",chunk);

        params.put("walltime", "00:10:00");
        params.put("executable_path", "apps/" + name_apps[id_app] + "/builds/127.0.0.1/headnode/release");//"/builds/hpccloud.ssd.sscc.ru/headnode/release");
        params.put("args", "");
        params.put("env_vars", "");
        params.put("queue_name", "");

        return params;
    }

    public void runJob(int numbNode, String name) throws JSONException {
        // Firstly, parameters should be set
        JSONObject parameters = setParams(numbNode, name);

        String url = "http://192.168.0.108:4000/api/1.0/jobs?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/jobs?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest requestForNewJob = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id = response.getInt("job_id");

                    SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = localStorage.edit();
                    editor.putInt("id_job", id);
                    editor.apply();

                    checkState(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("RUNJOB_JSONERR!!!", e.getMessage());
                }

                Log.d("RUNJOB!!!", "ID was received.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RUNJOB_ERR!!!", error.getMessage());
            }
        });

        // Позволяет избежать Volley отправки данных дважды
        requestForNewJob.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Tag the request
        String TAG = "Tag_for_new_job" + numbNode + "_request";
        // Set the tag on the request.
        requestForNewJob.setTag(TAG);
        queue.add(requestForNewJob);
        //queue.start();
    }

    public void checkState(int id_job) {
        String url = "http://192.168.0.108:4000/api/1.0/jobs/" + id_job + "?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/jobs/" + id_job + ?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForApplications = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resultResp = new JSONObject(response);
                    String state = resultResp.getString("state");

                    SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    int id_job = localStorage.getInt("id_job", 0);
                    if (!state.equals("finished")) {
                        checkState(id_job);
                    }
                    else {
                        EditText data = findViewById(R.id.editTextName);
                        String nameJobs = data.getText().toString();
                        int node_i = localStorage.getInt("node_numb", 1);

                        findTime(nameJobs + (node_i - 1), id_job);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("CHECKSTATE_JSONERR!!!", e.getMessage());
                }

                Log.d("CHECKSTATE!!!", "State of job was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CHECKSTATE_ERR!!!", error.getMessage());
            }
        });

        // Tag the request
        String TAG = "Tag_for_state_of_jobs_request";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        //queue.start();
    }

    public void findTime(final String name_jobs, int id_job){
        String url = "http://192.168.0.108:4000/api/1.0/fs/jobs/" + name_jobs + "/" + id_job + ".out?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/fs/jobs/" + name_jobs + "/" + id_job + ".out?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForApplications = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("FINDTIME!!!", "Real time was received.");
                    JSONObject resultResp = new JSONObject(response);
                    JSONObject subObj = new JSONObject(resultResp.getString("file"));
                    String result = subObj.getString("body");
                    String[] words = result.split("\\n");

                    int idStart = 0, idStop = 0;
                    String start = "hpccloud.ssd.sscc.ru-profiling-start";
                    String stop = "hpccloud.ssd.sscc.ru-profiling-stop";

                    for(int i = 0; i < words.length; i++) {
                        if(words[i].equals(start)) {
                            idStart = i + 1;
                        }
                        if(words[i].equals(stop)) {
                            idStop = i + 1;
                        }
                    }

                    //String dateFormat = "EEE MMM dd HH:mm:ss yyyy z"; // Этот формат для времени выдаваемого сервером hpccloud.ssd.sscc.ru
                    String dateFormat = "HH:mm:ss";
                    SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());

                    String[] date = words[idStart].split("\\s");
                    Date dateStart = format.parse(date[3]);

                    date = null;
                    date = words[idStop].split("\\s");
                    Date dateStop = format.parse(date[3]);

                    Integer realTime = dateStop.getSeconds() - dateStart.getSeconds();

                    JSONObject obj = new JSONObject();
                    obj.put("name", name_jobs);
                    obj.put("n_core", name_jobs.substring(name_jobs.length() - 1));
                    obj.put("time", realTime);

                    SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    JSONArray resArr = new JSONArray(localStorage.getString("res_time", ""));
                    resArr.put(obj);
                    SharedPreferences.Editor editor = localStorage.edit();
                    editor.remove("res_time");
                    editor.remove("id_job");
                    editor.putString("res_time", resArr.toString());
                    editor.apply();

                    int node_i = localStorage.getInt("node_numb", 1);
                    EditText data = findViewById(R.id.editTextNodes2);
                    Integer nodes2 = parseInt(data.getText().toString());
                    data = findViewById(R.id.editTextName);
                    String nameJobs = data.getText().toString();
                    if(node_i <= nodes2) {
                        editor.remove("node_numb");
                        editor.putInt("node_numb", node_i + 1);
                        editor.apply();
                        runJob(node_i, nameJobs + node_i);
                    } else {
                        editor.remove("node_numb");
                        editor.apply();
                        Button b = findViewById(R.id.button6);
                        b.setClickable(true);
                        Toast.makeText(getBaseContext(), "Success! Now You can see the result", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("FINDTIME_JSONERR!!!", e.getMessage());
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("FINDTIME_PARSEERR!!!", e.getMessage());

                    TextView debugText = findViewById(R.id.textViewInfoView);
                    debugText.setText(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FINDTIME_ERR!!!", error.toString() + "        " + error.getMessage());
            }
        });

        // Tag the request
        String TAG = "Tag_for_parse_time";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        //queue.start();
    }

    public void startMeasurePerfomanse (View view) throws JSONException {

        EditText data = findViewById(R.id.editTextNodes1);
        Integer nodes1 = parseInt(data.getText().toString());
        data = findViewById(R.id.editTextName);
        String nameJobs = data.getText().toString();

        Toast.makeText(this, "Please, wait a few time", Toast.LENGTH_SHORT).show();

        JSONArray timeArr = new JSONArray();
        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = localStorage.edit();
        editor.putString("res_time", timeArr.toString());
        editor.apply();

        runJob(nodes1, nameJobs + nodes1);

        editor.putInt("node_numb", nodes1 + 1);
        editor.apply();
    }

    public void seeResult (View view) {
        Intent intent = new Intent(this, ViewPerfomanceActivity.class);
        startActivity(intent);
    }
}
