package com.example.handyman.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.handyman.R;
import com.example.handyman.job.Job;

import java.util.List;

// to show the jobs that are related to the worker
public class WorkerJobAdapter extends BaseAdapter {

    List<Job> adapterJobList;

    public WorkerJobAdapter(List<Job> adapterJobList) {
        this.adapterJobList = adapterJobList;
    }

    @Override
    public int getCount() {
        return adapterJobList.size();
    }

    @Override
    public Object getItem(int position) {
        return adapterJobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_worker_jobs, parent, false);
        }

        TextView txtViewJobs = convertView.findViewById(R.id.textViewWorkerJob);
        Job job = adapterJobList.get(position);
        String title = job.getTitle();
        txtViewJobs.setText(title != null ? title : "No Title");
        txtViewJobs.setGravity(Gravity.CENTER_VERTICAL);
        return convertView;
    }

    public void updateData(List<Job> newJobs) {
        adapterJobList.clear();
        adapterJobList.addAll(newJobs);
        notifyDataSetChanged();
    }
}
