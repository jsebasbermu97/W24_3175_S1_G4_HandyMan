package com.example.handyman.professions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.handyman.R;
import com.example.handyman.adapters.WorkersAdapter;
import com.example.handyman.database.Database;
import com.example.handyman.worker.Worker;
import com.example.handyman.worker.WorkerProfileActivity;

import java.util.List;

public class ElectricianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrician);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Database db = new Database(this);
        List<Worker> electricians = db.getWorkersByProfession("Electrician");

        ListView electriciansListView = findViewById(R.id.listViewElectricians);
        WorkersAdapter electricianAdapter = new WorkersAdapter(this, electricians);
        electriciansListView.setAdapter(electricianAdapter);

        electriciansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Worker selectedWorker = electricians.get(position);

                Intent intent = new Intent(ElectricianActivity.this, WorkerProfileActivity.class);
                intent.putExtra("id", selectedWorker.id);
                intent.putExtra("Name", selectedWorker.name);
                intent.putExtra("Email", selectedWorker.email);
                intent.putExtra("Profession", selectedWorker.profession);
                intent.putExtra("Address", selectedWorker.address);

                startActivity(intent);
            }
        });
    }
}