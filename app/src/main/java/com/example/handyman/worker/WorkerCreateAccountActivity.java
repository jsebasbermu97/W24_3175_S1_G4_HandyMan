package com.example.handyman.worker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.database.Database;
import com.example.handyman.professions.Professions;

// -------------- to create worker account --------------
public class WorkerCreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_create_account);

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        Spinner spinnerProfession = findViewById(R.id.spinnerProfession);
        spinnerProfession.setAdapter(new ArrayAdapter<Professions>(WorkerCreateAccountActivity.this, android.R.layout.simple_spinner_item, Professions.values()));

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtName = findViewById(R.id.editTextName);
                String txtNameString = txtName.getText().toString();
                if (txtNameString.isEmpty()){
                    Toast.makeText(WorkerCreateAccountActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText txtEmail = findViewById(R.id.editTextEmailAddress);
                String txtEmailString = txtEmail.getText().toString();
                if (txtEmailString.isEmpty()){
                    Toast.makeText(WorkerCreateAccountActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText txtPassword = findViewById(R.id.editTextCreatePassword);
                String txtPasswordString = txtPassword.getText().toString();
                if (txtPasswordString.isEmpty()) {
                    Toast.makeText(WorkerCreateAccountActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Spinner spinnerProfession = findViewById(R.id.spinnerProfession);
                String txtProfessionString = spinnerProfession.getSelectedItem().toString();
                if (txtProfessionString.isEmpty()) {
                    Toast.makeText(WorkerCreateAccountActivity.this, "Please select your profession", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText txtAddress = findViewById(R.id.editTextAddress);
                String txtAddressString = txtAddress.getText().toString();
                if (txtAddressString.isEmpty()) {
                    Toast.makeText(WorkerCreateAccountActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }


                Database database = new Database(WorkerCreateAccountActivity.this);
                SQLiteDatabase db = database.getWritableDatabase();

                database.addWorker(db, txtNameString, txtEmailString, txtProfessionString, txtPasswordString, txtAddressString);

                Toast.makeText(WorkerCreateAccountActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();
                txtName.setText("");
                txtEmail.setText("");
                txtPassword.setText("");

                // creating a bundle to pass information to next activity
                Bundle bundle = new Bundle();
                bundle.putString("Name", txtNameString);
                bundle.putString("Profession", txtProfessionString);
                bundle.putString("Address", txtAddressString);
                Intent intent = new Intent(WorkerCreateAccountActivity.this, WorkerProfileActivity.class);
                intent.putExtras(bundle);
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