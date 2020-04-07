package com.example.newcommunity.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newcommunity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail, userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private CircleImageView loginPhoto;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            updateUI();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_button);
        loginProgress = findViewById(R.id.login_progress);
        loginPhoto = findViewById(R.id.login_photo);

        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.VISIBLE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String userEmail = userMail.getText().toString();
                final String userPass = userPassword.getText().toString();

                if (userEmail.isEmpty() || userPass.isEmpty()){
                    showMessage("Pls verify the input fields");
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                }else{
                    singIn(userEmail,userPass);
                }
            }
        });

    }

    private void singIn(String userEmail, String userPass) {
        mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    btnLogin.setVisibility(View.INVISIBLE);
                    loginProgress.setVisibility(View.VISIBLE);
                    updateUI();
                }else{
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    showMessage("Error: " + task.getException().getMessage());
                }
            }
        });
    }

    private void updateUI() {
        Intent homeActivity = new Intent(LoginActivity.this, Home.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
