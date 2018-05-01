package com.outerspace.locatedcactus;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * LocatedCactus: Practice app to use the Google Location Services.
 *
 * Bibliography:
 *
 * Get the last known location
 *      https://developer.android.com/training/location/retrieve-current
 * Receive location updates:
 *      https://developer.android.com/training/location/receive-location-updates
 * LocationRequest:
 *      https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest
 */

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient locationClient;
    private LocationRequest request;
    private LocationCallback callback;

    private static final long FIVE_SECONDS = 5000l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveLastLocation();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.wana_quit_title)
                .setMessage(R.string.wana_quit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    public void onClickGetLocation(View view) {
        retrieveLastLocation();
    }

    public void onClickUpdateLocation(View view) {
        updateLocations();
    }

    public void onClickNiceToKnow(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.nice_to_know)
                .setMessage(R.string.nice_message)
                .setPositiveButton(R.string.nice_to_know, null)
                .create();
        builder.show();
    }

    public void retrieveLastLocation() {

        int permissionCoarse = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int granted = PackageManager.PERMISSION_GRANTED;

        if( permissionCoarse == granted || permissionFine == granted ) {
            locationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            displayLocation(location);
                        }
                    });
        }
    }

    public void updateLocations() {
        int permissionCoarse = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int granted = PackageManager.PERMISSION_GRANTED;

        if( permissionCoarse == granted || permissionFine == granted ) {
            request = new LocationRequest();
            request
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(FIVE_SECONDS);
            callback = new MyLocationCallback();
            locationClient.requestLocationUpdates(request, callback, null);
        }
    }

    private class MyLocationCallback extends LocationCallback {

        public MyLocationCallback() {
        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            for(Location location : locationResult.getLocations()) {
                displayLocation(location);
            }
        }
    }

    private void displayLocation(Location location) {
        TextView txtResult = (TextView) findViewById(R.id.text_result);
        if (location != null) {
            StringBuffer sb = new StringBuffer();
            sb
                    .append("Latitude: ")
                    .append(location.getLatitude())
                    .append('\n')
                    .append("Longitude: ")
                    .append(location.getLongitude())
                    .append('\n')
                    .append("Altitude:")
                    .append(location.getAltitude());
            txtResult.setText(sb.toString());
        }
        else {
            txtResult.setText(R.string.invalid_result);
        }
    }
}
