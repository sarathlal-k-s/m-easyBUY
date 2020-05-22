package com.sarath.m_easybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editTextUsername,editTextEmail,editTextPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button regButton;
        TextView loginText;

        loginText = findViewById(R.id.have_account_login);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        regButton = findViewById(R.id.RegisterButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.usernameRegister);
        editTextEmail = findViewById(R.id.emailRegister);
        editTextPassword = findViewById(R.id.passwordRegister);
        progressBar = findViewById(R.id.progressbar);
        fstore = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.INVISIBLE);
    }

    public void openLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFeed(){
        Intent intent = new Intent(this,FeedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void registerUser(){
        progressBar.setVisibility(View.VISIBLE);
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(username.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            editTextUsername.setError("Username Required");
            editTextUsername.requestFocus();
            return;
        }
        if (username.contains(" ")) {
            progressBar.setVisibility(View.INVISIBLE);
            editTextUsername.setError("No Spaces Allowed");
            editTextUsername.requestFocus();
            return;
        }


        if(email.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.setError("Email-id Required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            progressBar.setVisibility(View.INVISIBLE);
            editTextEmail.setError("Enter a valid email-id");
            editTextEmail.requestFocus();
            return;
        }


        if(password.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            progressBar.setVisibility(View.INVISIBLE);
            editTextPassword.setError("Minimum length of password is 6");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    userid = user.getUid();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                    user.updateProfile(profileUpdates);

                    DocumentReference reference = fstore.collection("users").document(userid);
                    Map<String,Object> userDetails = new HashMap<>();
                    userDetails.put("name",username);
                    userDetails.put("email",email);
                    reference.set(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                openFeed();
                                Toast.makeText(getApplicationContext(), "User Registered. Logged In", Toast.LENGTH_LONG).show();
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Error During Storing User Data", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else if(task.getException() != null){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
