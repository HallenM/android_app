package com.hpccloud.ssd.sscc;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.sort;

public class FileSystemActivity extends ListActivity {//AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_system);


        /*final TextAdapter listAdapter = new TextAdapter();
        listView.setAdapter(listAdapter);

        List<String> example = new ArrayList<>();
        for(int i = 0; i < 30; i++) {
            example.add("   " + String.valueOf(i));
        }
        listAdapter.setData(example);*/


    }

    /*class TextAdapter extends BaseAdapter {

        private List<String> data = new ArrayList<>();

        public void setData(List<String> data) {
            if(data != null) {
                this.data.clear();
                if(data.size() > 0){
                    this.data.addAll(data);
                }
                // Сообщение адаптеру, что данные обновились
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                // Раздуваем макет от родителя,(parent) хотим получить его контекст
                // Вне этого будем печатать(inflate) и раздувать макет элемента, который мы создали (R.layout.file_system_list_item)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_system_list_item, parent, false);
                // Передача родительского эл-та, чтобы завершить передачу параметров(установка информации к новому ViewHolder, который мы недавно создали)
                convertView.setTag(new ViewHolder((TextView) convertView.findViewById(R.id.textItemFS)));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            final String item = getItem(position);
            holder.info.setText(item);

            return convertView;
        }

        class ViewHolder {
            TextView info;

            ViewHolder(TextView info) {
                this.info = info;
            }
        }

    }

    // Кодовый идентификатор для запроса на разрешение
    private static final int REQUEST_PERMISSIONS = 1234;

    // Строковый массив с фактическими разрешениями, которые мы хотим запросить
    // Позволяют читать из внешего хранилища и писать в него
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Количество разрешений
    private static final int PERMISSIONS_COUNT = 2;

    // Прелоставлены ли разрешения READ_EXTERNAL_STORAGE и WRITE_EXTERNAL_STORAGE
    private boolean arePermissionsGranted() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int p = 0; p < PERMISSIONS_COUNT; p++) {
                // PERMISSION_GRANTED - разрешение получено; PERMISSION_DENIED - разрешения нет
                if(checkSelfPermission(PERMISSIONS[p]) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }*/

    public void showMenu (View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
