package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ClustersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clusters);
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
