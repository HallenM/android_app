package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApplicationsActivity extends AppCompatActivity {

    private static final String TAG = "JSONSTR";
    private static String JSONstr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String token = localStorage.getString("token", "unknown").trim();
        String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/projects?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForApplications = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONstr = response;
                /*ArrayList<String> names = new ArrayList<String>();
                ArrayList<String> types = new ArrayList<String>();
                ArrayList<Integer> id = new ArrayList<Integer>();
                try {
                    JSONObject resultResp = new JSONObject(JSONstr);
                    JSONArray responseArr = resultResp.getJSONArray("projects");
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject jsonValue = responseArr.getJSONObject(i);
                        String str = jsonValue.getString("type");
                        if(str.equals("app"))
                        {
                            names.set(i, jsonValue.getString("name"));
                            types.set(i, str);
                            id.set(i, jsonValue.getInt("id"));
                        }
                    }
                    String str = "";
                    for(int i = 0; i < names.size(); i++)
                    {
                        str += "name: " + names.get(i) + ", type: " + types.get(i) + ", id: " + id.get(i) + "; ";
                    }
                    TextView debugText = findViewById(R.id.textView6);
                    debugText.setText("");
                    debugText.setText("111: " + str);
                } catch (JSONException e) {
                    e.printStackTrace();
                    TextView debugText = findViewById(R.id.textView6);
                    debugText.setText("!!!" + e.toString());
                }*/

                /*String names[];
                String types[];
                int id[];
                try {
                    JSONObject resultResp = new JSONObject(response);
                    JSONArray responseArr = resultResp.getJSONArray("projects");
                    names = new String[responseArr.length()];
                    types = new String[responseArr.length()];
                    id = new int[responseArr.length()];
                    for (int i = 0; i < responseArr.length(); i++) {
                        JSONObject jsonValue = responseArr.getJSONObject(i);
                        names[i] = jsonValue.getString("name");
                        types[i] = jsonValue.getString("type");
                        id[i] = jsonValue.getInt("id");
                    }
                    String str = "";
                    for(int i = 0; i < names.length; i++)
                    {
                        str += "name: " + names[i] + ", type: " + types[i] + ", id: " + id[i] + "; ";
                    }
                    TextView debugText = findViewById(R.id.textView6);
                    debugText.setText("1: " + str);
                } catch (JSONException e) {
                    e.printStackTrace();
                    TextView debugText = findViewById(R.id.textView6);
<<<<<<< HEAD
                    debugText.setText(e.toString());
                }*/
=======
                    debugText.setText(e.getMessage());
                    Log.e("ERROR VOLLEY", e.getMessage());
                }
>>>>>>> d6d55e0bab11b03e0b4224b3f2526e3a86b0fa48
                Log.d(TAG, "Response was sent successful. Data was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
<<<<<<< HEAD
                Log.d(TAG, error.toString());
            }
        });
=======
                Log.d(TAG, "!!!!! " + error.toString());
                TextView debugText = findViewById(R.id.textView6);
                debugText.setText(error.toString());

            }
        })/*{
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };*/
>>>>>>> d6d55e0bab11b03e0b4224b3f2526e3a86b0fa48

//        {
//            public Map<String, String> getParams() {
//                HashMap<String, String> params = new HashMap<>();
//                //headers.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("access_token", token);
//                return params;
//            }
//        }
        ;

        // Tag the request
        String TAG = "TagForCountProjectsRequest";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        queue.start();

        String names[], allNames[];
        String types[];
        int id[], allId[];
        try {
            JSONObject resultResp = new JSONObject(JSONstr);
            JSONArray responseArr = resultResp.getJSONArray("projects");
            int count = 0;
            types = new String[responseArr.length()];
            for (int i = 0; i < responseArr.length(); i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                types[i]= jsonValue.getString("type");
                if(types[i].equals("app"))
                {
                    count++;
                }
            }
            names = new String[count];
            id = new int[count];
            for (int i = 0, j = 0; i < responseArr.length() & j < count; i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                if(types[i].equals("app"))
                {
                    names[j] = jsonValue.getString("name");
                    id[j] = jsonValue.getInt("id");
                    j++;
                }
            }
            String str = "";
            for(int i = 0; i < names.length; i++)
            {
                str += "name: " + names[i] + ", type: " + types[i] + ", id: " + id[i] + "; ";
            }
            //TextView debugText = findViewById(R.id.textView6);
            //debugText.setText("111111: " + str);

            // Находим список
            ListView menuList = findViewById(R.id.lvMain);
            // Режим выбора пунктов списка (последний нажатый пункт)
            menuList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Получение массива из файла ресурсов
            //names = getResources().getStringArray(R.array.names);
            // Создание адаптера
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            //Присваивание адаптера списку
            menuList.setAdapter(adapter);

            /*menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    //Intent intent = nextPage(position);
                    //startActivity(intent);
                }
            });*/
        } catch (JSONException e) {
            e.printStackTrace();
            //TextView debugText = findViewById(R.id.textView6);
            //debugText.setText(e.toString());
        }
    }

    public static final String APP_PREFERENCES = "Settings for local storage";

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
