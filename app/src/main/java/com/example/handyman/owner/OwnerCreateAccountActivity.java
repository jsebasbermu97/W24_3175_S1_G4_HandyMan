package com.example.handyman.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.database.Database;
import com.example.handyman.worker.WorkerCreateAccountActivity;

// to create an owner account
public class OwnerCreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_create_account);

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtName = findViewById(R.id.editTextName);
                String txtNameString = txtName.getText().toString();
                if (txtNameString.isEmpty()){
                    Toast.makeText(OwnerCreateAccountActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText txtEmail = findViewById(R.id.editTextEmailAddress);
                String txtEmailString = txtEmail.getText().toString();
                if (txtEmailString.isEmpty()){
                    Toast.makeText(OwnerCreateAccountActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText txtPassword = findViewById(R.id.editTextCreatePassword);
                String txtPasswordString = txtPassword.getText().toString();
                if (txtPasswordString.isEmpty()) {
                    Toast.makeText(OwnerCreateAccountActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Database database = new Database(OwnerCreateAccountActivity.this);
                SQLiteDatabase db = database.getWritableDatabase();
                database.addOwner(db, txtNameString, txtEmailString, txtPasswordString);

                Toast.makeText(OwnerCreateAccountActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();
                txtName.setText("");
                txtEmail.setText("");
                txtPassword.setText("");
            }
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