package com.traveloid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    TextView email;
    TextView password;
    TextView name;
    ProgressBar progressBar;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        progressBar = findViewById(R.id.loading);
        profilePic = findViewById(R.id.profile_image);
    }

    public void createAccount(View view) {
    }

    public void loginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}