package com.example.smart24x7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check if the required permissions are granted
        if (arePermissionsGranted()) {
            proceedToRegistration();
        } else {
            // Request the necessary permissions
            requestPermissions();
        }
    }

    private boolean arePermissionsGranted() {
        // Check if the necessary permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int smsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

            return locationPermission == PackageManager.PERMISSION_GRANTED &&
                    smsPermission == PackageManager.PERMISSION_GRANTED;
        }
        return true; // If SDK version is lower than M, permissions are considered granted
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all permissions are granted
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                proceedToRegistration();
            } else {
                Toast.makeText(this, "Permissions not Granted. The App will close.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void proceedToRegistration() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(Splash.this, Registration.class);
            startActivity(intent);
            finish();
        }, 5000);
    }
}
