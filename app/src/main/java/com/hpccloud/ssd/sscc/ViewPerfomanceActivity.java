package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ViewPerfomanceActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_perfomance);

        TextView info = findViewById(R.id.textViewInfoView);
        info.setMovementMethod(new ScrollingMovementMethod());
        info.setText("Explanation of names columns\n\n" +
                "nCor - count of core for running jobs;\n" +
                "t - time of executing the job on cores;\n" +
                "acc - acceleration relative to executing on 1 core;\n" +
                "eff - efficiency of executing the application;");

        String[] headers = {"nCor", "t, sec", "acc", "eff"};
        String[][] dataForTable = null;

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        JSONArray resArr = null;
        Integer[] nCore, time;
        Double[] acceler, effic;
        try {
            resArr = new JSONArray(localStorage.getString("res_time", ""));
            nCore = new Integer[resArr.length()];
            time = new Integer[resArr.length()];
            acceler = new Double[resArr.length()];
            effic = new Double[resArr.length()];
            for(int i = 0; i < resArr.length(); i++) {
                JSONObject obj = resArr.getJSONObject(i);
                nCore[i] = obj.getInt("n_core");
                time[i] = obj.getInt("time");
                if(i!= 0) {
                    acceler[i] = Double.valueOf(time[0] / time[i]);
                    effic[i] = (acceler[i] / nCore[i]) * 100;
                } else {
                    acceler[0] = Double.valueOf(time[0] / time[0]);
                    effic[0] = (acceler[0] / nCore[0]) * 100;
                }
            }
            dataForTable = new String[resArr.length()][4];
            for(int i = 0; i < resArr.length(); i++) {
                dataForTable[i][0] = nCore[i].toString();
                dataForTable[i][1] = time[i].toString();
                dataForTable[i][2] = acceler[i].toString();
                dataForTable[i][3] = effic[i].toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        tableView.setColumnCount(4);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, headers));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, dataForTable));

    }

    public void showMenu (View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
