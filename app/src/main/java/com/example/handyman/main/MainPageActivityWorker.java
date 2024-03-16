package com.example.handyman.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handyman.R;
import com.example.handyman.adapters.WorkerJobAdapter;
import com.example.handyman.database.Database;
import com.example.handyman.job.Job;
import com.example.handyman.worker.Worker;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivityWorker extends AppCompatActivity {

    private WorkerJobAdapter workerJobAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page_worker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ListView listViewJobs = findViewById(R.id.listViewWorkerJobs);
        workerJobAdapter = new WorkerJobAdapter(new ArrayList<>());
        listViewJobs.setAdapter(workerJobAdapter);

        loadJobs();
    }



    private void loadJobs() {
        int workerId = getCurrentWorkerId();
        if (workerId != -1) {
            Database dbHelper = new Database(MainPageActivityWorker.this);
            List<Job> jobList = dbHelper.getJobsForWorker(workerId);
            workerJobAdapter.updateData(jobList);
        } else {
            Toast.makeText(MainPageActivityWorker.this, "Worker ID not found.", Toast.LENGTH_SHORT).show();
        }
    }


    private int getCurrentWorkerId() {
        SharedPreferences sharedPreferences = MainPageActivityWorker.this.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ownerId", -1);
    }
}