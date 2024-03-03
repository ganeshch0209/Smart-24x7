package com.example.smart24x7;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
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
    private String phoneNumber;

    private LocationManager locationManager;
    private Context context;

    public SendMessage(Context context) {
        this.context = context;
    }

    public void sendMessagesToAllNumbers() {
        // Retrieve all phone numbers from the database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<MyModel> dataList = dbHelper.getAllData();

        // Iterate over the dataList to send messages to each number
        for (MyModel model : dataList) {
            String phoneNumber = model.getPhoneNo();
            sendMessageToNumber(phoneNumber);
        }
    }

    private void sendMessageToNumber(String phoneNumber) {
        if (!phoneNumber.equals("") && !message.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            getLocation();
            String locationUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            String smsText = message + "\n" + locationUrl;
            smsManager.sendTextMessage(phoneNumber, null, smsText, null, null);
            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show();
        }
    }

    private double latitude, longitude;

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions
            requestLocationPermissions();
        } else {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } else {
                    // If the last known location is not available, request location updates
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                // Remove location updates after retrieving the current location
                                locationManager.removeUpdates(this);
                            }
                        }

                        // Other methods of the LocationListener interface

                        @Override
                        public void onProviderEnabled(String provider) {}

                        @Override
                        public void onProviderDisabled(String provider) {}

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {}
                    });
                }
            } else {
                // Show dialog to enable GPS
                showGPSDisabledDialog();
            }
        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    private void showGPSDisabledDialog() {
        Toast.makeText(context, "Please enable GPS to get location information", Toast.LENGTH_LONG).show();
    }
}
