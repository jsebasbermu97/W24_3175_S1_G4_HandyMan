package com.example.handyman.job;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.handyman.R;
import com.example.handyman.adapters.UserJobAdapter;
import com.example.handyman.database.Database;

import java.util.ArrayList;
import java.util.List;

// to show jobs in the fragment on user main page
public class JobsFragment extends Fragment {

    private UserJobAdapter adapter;

    public JobsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jobs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listViewJobs = view.findViewById(R.id.listViewJobs);
        adapter = new UserJobAdapter(new ArrayList<>());
        listViewJobs.setAdapter(adapter);

        // open the job information page in send data to it
        listViewJobs.setOnItemClickListener((parent, view1, position, id) -> {
            Job job = (Job) adapter.getItem(position);

            Intent intent = new Intent(getActivity(), JobInformation.class);
            intent.putExtra("JobId", job.getId());
            intent.putExtra("JobOwnerId", job.getOwner_id());
            intent.putExtra("JobWorkerId", job.getWorker_id());
            intent.putExtra("JobTitle", job.getTitle());
            intent.putExtra("JobDescription", job.getDescription());
            intent.putExtra("JobBudget", job.getBudget());
            intent.putExtra("JobStartDate", job.getStartDate());
            intent.putExtra("JobEndDate", job.getEndDate());

            startActivity(intent);
        });

        loadJobs();
    }


    // update the jobs when comes back to the page
    @Override
    public void onResume() {
        super.onResume();
        loadJobs();
    }

    private void loadJobs() {
        if (getActivity() == null) return;
        Database dbHelper = new Database(getActivity());
        List<Job> jobList = dbHelper.getJobsForUser(getCurrentUserId());
        adapter.updateData(jobList);
    }

    private int getCurrentUserId() {
        if (getActivity() == null) return -1;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ownerId", -1);
    }
}
