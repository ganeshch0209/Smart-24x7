package com.example.smart24x7;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class SendMessage {

    private static final int REQUEST_LOCATION = 1;

    private String message = "Need Help! I am in DANGER. You can track me at: ";
    private Context context;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public SendMessage(Context context) {
        this.context = context;
    }

    public void sendMessagesToAllNumbers() {
        if (checkLocationPermission()) {
            sendMessagesWithLocation();
        } else {
            Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void sendMessagesWithLocation() {
        // Retrieve all phone numbers from the database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<MyModel> dataList = dbHelper.getAllData();

        // Request location updates
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(context, "Location manager unavailable", Toast.LENGTH_SHORT).show();
        }

        // Iterate over the dataList to send messages to each number
        for (MyModel model : dataList) {
            String phoneNumber = model.getPhoneNo();
            sendMessageToNumber(phoneNumber);
        }
    }

    private void sendMessageToNumber(String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        String locationUrl = "https://www.google.com/maps/search/?api=1&query=" + MyLocationListener.latitude + "," + MyLocationListener.longitude;
        String smsText = message + "\n" + locationUrl;
        smsManager.sendTextMessage(phoneNumber, null, smsText, null, null);
        Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
    }

    private class MyLocationListener implements LocationListener {
        public static double latitude;
        public static double longitude;

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                // Remove location updates after retrieving the current location
                if (locationManager != null && locationListener != null) {
                    locationManager.removeUpdates(locationListener);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
