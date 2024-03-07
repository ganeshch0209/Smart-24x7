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
        locationListener = new MyLocationListener(locationManager, dataList);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(context, "Location manager unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLocationListener implements LocationListener {
        private LocationManager locationManager;
        private List<MyModel> dataList;

        private void sendMessageToNumbers(double latitude, double longitude) {
            for (MyModel model : dataList) {
                String phoneNumber = model.getPhoneNo();
                SmsManager smsManager = SmsManager.getDefault();
                String locationUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                String smsText = message + "\n" + locationUrl;
                smsManager.sendTextMessage(phoneNumber, null, smsText, null, null);
            }
            Toast.makeText(context, "Messages sent", Toast.LENGTH_SHORT).show();
        }

        public MyLocationListener(LocationManager locationManager, List<MyModel> dataList) {
            this.locationManager = locationManager;
            this.dataList = dataList;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Remove location updates after retrieving the current location
                if (locationManager != null) {
                    locationManager.removeUpdates(this);
                }
                sendMessageToNumbers(latitude, longitude);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}


    }
}
