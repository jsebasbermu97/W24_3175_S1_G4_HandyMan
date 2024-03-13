package com.example.handyman.job;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.handyman.R;
import com.example.handyman.adapters.JobAdapter;
import com.example.handyman.database.Database;
import com.example.handyman.job.Job;

import java.util.ArrayList;
import java.util.List;

public class JobsFragment extends Fragment {

    private JobAdapter adapter;

    public JobsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jobs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listViewJobs = view.findViewById(R.id.listViewJobs);
        adapter = new JobAdapter(new ArrayList<>());
        listViewJobs.setAdapter(adapter);
        loadJobs();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadJobs(); // update the jobs
    }

    private void loadJobs() {
        if (getActivity() == null) return;
        Database dbHelper = new Database(getActivity());
        List<Job> jobList = dbHelper.getJobsForUser(getCurrentUserId());
        adapter.updateData(jobList); // Update adapter data
    }

    private int getCurrentUserId() {
        if (getActivity() == null) return -1;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ownerId", -1);
    }
}
