package com.example.handyman.job;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handyman.R;

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


        Intent intent = getIntent();
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
    }
}