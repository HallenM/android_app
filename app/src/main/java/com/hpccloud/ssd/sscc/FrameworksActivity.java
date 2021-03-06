package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FrameworksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frameworks);

        String str[] = new String[1]; str[0] = "coming soon";
        // Находим список
        ListView frameworksList = findViewById(R.id.lvMain);
        // Режим выбора пунктов списка (последний нажатый пункт)
        frameworksList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Создание адаптера
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str);
        //Присваивание адаптера списку
        frameworksList.setAdapter(adapter);
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
