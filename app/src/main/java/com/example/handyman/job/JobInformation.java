package com.example.handyman.job;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handyman.ChatActivity;
import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.database.Database;
import com.example.handyman.worker.WorkerProfileActivity;

// to show the job information, and end the job
public class JobInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get the information of the job from previous activity and list
        Intent intent = getIntent();
        int jobId = intent.getIntExtra("JobId", -1);
        int owner_id = intent.getIntExtra("JobOwnerId",-1);
        int worker_id = intent.getIntExtra("JobWorkerId", -1);

        Log.d("worker id", String.valueOf(worker_id));
        Log.d("owner id", String.valueOf(owner_id));

        String title = intent.getStringExtra("JobTitle");
        String description = intent.getStringExtra("JobDescription");
        double budget = intent.getDoubleExtra("JobBudget", 0);
        String startDate = intent.getStringExtra("JobStartDate");
        String endDate = intent.getStringExtra("JobEndDate");

        ((TextView) findViewById(R.id.textViewJobTitle)).setText(title);
        ((TextView) findViewById(R.id.textViewJobDescription)).setText(description);
        ((TextView) findViewById(R.id.textViewJobBudget)).setText(String.valueOf(budget));
        ((TextView) findViewById(R.id.textViewJobStartDate)).setText(startDate);
        ((TextView) findViewById(R.id.textViewJobEndDate)).setText(endDate);


        Database db = new Database(JobInformation.this);
        String workerName = db.getWorkerNameById(worker_id);
        String workerProfession = db.getWorkerProfessionById(worker_id);
        String ownerName = db.getOwnerNameById(owner_id);

        Log.d("worker name", workerName);
        Log.d("worker profession", workerProfession);
        Log.d("owner name", ownerName);


        // ------------------ open chat button logic -----------------------

        Button buttonOpenChat = findViewById(R.id.btnOpenChat);

        buttonOpenChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Database db = new Database(JobInformation.this);
                String workerName = db.getWorkerNameById(worker_id);
                String workerProfession = db.getWorkerProfessionById(worker_id);
                String ownerName = db.getOwnerNameById(owner_id);

                Intent intent = new Intent(JobInformation.this, ChatActivity.class);
                intent.putExtra("workerName", workerName);
                intent.putExtra("workerProfession", workerProfession);
                intent.putExtra("ownerName", ownerName);

                startActivity(intent);

            }
        });

        // remove the job from table
        Button buttonJobEnd = findViewById(R.id.buttonJobEnd);

        buttonJobEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jobId != -1) {
                    Database dbHelper = new Database(JobInformation.this);
                    dbHelper.deleteJobById(jobId);

                    Toast.makeText(JobInformation.this, "Job finished and deleted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(JobInformation.this, "Error: Job ID not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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