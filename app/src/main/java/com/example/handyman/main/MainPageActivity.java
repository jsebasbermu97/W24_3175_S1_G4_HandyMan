package com.example.handyman.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.handyman.R;
import com.example.handyman.adapters.ProfessionAdapter;
import com.example.handyman.professions.CarpenterActivity;
import com.example.handyman.professions.ElectricianActivity;
import com.example.handyman.professions.PainterActivity;
import com.example.handyman.professions.PlumberActivity;
import com.example.handyman.worker.Professions;

import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<String> professions = Professions.getProfessions();

        ListView listViewCategories = findViewById(R.id.listViewCategories);
        ProfessionAdapter professionAdapter = new ProfessionAdapter(professions);
        listViewCategories.setAdapter(professionAdapter);

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedProfession = listViewCategories.getItemAtPosition(position).toString();

                if (selectedProfession.equals("Carpenter")) {
                    startActivity(new Intent(MainPageActivity.this, CarpenterActivity.class));
                } else if(selectedProfession.equals("Electrician")){
                    startActivity(new Intent(MainPageActivity.this, ElectricianActivity.class));
                } else if(selectedProfession.equals("Plumber")){
                    startActivity(new Intent(MainPageActivity.this, PlumberActivity.class));
                } else if(selectedProfession.equals("Painter")){
                    startActivity(new Intent(MainPageActivity.this, PainterActivity.class));
                }

                // Add logic for other professions
                // Create Activity for other professions (same as the CarpenterActivity)
                // Create the xml file for other professions
            }
        });


    }
}