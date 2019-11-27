package com.hpccloud.ssd.sscc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class JobsAdapter extends BaseAdapter{
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null, true);

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

    private class ViewHolder {

        protected TextView jobName, jobState;

    }
}
