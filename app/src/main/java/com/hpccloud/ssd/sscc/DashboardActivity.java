package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";

    String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Integer id = localStorage.getInt("user_id", 0);
        String token = localStorage.getString("token", "unknown");
        String result = "token: " + token + " \n" + "id: " + id.toString();

        // Находим список
        ListView menuList = findViewById(R.id.lvMain);
        // Режим выбора пунктов списка (последний нажатый пункт)
        menuList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Получение массива из файла ресурсов
        names = getResources().getStringArray(R.array.names);
        // Создание адаптера
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        //Присваивание адаптера списку
        menuList.setAdapter(adapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = nextPage(position);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),
                 //       "Вы выбрали " + names[position], Toast.LENGTH_SHORT).show();
            }
        });

    }

    public Intent nextPage (int position) {

        Intent intent = null;
        switch(position){
            case 0: intent = new Intent(this, ApplicationsActivity.class); break;
            case 1: intent = new Intent(this, FrameworksActivity.class);break;
            case 2: intent = new Intent(this, ModelsActivity.class);break;
            case 3: intent = new Intent(this, JobsActivity.class);break;
            case 4: intent = new Intent(this, ClustersActivity.class);break;
            case 5: intent = new Intent(this, FileSystemActivity.class);break;
        }
        return intent;
    }

    public void logOut (View view) {
        // Очистка данных
        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        localStorage.edit().remove("user_id").clear().apply();
        localStorage.edit().remove("token").clear().apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
