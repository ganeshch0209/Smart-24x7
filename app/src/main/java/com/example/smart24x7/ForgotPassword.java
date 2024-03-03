package com.example.smart24x7;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPassword extends Activity {
    private EditText emailEditText;
    private Button sendPasswordButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firestore = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.email);
        sendPasswordButton = findViewById(R.id.sendpassword);

        sendPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordToEmail();
            }
        });
    }

    private void sendPasswordToEmail() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid Email address", Toast.LENGTH_SHORT).show();
        } else {
            // Retrieve password from Firestore
            retrievePasswordFromFirestore(email);
        }
    }

    private void retrievePasswordFromFirestore(String email) {
        firestore.collection("users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                String storedEmail = document.getString("email");
                                if (storedEmail.equals(email)) {
                                    String password = document.getString("password");
                                    sendPassword(email, password);
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Email not found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ForgotPassword.this, "Email not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ForgotPassword.this, "Failed to retrieve Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendPassword(String email, String password) {
        String message = "Email: " + email + "\nPassword: " + password;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
