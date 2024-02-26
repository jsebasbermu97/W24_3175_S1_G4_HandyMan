package com.example.handyman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handyman.database.Database;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---------- Login ---------------
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameInput = findViewById(R.id.editTextUserName);
                EditText passwordInput = findViewById(R.id.editTextPassword);
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                Database db = new Database(MainActivity.this);

                if(db.checkUserCredentials(username, password)) {
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed. Invalid username or password", Toast.LENGTH_LONG).show();
                }
            }
        });


        // ---------- Create and Populate the database ------------
        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // ---------------------------------------------------

        // ---------- for creating account ------------
        TextView txtViewCreateAccount = findViewById(R.id.textViewCreateAccountLink);
        txtViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
            }
        });
        // --------------------------------------------
    }
}