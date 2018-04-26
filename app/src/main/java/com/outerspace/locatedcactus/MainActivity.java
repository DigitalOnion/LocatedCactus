package com.outerspace.locatedcactus;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveLastLocation();
    }

    public void onClickGetLocation(View view) {
        retrieveLastLocation();
    }

    public void retrieveLastLocation() {

        int permissionCoarse = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int granted = PackageManager.PERMISSION_GRANTED;

        if( permissionCoarse == granted || permissionFine == granted ) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            displayLocation(location);
                        }
                    });
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
