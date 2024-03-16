package com.example.handyman.professions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.adapters.WorkersAdapter;
import com.example.handyman.database.Database;
import com.example.handyman.worker.Worker;
import com.example.handyman.worker.WorkerProfileActivity;

import java.util.List;

public class CarpenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carpenter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Database db = new Database(this);
        List<Worker> carpenters = db.getWorkersByProfession("Carpenter");

        ListView carpentersListView = findViewById(R.id.listViewCarpenters);
        WorkersAdapter carpenterAdapter = new WorkersAdapter(this, carpenters);
        carpentersListView.setAdapter(carpenterAdapter);

        carpentersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Worker selectedWorker = carpenters.get(position);

                Intent intent = new Intent(CarpenterActivity.this, WorkerProfileActivity.class);
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