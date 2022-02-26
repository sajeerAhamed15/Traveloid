package com.traveloid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.traveloid.api.FirebaseApi;
import com.traveloid.model.Hike;
import com.traveloid.model.User;
import com.traveloid.utils.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    TextView email;
    TextView password;
    TextView name;
    ProgressBar progressBar;
    ImageView profilePic;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        progressBar = findViewById(R.id.loading);
        profilePic = findViewById(R.id.profile_image);

        progressBar.setVisibility(View.INVISIBLE);
    }

    public void createAccount(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUpActivity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveAndLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Creating user failed.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void saveAndLogin(FirebaseUser user) {
        String _name = name.getText().toString();
        String _profileUrl = "";

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(_name);
        newUser.setImage(_profileUrl);

        FirebaseApi.saveUser(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("saveAndLogin", "DocumentSnapshot added with ID: " + documentReference.getId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPrefUtils.saveInSP(newUser, SignUpActivity.this);
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("saveAndLogin", "Error adding document", e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignUpActivity.this, "Creating user failed.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
    }

    public void loginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}