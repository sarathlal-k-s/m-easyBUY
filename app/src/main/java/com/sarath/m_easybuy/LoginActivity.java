package com.sarath.m_easybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.no_account_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void openRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFeed(){
        Intent intent = new Intent(this,FeedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void loginUser(){
        passwordEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
        emailEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
        progressBar.setVisibility(View.VISIBLE);
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(email.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            emailEditText.setError("Email-id Required");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            progressBar.setVisibility(View.INVISIBLE);
            emailEditText.setError("Enter a valid email-id");
            emailEditText.requestFocus();
            return;
        }

        if(password.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            passwordEditText.setError("Password Required");
            passwordEditText.requestFocus();
            return;
        }
        if(password.length()<6){
            progressBar.setVisibility(View.INVISIBLE);
            passwordEditText.setError("Minimum length of password is 6");
            passwordEditText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
                    openFeed();
                }
                else if(task.getException() != null){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
