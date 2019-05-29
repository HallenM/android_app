package com.hpccloud.ssd.sscc;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

// Подключения для Volley
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    public void authorizedUser (View view) {
        EditText data = (EditText) findViewById(R.id.editText);
        final String login = data.getText().toString();
        data = (EditText) findViewById(R.id.editText2);
        final String password = data.getText().toString();

        String url = "http://hpccloud.ssd.sscc.ru/api/1.0/tokens";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForAuthorization = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                TextView textView = findViewById(R.id.textView2);

                //JSONObject resultResp = new JSONObject(response.);
                //JSONObject tokenObj = resultResp.getJSONArray("tokens");

                textView.setText("Response done! " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView textView = findViewById(R.id.textView2);
                textView.setText("ERRROR!! " + error.toString());
                Log.d(TAG, error.toString());
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = login + ":" + password;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };

        // Tag the request
        String TAG = "TagForAuthorizationRequest";
        // Set the tag on the request.
        requestForAuthorization.setTag(TAG);

        queue.add(requestForAuthorization);
    }


}
