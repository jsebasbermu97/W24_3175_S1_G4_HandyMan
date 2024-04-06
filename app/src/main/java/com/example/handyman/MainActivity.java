package com.example.handyman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {


    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ConstraintLayout mMainLayout;

    private FirebaseAuth mAuth; // firebase authorization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // --------------- biometric login ----------------------------

        mMainLayout= findViewById(R.id.main_layout);

        if (mMainLayout.getVisibility() != View.VISIBLE && isBiometricPromptEnabled()) {
            showBiometricPrompt();
        } else {
            mMainLayout.setVisibility(View.VISIBLE);
        }

        // ----- if you want to clear database and fill it with basic data, un-comment this line and run the program once ------
        // remember to comment it out again

        // Database.deleteDatabase(this);


        // ------------------ Login ------------------

        mAuth = FirebaseAuth.getInstance();
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
                saveTypeOfUser(userType);
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

        // ----------------- Google sign in --------------
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.revokeAccess();

        SignInButton btnGoogle = findViewById(R.id.buttonGoogle);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }
        });


        // ---------- Create and Populate the database ------------
        Database database = new Database(this);
        SQLiteDatabase db = database.getWritableDatabase();


        // ---------- for Creating account ------------
        TextView txtViewCreateAccount = findViewById(R.id.textViewCreateAccountLink);
        txtViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
            }
        });

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

    // --------------- for biometric login ------------------
    // if you are using the virtual device you cannot see the biometric login, since it doesn't support it
    // if you are using the physical device you can see the biometric login

    private void showBiometricPrompt() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL);

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            Executor executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    if (errorCode == BiometricPrompt.ERROR_USER_CANCELED || errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                        navigateToMainAppFunctionality();
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(MainActivity.this, "Authentication succeeded.", Toast.LENGTH_SHORT).show();
                    navigateToMainAppFunctionality();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Login")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else {
            navigateToMainAppFunctionality();
        }
    }

    private void navigateToMainAppFunctionality() {
        mMainLayout.setVisibility(View.VISIBLE);
    }

    private void setBiometricPromptEnabled(boolean isEnabled) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("BiometricPromptEnabled", isEnabled);
        editor.apply();
    }

    private boolean isBiometricPromptEnabled() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("BiometricPromptEnabled", true);
    }


    // ------- save the owner/worker id, name, type that is logged in inside SharedPreference ---------
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

    private void saveTypeOfUser(String userType){
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userType", userType);
        editor.apply();
    }
}