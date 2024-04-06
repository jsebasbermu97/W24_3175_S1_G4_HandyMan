package com.example.handyman.professions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.adapters.WorkersAdapter;
import com.example.handyman.database.Database;
import com.example.handyman.worker.Worker;
import com.example.handyman.worker.WorkerProfileActivity;

import java.util.List;

public class PainterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Database db = new Database(this);
        List<Worker> painters = db.getWorkersByProfession("Painter");

        ListView paintersListView = findViewById(R.id.listViewPainters);
        WorkersAdapter painterAdapter = new WorkersAdapter(this, painters);
        paintersListView.setAdapter(painterAdapter);

        paintersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Worker selectedWorker = painters.get(position);

                // pass the worker information to the next activity
                Intent intent = new Intent(PainterActivity.this, WorkerProfileActivity.class);
                intent.putExtra("id", selectedWorker.id);
                intent.putExtra("Name", selectedWorker.name);
                intent.putExtra("Email", selectedWorker.email);
                intent.putExtra("Profession", selectedWorker.profession);
                intent.putExtra("Address", selectedWorker.address);

                startActivity(intent);
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