import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime;
    private SendMessage sendMessage;
    private static final float DEFAULT_SHAKE_THRESHOLD = 20.0f; // Default shake threshold
    private float shakeThreshold = DEFAULT_SHAKE_THRESHOLD; // Initial shake threshold

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sendMessage = new SendMessage(MainActivity.this);

        context = this;

        // Set the shake threshold dynamically based on accelerometer maximum range
        if (accelerometer != null) {
            shakeThreshold = accelerometer.getMaximumRange() / 2; // Adjust as needed
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double magnitude = Math.sqrt(x * x + y * y + z * z);

            if (magnitude > shakeThreshold) { // Use dynamic shake threshold
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
