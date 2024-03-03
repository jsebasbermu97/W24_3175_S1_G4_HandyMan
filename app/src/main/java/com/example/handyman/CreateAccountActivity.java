package com.example.handyman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.handyman.owner.OwnerCreateAccountActivity;
import com.example.handyman.worker.WorkerCreateAccountActivity;

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
                    Toast.makeText(CreateAccountActivity.this, "You muse select one of the options", Toast.LENGTH_SHORT).show();
                } else if (selected == R.id.radioButtonOwner) {
                    startActivity(new Intent(CreateAccountActivity.this, OwnerCreateAccountActivity.class));
                } else {
                    startActivity(new Intent(CreateAccountActivity.this, WorkerCreateAccountActivity.class));
                }
            }
        });
    }
}