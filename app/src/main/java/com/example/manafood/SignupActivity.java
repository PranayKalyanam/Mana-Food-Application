package com.example.manafood;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.manafood.databinding.ActivitySignupBinding;
import com.example.manafood.model.GoogleUserModel;
import com.example.manafood.model.UserModel;
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

public class SignupActivity extends AppCompatActivity {

    private String username;
    private String email;
    private String password;


    //Firebase
    private FirebaseAuth auth;
    private DatabaseReference database;

    //google sign-in
    private GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //Initializing firebase auth
        auth = FirebaseAuth.getInstance();
        //Initializing firebase database
        database = FirebaseDatabase.getInstance().getReference();
        //Google sign-in
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.signUpCreateButton.setOnClickListener(v -> {
            //get text from edittext fields
            username = binding.signUpUserName.getText().toString().trim();
            email = binding.signUpEmail.getText().toString().trim();
            password = binding.signUpPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            } else {
                createAccount(email, password);
            }
        });

        binding.signUpGoogleButton.setOnClickListener(v -> {
            Intent signIntent = googleSignInClient.getSignInIntent();
            launcher.launch(signIntent);

        });

        binding.haveanaccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
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
                        saveGoogleUserdata(userName, userEmail);
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

    private void saveGoogleUserdata(String userName, String userEmail) {
        FirebaseUser currentUser = auth.getCurrentUser();
        GoogleUserModel googleUserModel = new GoogleUserModel(userName,userEmail, null, null);
        if (currentUser != null) {
            String userId = currentUser.getUid();
            database.child("user").child(userId).setValue(googleUserModel);
        }
    }


    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                saveUserData(user);
                updateUi();
                finish();
            } else {
                Toast.makeText(this, "Account is not Created", Toast.LENGTH_SHORT).show();
                Log.d("Account", "createAccount: Failure", task.getException());
            }
        });
    }

    private void updateUi() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void saveUserData(FirebaseUser user) {
        username = binding.signUpUserName.getText().toString();
        email = binding.signUpEmail.getText().toString();
        password = binding.signUpPassword.getText().toString();

        UserModel userModel = new UserModel(username, email, password, null, null);

        if (user != null) {
            //get a unique id
            String userid = user.getUid();
            //save user data in Firebase database
            database.child("user").child(userid).setValue(userModel);
        } else {
            Log.e("SaveUserData", "CurrentUserData: null");
        }

    }
}