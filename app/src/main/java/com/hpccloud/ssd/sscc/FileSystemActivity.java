package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FileSystemActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "Settings for local storage";
    public File treeFiles = new File("/");
    public ArrayList<String> path = new ArrayList<String>();
    public String currDir = "/";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_system);

        SharedPreferences localStorage = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        token = localStorage.getString("token", "unknown").trim();

        String url = "http://192.168.0.108:4000/api/1.0/fs/?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/fs/?access_token=" + token;
        path.add("/");

        getFileSystemRoot(url);
    }

    public void getFileSystemRoot(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForFiles = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fillListRoot(response);
                Log.d("FILESYSTEM", "Dirs and files was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FILESYSTEM_ERR", error.toString());
            }
        });

        // Tag the request
        String TAG = "Tag_for_list_of_files_and_dirs_request";
        // Set the tag on the request.
        requestForFiles.setTag(TAG);
        queue.add(requestForFiles);
        //queue.start();
    }

    public void fillListRoot(String JSONstr) {
        final String names_dirs[];
        String names_files[];

        ArrayList<FileModel> fileModelArrayList = new ArrayList<>();
        try {
            JSONObject resultResp = new JSONObject(JSONstr);
            JSONObject innerObj = resultResp.getJSONObject("directory");
            JSONArray dirs = innerObj.getJSONArray("dirs");
            JSONArray files = innerObj.getJSONArray("files");
            names_dirs = new String[dirs.length()];
            names_files = new String[files.length()];

            for (int i = 0; i < dirs.length(); i++) {
                names_dirs[i] = dirs.getString(i);

                /*File f = new File("/", names_dirs[i]);
                treeFiles = f;*/

                FileModel fileModel = new FileModel();
                fileModel.setName(names_dirs[i]);
                fileModel.setView(R.drawable.close_folder);
                fileModelArrayList.add(fileModel);
            }

            for (int i = 0; i < files.length(); i++) {
                names_files[i] = files.getString(i);

                FileModel fileModel = new FileModel();
                fileModel.setName(names_files[i]);
                fileModel.setView(R.drawable.file_in_folder);
                fileModelArrayList.add(fileModel);
            }

            // Находим список
            ListView fileSystList = null;
            fileSystList = findViewById(R.id.lvMain);
            fileSystList.setAdapter(null);

            // Режим выбора пунктов списка (последний нажатый пункт)
            fileSystList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Создание адаптера
            FileSystemAdapter adapter = new FileSystemAdapter(this, fileModelArrayList);

            //Присваивание адаптера списку
            fileSystList.setAdapter(adapter);

            // добвляем для списка слушатель
            fileSystList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {
                    File f = new File("/", names_dirs[position]);
                    currDir = names_dirs[position];
                    treeFiles = f;
                    path.add(names_dirs[position]);
                    getInnerFile(f.getAbsolutePath());
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FILESYSTEM_JSONERR!!!", e.getMessage());
        }
    }

    public void getInnerFile(String dir) {
        String url = "http://192.168.0.108:4000/api/1.0/fs/" + dir + "?access_token=" + token;
        //String url = "http://hpccloud.ssd.sscc.ru:4000/api/1.0/fs/" + dir + "?access_token=" + token;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestForFiles = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fillList(response);
                Log.d("FILESYSTEM", "Dirs and files was obtained.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FILESYSTEM_ERR", error.toString());
            }
        });

        // Tag the request
        String TAG = "Tag_for_list_of_files_and_dirs_request";
        // Set the tag on the request.
        requestForFiles.setTag(TAG);
        queue.add(requestForFiles);
    }

    public void fillList(String JSONstr) {
        final String names_dirs[];
        String names_files[];

        ArrayList<FileModel> fileModelArrayList = new ArrayList<>();
        try {
            JSONObject resultResp = new JSONObject(JSONstr);
            JSONObject innerObj = resultResp.getJSONObject("directory");
            JSONArray dirs = innerObj.getJSONArray("dirs");
            JSONArray files = innerObj.getJSONArray("files");
            names_dirs = new String[dirs.length()];
            names_files = new String[files.length()];

            for (int i = 0; i < dirs.length(); i++) {
                names_dirs[i] = dirs.getString(i);

                FileModel fileModel = new FileModel();
                fileModel.setName(names_dirs[i]);
                fileModel.setView(R.drawable.close_folder);
                fileModelArrayList.add(fileModel);
            }

            for (int i = 0; i < files.length(); i++) {
                names_files[i] = files.getString(i);

                /*File ff = new File("/", names_files[i]);
                treeFiles = ff;*/

                FileModel fileModel = new FileModel();
                fileModel.setName(names_files[i]);
                fileModel.setView(R.drawable.file_in_folder);
                fileModelArrayList.add(fileModel);
            }

            // Находим список
            ListView fileSystList = null;
            fileSystList = findViewById(R.id.lvMain);
            fileSystList.setAdapter(null);

            // Режим выбора пунктов списка (последний нажатый пункт)
            fileSystList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Создание адаптера
            FileSystemAdapter adapter = new FileSystemAdapter(this, fileModelArrayList);

            //Присваивание адаптера списку
            fileSystList.setAdapter(adapter);

            // добвляем для списка слушатель
            fileSystList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {
                    File f = new File(treeFiles, names_dirs[position]);
                    currDir = names_dirs[position];
                    treeFiles = f;
                    path.add(names_dirs[position]);
                    getInnerFile(f.getAbsolutePath());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FILESYSTEM_JSONERR!!!", e.getMessage());
        }
    }

    public class FileSystemAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<FileModel> fileSystemArrayList;

        public FileSystemAdapter(Context context, ArrayList<FileModel> fileSystemArrayList) {

            this.context = context;
            this.fileSystemArrayList = fileSystemArrayList;
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
            return fileSystemArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileSystemArrayList.get(position);
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
                // Вне этого будем печатать(inflate) и раздувать макет элемента, который мы создали (R.layout.file_system_list_item)
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.file_system_list_item, null, true);

                // Передача родительского эл-та, чтобы завершить передачу параметров(установка информации к новому ViewHolder, который мы недавно создали)
                holder.fileName = convertView.findViewById(R.id.textViewName);
                holder.typeIm = convertView.findViewById(R.id.fs_Icon);
                convertView.setTag(holder);
            }else {
                // the getTag returns the viewHolder object set as a tag to the view
                holder = (ViewHolder)convertView.getTag();
            }

            holder.fileName.setText(fileSystemArrayList.get(position).getName());
            holder.typeIm.setImageResource(fileSystemArrayList.get(position).getView());

            return convertView;
        }

        public void clearData() {
            // clear the data
            fileSystemArrayList.clear();
        }

        class ViewHolder {

            protected TextView fileName;
            protected ImageView typeIm;

        }
    }

    class FileModel {
        private String name;
        private int view;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getView() { return view; }

        public void setView(int view) { this.view = view; }
    }

    public void showMenu (View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void backFolder(View view) {
        String pathNow = treeFiles.toString();;
        String dirNow = currDir;
        int n = pathNow.length();
        int m = dirNow.length();
        String pathBack = pathNow.substring(0, n - m - 1);
        File f = new File(pathBack);
        treeFiles = f;
        int dim = path.size();
        if(dim != 0) {
            path.remove(dim - 1);
            currDir = path.get(path.size() - 1);
        }

        getInnerFile(pathBack);
    }
}
