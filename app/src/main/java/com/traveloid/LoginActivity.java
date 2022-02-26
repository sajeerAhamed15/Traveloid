package com.traveloid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.traveloid.api.FirebaseApi;
import com.traveloid.model.User;
import com.traveloid.utils.SharedPrefUtils;

public class LoginActivity extends AppCompatActivity {

    TextView email;
    TextView password;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.loading);

        progressBar.setVisibility(View.INVISIBLE);
    }

    public void loginClicked(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String _email = email.getText().toString();
        String _password = password.getText().toString();
        mAuth.signInWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LoginActivity", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            fetchAndLogin(user);
                        } else {
                            Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                            updateUI();
                        }
                    }
                });
    }

    private void fetchAndLogin(FirebaseUser user) {
        FirebaseApi.getUser(user.getEmail()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    User _user = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        _user = document.toObject(User.class);
                        break;
                    }
                    User final_user = _user;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPrefUtils.saveInSP(final_user, LoginActivity.this);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
                }
            }
        });
    }

    private void updateUI() {
        progressBar.setVisibility(View.INVISIBLE);
        password.setText("");
        password.setError("Authentication Failed");
        password.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
    }

    public void signUpClicked(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            startActivity(new Intent(this, MainActivity.class));
//        }
        if (SharedPrefUtils.getUserFromSP(this) != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}