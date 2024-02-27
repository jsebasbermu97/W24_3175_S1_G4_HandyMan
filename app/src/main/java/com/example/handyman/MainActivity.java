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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // ------------------ Login ------------------
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameInput = findViewById(R.id.editTextUserName);
                EditText passwordInput = findViewById(R.id.editTextPassword);
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter your username and password", Toast.LENGTH_SHORT).show();
                }

                Database db = new Database(MainActivity.this);

                if(db.checkUserCredentials(username, password)) {
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed. Invalid username or password", Toast.LENGTH_LONG).show();
                }
            }
        });
        // -----------------------------------------------

        // ----------------- Google sign in --------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton btnGoogle = findViewById(R.id.buttonGoogle);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }
        });
        // -----------------------------------------------


        // ---------- Create and Populate the database ------------
        Database dbHelper = new Database(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // ---------------------------------------------------

        // ---------- for Creating account ------------
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