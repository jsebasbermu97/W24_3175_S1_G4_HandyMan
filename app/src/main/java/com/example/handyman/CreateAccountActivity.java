package com.example.handyman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.handyman.owner.OwnerCreateAccountActivity;
import com.example.handyman.worker.WorkerCreateAccountActivity;

// -------------- to show the page that you can choose owner or worker type --------------
public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button buttonAccount = findViewById(R.id.buttonAccount);
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = findViewById(R.id.radioGroupUserType);
                int selected = radioGroup.getCheckedRadioButtonId();
                if (selected == -1) {
                    Toast.makeText(CreateAccountActivity.this, "You must select one of the options", Toast.LENGTH_SHORT).show();
                } else if (selected == R.id.radioButtonOwner) {
                    startActivity(new Intent(CreateAccountActivity.this, OwnerCreateAccountActivity.class));
                } else {
                    startActivity(new Intent(CreateAccountActivity.this, WorkerCreateAccountActivity.class));
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