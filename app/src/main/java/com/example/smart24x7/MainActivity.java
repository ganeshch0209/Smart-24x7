package com.example.smart24x7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime;
    private SendMessage sendMessage;
    private static final int SHAKE_THRESHOLD = 800;

    private String current_accel;
    private String prev_accel;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    private float mAccel;
    private Button btn;
    private Context context;

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            accelerationCurrentValue = Math.sqrt(x * x + y * y + z * z);
            float changeInAcceleration = (float) Math.abs(accelerationCurrentValue - accelerationPreviousValue);
            accelerationPreviousValue = accelerationCurrentValue;

            mAccel = mAccel * 0.9f + changeInAcceleration; // perform low-cut filter

            if (mAccel > 20.0f) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > 2000) {
                    lastShakeTime = currentTime;
                    sendMessage.sendMessagesToAllNumbers();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // Not needed for this implementation
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sendMessage = new SendMessage(MainActivity.this);

        context = this;
        mAccel = 0.00f;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double magnitude = Math.sqrt(x * x + y * y + z * z);

            if (magnitude > SHAKE_THRESHOLD) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastShakeTime > 2000) {
                    lastShakeTime = currentTime;
                    sendMessage.sendMessagesToAllNumbers();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this implementation
    }

    public void addRelative(View v) {
        Intent i = new Intent(getApplicationContext(), AddRelative.class);
        startActivity(i);
    }

    public void helplineNumbers(View v) {
        Intent i = new Intent(getApplicationContext(), helplineCall.class);
        startActivity(i);
    }

    public void triggers(View v) {
        Intent i = new Intent(getApplicationContext(), TrigActivity.class);
        startActivityForResult(i, 1);
    }

    public void HowTo(View v) {
        Intent i = new Intent(getApplicationContext(), HowToSwipe.class);
        startActivity(i);
    }

    public void MyProfile(View v) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "User Not Authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    public void LogOut(View v) {
        Toast.makeText(this, "Successfully Logged Out!", Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean triggerEnabled = data.getBooleanExtra("triggerEnabled", false);
            Toast.makeText(this, "Trigger " + (triggerEnabled ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        }
    }
}
