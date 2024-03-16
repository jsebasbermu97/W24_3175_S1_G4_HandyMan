package com.example.handyman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handyman.database.Database;
import com.example.handyman.main.MainPageActivityOwner;
import com.example.handyman.main.MainPageActivityWorker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Database.deleteDatabase(this); // TODO: delete this line later

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
                String userType = db.checkUserCredentials(username, password);

                if (userType != null) {
                    Intent intent;
                    if ("owner".equals(userType)) {
                        int userId = db.getOwnerIdByEmail(username);
                        String ownerName = db.getOwnerNameByEmail(username);
                        saveOwnerId(userId);
                        saveOwnerName(ownerName);
                        intent = new Intent(MainActivity.this, MainPageActivityOwner.class);
                    } else {
                        int workerId = db.getWorkerIdByEmail(username);
                        String workerName = db.getWorkerNameByEmail(username);
                        saveWorkerId(workerId);
                        saveWorkerName(workerName);
                        intent = new Intent(MainActivity.this, MainPageActivityWorker.class);
                    }
                    startActivity(intent);
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
        Database database = new Database(this);
        SQLiteDatabase db = database.getWritableDatabase();
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

    // ------------------ Checking the Sign in with Firebase ------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign in Failed - Internal Server Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Google Authentication successful
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Extract name and email from the Google account
                                String name = user.getDisplayName();
                                String email = user.getEmail();

                                Database database = new Database(MainActivity.this);
                                SQLiteDatabase db = database.getWritableDatabase();
                                database.addGoogleOwner(db, name, email);
                                // save owner id in shared preferences
                                int ownerId = database.getOwnerIdByEmail(email);
                                saveOwnerId(ownerId);
                            }
                            startActivity(new Intent(MainActivity.this, MainPageActivityOwner.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    // --------------------------------------------------------------------
    // ----- save the owner and worker id and name that is logged in inside SharedPreference
    private void saveOwnerId(int ownerId) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ownerId", ownerId);
        editor.apply();
    }

    private void saveOwnerName(String ownerName){
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ownerName", ownerName);
        editor.apply();
    }

    private void saveWorkerId(int workerId) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("workerId", workerId);
        editor.apply();
    }

    private void saveWorkerName(String workerName){
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("workerName", workerName);
        editor.apply();
    }
}