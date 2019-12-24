package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Подключения для Volley
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final String TAG = "JSONSTR";
    public static final String APP_PREFERENCES = "Settings for local storage";
    private static String name = "";

    public void isTokenCorrect (String token) {
        TextView textView = findViewById(R.id.textView2);
        textView.setBackgroundResource(R.color.inputWhite);
        textView.setText("");

        if (token.equals("unknown")) {
            textView.setBackgroundResource(R.drawable.error_field);
            textView.setText("wrong login or password");

            // Таймер обратного отсчёта на 3с с шагом в 1с (значения в мс):
            new CountDownTimer(3000, 1000) {
                // Обновление счётчика обратного отсчета с каждой секундой
                public void onTick(long millisUntilFinished) {
                    TextView textViewOld = findViewById(R.id.textView2);
                    textViewOld.setText("wrong login or password ");
                }

                // Действия после завершения отсчёта
                public void onFinish() {
                    TextView textViewOld = findViewById(R.id.textView2);
                    textViewOld.setBackgroundResource(R.color.inputWhite);
                    textViewOld.setText("");
                }
            }.start();
        }
        else {
            Toast.makeText(this, "Hello, " + name, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForAuthorization = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String token = null;
                int id = 0;
                try {
                    JSONObject resultResp = new JSONObject(response);
                    JSONArray responseArr = resultResp.getJSONArray("tokens");
                    JSONObject jsonValue = responseArr.getJSONObject(0);
                    token = jsonValue.getString("token");
                    id = jsonValue.getInt("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = localStorage.edit();
                editor.putInt("user_id", id);
                editor.putString("token", token);
                editor.apply();

                isTokenCorrect(token);

                Log.d(TAG, "Response was sent successful. Data was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = localStorage.edit();
                editor.putInt("user_id", 0);
                editor.putString("token", "unknown");
                editor.apply();

                isTokenCorrect("unknown");

                Log.d(TAG, "!!!!!!!!!!!!!!" + error.toString());
            }
        }) {
            public Map<String, String> getHeaders() {
                EditText data = findViewById(R.id.editText);
                String login = data.getText().toString();
                name = login;
                data = findViewById(R.id.editText2);
                String password = data.getText().toString();

                String credentials = login + ":" + password;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };

        // Tag the request
        String TAG = "Tag_for_authorization_request";
        // Set the tag on the request.
        requestForAuthorization.setTag(TAG);
        queue.add(requestForAuthorization);
        queue.start();
    }

    public void authorizedUser (View view) {
        // Очистка значений
        TextView textView = findViewById(R.id.textView2);
        textView.setBackgroundResource(R.color.inputWhite);
        textView.setText("");

        String url = /*192.168.0.101*/"192.168.12.139:4000/api/1.0/tokens";
        sendRequest(url);
    }
}
