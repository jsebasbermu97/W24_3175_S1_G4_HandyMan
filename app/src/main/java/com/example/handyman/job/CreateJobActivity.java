package com.example.handyman.job;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.database.Database;
import com.example.handyman.main.MainPageActivityOwner;

public class CreateJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int workerId = getIntent().getIntExtra("workerId", -1);
        int ownerId = getIntent().getIntExtra("ownerId", -1);

        Button buttonCreateJob = findViewById(R.id.buttonCreateJobFinal);
        buttonCreateJob.setOnClickListener(v -> {
            String title = ((EditText) findViewById(R.id.editTextJobTitle)).getText().toString();
            String description = ((EditText) findViewById(R.id.editTextJobDescription)).getText().toString();
            String startDate = ((EditText) findViewById(R.id.editTextJobStartDate)).getText().toString();
            String endDate = ((EditText) findViewById(R.id.editTextJobEndDate)).getText().toString();
            double budget = Double.parseDouble(((EditText) findViewById(R.id.editTextJobBudget)).getText().toString());

            Database db = new Database(CreateJobActivity.this);
            db.addJob(workerId, ownerId, title, description, startDate, endDate, budget);

            Toast.makeText(CreateJobActivity.this, "Job Created Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateJobActivity.this, MainPageActivityOwner.class));
        });
    }

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