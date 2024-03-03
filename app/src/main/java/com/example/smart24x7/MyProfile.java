package com.example.smart24x7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MyProfile extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;

    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);


        // Initialize the Firestore instance
        db = FirebaseFirestore.getInstance();

        // Find the TextViews in the layout
        fullNameTextView = findViewById(R.id.fullname);
        emailTextView = findViewById(R.id.email);
        phoneNumberTextView = findViewById(R.id.phoneno);

        // Get the user ID from the intent extra
        userID = getIntent().getStringExtra("userId");

        // Fetch the data from Firestore and update the TextViews
        fetchProfileData();
    }

    private void fetchProfileData() {
        // Retrieve the user details from Firestore using the provided user ID
        db.collection("users").document(userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve the data from the document
                            String fullName = document.getString("fullname");
                            String email = document.getString("email");
                            String phoneNumber = document.getString("phone");

                            // Set the retrieved data to the TextViews
                            fullNameTextView.setText(fullName);
                            emailTextView.setText(email);
                            phoneNumberTextView.setText(phoneNumber);
                        }
                    }
                });
    }

}
