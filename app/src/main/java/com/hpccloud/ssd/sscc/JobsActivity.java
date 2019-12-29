package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JobsActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    private static final String TAG = "JSONSTR_JOBS";

    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        updateListOfJobs();

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // меняем изображение на кнопке
                if (flag) {
                    imageView.setImageResource(R.drawable.update_page_onclick);
                }
                else {
                    // возвращаем первую картинку
                    imageView.setImageResource(R.drawable.update_page);
                }
                updateListOfJobs();
                flag = !flag;
            }
        });
    }

    public void updateListOfJobs() {
        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final String token = localStorage.getString("token", "unknown").trim();

        String url = "http://192.168.0.108:4000/api/1.0/jobs?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/jobs?access_token=" + token;

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
        String TAG = "Tag_for_count_of_jobs_request";
        // Set the tag on the request.
        requestForApplications.setTag(TAG);
        queue.add(requestForApplications);
        queue.start();
    }

    public void fillList(String JSONstr) {
        final String names_jobs[];
        String states_jobs[];
        String time_jobs[];
        final int id_jobs[];

        ArrayList<JobsModelForList> jobsModelArrayList = new ArrayList<>();
        try {
            JSONObject resultResp = new JSONObject(JSONstr);
            JSONArray responseArr = resultResp.getJSONArray("jobs");
            names_jobs = new String[responseArr.length()];
            states_jobs = new String[responseArr.length()];
            time_jobs = new String[responseArr.length()];
            id_jobs = new int[responseArr.length()];
            for (int i = 0; i < responseArr.length(); i++) {
                JSONObject jsonValue = responseArr.getJSONObject(i);
                names_jobs[i] = jsonValue.getString("name");
                states_jobs[i] = jsonValue.getString("state");
                time_jobs[i] = jsonValue.getString("last_modify_time");
                id_jobs[i] = jsonValue.getInt("id");

                JobsModelForList jobModel = new JobsModelForList();
                jobModel.setNameJob(names_jobs[i]);
                jobModel.setStateJob(states_jobs[i]);
                jobsModelArrayList.add(jobModel);
            }

            /*Collections.sort(jobsModelArrayList, new Comparator<JobsModelForList>() {
                @Override
                public int compare(JobsModelForList left, JobsModelForList right) {
                    return left.getNameJob().compareTo(right.getNameJob());
                }
            });*/

            // Находим список
            ListView jobList = null;
            jobList = findViewById(R.id.lvMain);
            jobList.setAdapter(null);

            if(responseArr.length()!= 0) {
                // Режим выбора пунктов списка (последний нажатый пункт)
                jobList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                // Создание адаптера
                JobsAdapter adapter = new JobsAdapter(this, jobsModelArrayList);

                //Присваивание адаптера списку
                jobList.setAdapter(adapter);
            }

            // добвляем для списка слушатель
            jobList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {
                    SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = localStorage.edit();
                    editor.putInt("job_id", id_jobs[position]);
                    editor.putString("job_name", names_jobs[position]);
                    editor.apply();

                    Intent intent = new Intent(getBaseContext(), JobViewActivity.class);
                    startActivity(intent);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONERROR!!!", e.getMessage());
        }
    }

    public class JobsAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<JobsModelForList> jobsModelArrayList;

        public JobsAdapter(Context context, ArrayList<JobsModelForList> jobsModelArrayList) {

            this.context = context;
            this.jobsModelArrayList = jobsModelArrayList;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return jobsModelArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return jobsModelArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                // Раздуваем макет от родителя,(parent) хотим получить его контекст
                // Вне этого будем печатать(inflate) и раздувать макет элемента, который мы создали (R.layout.jobs_list_item)
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.jobs_list_item, null, true);

                // Передача родительского эл-та, чтобы завершить передачу параметров(установка информации к новому ViewHolder, который мы недавно создали)
                holder.jobName = (TextView) convertView.findViewById(R.id.textViewName);
                holder.jobState = (TextView) convertView.findViewById(R.id.textViewState);
                convertView.setTag(holder);
            }else {
                // the getTag returns the viewHolder object set as a tag to the view
                holder = (ViewHolder)convertView.getTag();
            }

            holder.jobName.setText(jobsModelArrayList.get(position).getNameJob());
            holder.jobState.setText(jobsModelArrayList.get(position).getStateJob());

            return convertView;
        }

        public void clearData() {
            // clear the data
            jobsModelArrayList.clear();
        }

        class ViewHolder {

            protected TextView jobName, jobState;

        }
    }

    class JobsModelForList {
        private String name;
        private String state;

        public String getNameJob() {
            return name;
        }

        public void setNameJob(String name) {
            this.name = name;
        }

        public String getStateJob() {
            return state;
        }

        public void setStateJob(String state) {
            this.state = state;
        }
    }

    public void newJob (View view) {
        Intent intent = new Intent(this, NewJobActivity.class);
        startActivity(intent);
    }

    public void showMenu (View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void updateData (View view) {
        updateListOfJobs();
    }
}
