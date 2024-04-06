package com.example.handyman.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.adapters.WorkerJobAdapter;
import com.example.handyman.bottomsheet.RatingBottomSheet;
import com.example.handyman.database.Database;
import com.example.handyman.job.Job;
import com.example.handyman.job.JobInformation;
import com.example.handyman.worker.Worker;

import java.util.ArrayList;
import java.util.List;

// the main page for workers that contains the jobs
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // -------------- for rating system pop-up ----------------------
        SharedPreferences sharedPreferences = getSharedPreferences("ratings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // it checks if it is the first time the app is open or not, then show the pop-up windows
        if (sharedPreferences.getBoolean("appOpenFirstTimeWorker"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("workerEmail",""), true)){
            editor.putBoolean("appOpenFirstTimeWorker"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("workerEmail",""),false);
            editor.apply();
        }else if (!sharedPreferences.getBoolean("appOpenFirstTimeWorker"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("workerEmail",""), true) && !sharedPreferences.getBoolean("ratedWorker"+getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getString("workerEmail",""), false)){
            //Toast.makeText(this, "Not Rated!", Toast.LENGTH_SHORT).show();
            RatingBottomSheet ratingBottomSheet = new RatingBottomSheet(this, "Worker");
            ratingBottomSheet.show();
        }


        ListView listViewJobs = findViewById(R.id.listViewWorkerJobs);
        workerJobAdapter = new WorkerJobAdapter(new ArrayList<>());
        listViewJobs.setAdapter(workerJobAdapter);

        loadJobs();

        // show the list of jobs on listView
        listViewJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Job job = (Job) workerJobAdapter.getItem(position);

                // pass the job information to the next activity
                Intent intent = new Intent(MainPageActivityWorker.this, JobInformation.class);
                intent.putExtra("JobId", job.getId());
                intent.putExtra("JobOwnerId", job.getOwner_id());
                intent.putExtra("JobWorkerId", job.getWorker_id());
                intent.putExtra("JobTitle", job.getTitle());
                intent.putExtra("JobDescription", job.getDescription());
                intent.putExtra("JobBudget", job.getBudget());
                intent.putExtra("JobStartDate", job.getStartDate());
                intent.putExtra("JobEndDate", job.getEndDate());

                startActivity(intent);
            }
        });
    }

    // load jobs again when back to the activity to bring the updated jobs
    @Override
    protected void onResume() {
        super.onResume();
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
        return sharedPreferences.getInt("workerId", -1);
    }

    // ------------- for home button ----------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}