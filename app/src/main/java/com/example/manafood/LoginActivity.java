package com.example.manafood;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.manafood.databinding.ActivityLoginBinding;
import com.example.manafood.model.GoogleUserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private String email;
    private String password;

    //firebase
    private FirebaseAuth auth;
    DatabaseReference database;

    //google sign-in
    private GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        //initialize Firebase database
        database = FirebaseDatabase.getInstance().getReference();
        //Google sign-in
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.loginbutton.setOnClickListener(v -> {
            //get text from editTexts
            email = binding.loginEmail.getText().toString().trim();
            password = binding.loginPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all details ", Toast.LENGTH_SHORT).show();
            } else {
                signIn(email, password);
            }
        });

        binding.loginGoogleButton.setOnClickListener(v->{
            Intent signInIntent = googleSignInClient.getSignInIntent();
            launcher.launch(signInIntent);
        });

        binding.donthavebutton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });


    }

    //launcher for google sign-in
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResult -> {
        if (activityResult.getResultCode() == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.getData());
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                String userName = account.getDisplayName();
                String userEmail = account.getEmail();
                String userIdToken = account.getIdToken();
                AuthCredential credential = GoogleAuthProvider.getCredential(userIdToken, null);
                auth.signInWithCredential(credential).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        saveGoogleUserdata(userName,userEmail);
                        updateUi();
                        finish();
                    } else {
                        Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    });


    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //FirebaseUser user = auth.getCurrentUser();
                updateUi();
                finish();
            } else {
                Toast.makeText(this, "Authentication is Failed", Toast.LENGTH_SHORT).show();
                Log.e("Account", "signIn: Failed", task.getException());
            }
        });
    }

    private void saveGoogleUserdata(String userName,String userEmail) {
        FirebaseUser currentUser = auth.getCurrentUser();
        GoogleUserModel googleUserModel = new GoogleUserModel(userName, userEmail, null, null);
        if (currentUser != null) {
            String userId = currentUser.getUid();
            database.child("user").child(userId).setValue(googleUserModel);
        }
    }

    //if user is logged in then directly redirect to main page
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
        startActivity(new Intent(this, MainActivity.class));
        finish();
        }
    }

    private void updateUi() {
        startActivity(new Intent(this, MainActivity.class));
    }
}