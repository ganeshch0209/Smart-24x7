package com.example.smart24x7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterbtn;
    TextView mCreateText;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterbtn = findViewById(R.id.registerbtn);
        mCreateText = findViewById(R.id.loginUser);
        mProgressBar = findViewById(R.id.progressbar1);

        mCreateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                String fullname = mFullName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();

                if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(Registration.this, "Enter Full Name!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Registration.this, "Enter Email!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Registration.this, "Enter Password!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(Registration.this, "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();

                                        // Create a user details map
                                        Map<String, Object> userDetails = new HashMap<>();
                                        userDetails.put("fullname", fullname);
                                        userDetails.put("email", email);
                                        userDetails.put("phone", phone);
                                        userDetails.put("password", password);
                                        userDetails.put("uid", userId); // Store the UID in Firestore

                                        // Store the user details in Firestore
                                        mFirestore.collection("users")
                                                .document(userId)
                                                .set(userDetails)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Registration.this, "Account Created Successfully!",
                                                                    Toast.LENGTH_SHORT).show();

                                                            // Pass the user ID to the MainActivity
                                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                            intent.putExtra("userId", userId);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Registration.this, "Failed to store user details.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    // Handle specific authentication failures
                                    String errorMessage = task.getException().getMessage();
                                    if (errorMessage != null && errorMessage.contains("WEAK_PASSWORD")) {
                                        Toast.makeText(Registration.this, "Password is too weak. Please choose a stronger password.", Toast.LENGTH_SHORT).show();
                                    } else if (errorMessage != null && errorMessage.contains("INVALID_EMAIL")) {
                                        Toast.makeText(Registration.this, "Invalid email format. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // For other authentication failures
                                        Toast.makeText(Registration.this, "Authentication Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
    }
}
