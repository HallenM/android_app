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

public class ModelsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_MODELS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String token = localStorage.getString("token", "unknown").trim();
        String url = /*192.168.0.101*/"http://hpccloud.ssd.sscc.ru:4000/api/1.0/projects?access_token=" + token;

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
        String TAG = "Tag_for_count_of_models_request";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        queue.start();
    }

    public void fillList(String JSONstr) {
        String names_models[];
        int id_models[];
        try {
            JSONObject resultResp = new JSONObject(JSONstr);
            JSONArray responseArr = resultResp.getJSONArray("projects");
            int count = 0;
            for (int i = 0; i < responseArr.length(); i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                if(jsonValue.getString("type").equals("model"))
                {
                    count++;
                }
            }
            names_models = new String[count];
            id_models = new int[count];
            for (int i = 0, j = 0; i < responseArr.length() & j < count; i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                if(jsonValue.getString("type").equals("model"))
                {
                    names_models[j] = jsonValue.getString("name");
                    id_models[j] = jsonValue.getInt("id");
                    j++;
                }
            }

            // Находим список
            ListView modelsList = findViewById(R.id.lvMain);
            // Режим выбора пунктов списка (последний нажатый пункт)
            modelsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Создание адаптера
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names_models);
            //Присваивание адаптера списку
            modelsList.setAdapter(adapter);
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
